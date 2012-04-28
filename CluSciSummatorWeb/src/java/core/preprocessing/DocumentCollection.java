/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.preprocessing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import core.utility.Global;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class DocumentCollection {
    /**
     * FIELD DARI CLASS
     * 1. _documentCollection: Koleksi dokumen hasil parsing
     * 2. conceptStrings: Semua konsep yang terambil dari koleksi dokumen
     * 3. documentTransactions: transaksi tiap konsep
     * 4. processedDocuments: Koleksi dokumen yang telah terproses: pembuangan tanda baca, stopword dan konjungsi, wordstemming
     * 
     * CATATAN: nanti field 2, 3, dan 4 gak perlu mencakup semua kategori retorik. Proses aja yang sesuai input
     */
    private ArrayList<Document> _documentCollection;
    public Hashtable<String, ArrayList<String>> conceptStrings;
    public Hashtable<String, ArrayList<ArrayList<Boolean>>> documentTransactions;
    public Hashtable<String, ArrayList<ArrayList<ArrayList<String>>>> processedDocuments;

    
    /**
     * KONSTRUKTOR DARI CLASS
     */
    public DocumentCollection() {
        _documentCollection = new ArrayList<Document>();
        conceptStrings = new Hashtable<String, ArrayList<String>>();
        documentTransactions = new Hashtable<String, ArrayList<ArrayList<Boolean>>>();
        processedDocuments = new Hashtable<String, ArrayList<ArrayList<ArrayList<String>>>>();
    }

    /**
     * METHOD DARI CLASS
     * 1. getDocumentCollection() : akses publik dari field _documentCollection
     * 2. setDocumentCollection(ArrayList<Document>) : mengeset field _documentCollection
     * 3. processDocuments() : memproses koleksi dokumen (praproses, mengumpulkan konsep, membuat transaksi konsep
     * 4. getDocumentByID: Mengembalikan Dokumen yang memiliki ID tertentu
     */
    
    /**
     * Method getDocumentCollection(): akses publik dari field _documentCollection
     * 
     * @return ArrayList<Document>
     */
    public ArrayList<Document> getDocumentCollection() {
        return _documentCollection;
    }

    /**
     * Method setDocumentCollection(): mengeset field _documentCollection
     * 
     * @param _documentCollection 
     */
    public void setDocumentCollection(ArrayList<Document> _documentCollection) {
        this._documentCollection = _documentCollection;
    }

    /**
     * Method processDocuments() : memproses koleksi dokumen (praproses, mengumpulkan konsep, membuat transaksi konsep
     * 
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void processDocuments() throws FileNotFoundException, IOException {
        long startTime = System.currentTimeMillis();

        //sentenceSplittedAllDocument disimpen di HT dengan keynya yaitu tag rhetoric
        Hashtable<String, ArrayList<ArrayList<ArrayList<String>>>> sentenceSplittedAllDocumentHT = new Hashtable<String, ArrayList<ArrayList<ArrayList<String>>>> ();

        //Praproses dokumen:
        for (String tagRhetoric : Global.rhetoricalStatusList) {
            //ArrayList dari kalimat semua dokumen, satu kalimat udah displit jadi satu kata yang udah di praproses
            ArrayList<ArrayList<ArrayList<String>>> sentenceSplittedAllDocument = new ArrayList<ArrayList<ArrayList<String>>>();
            
            for (int i = 0; i < _documentCollection.size(); ++i) {
                Document doc = _documentCollection.get(i);
                //ArrayList dari kalimat dalam 1 dokumen, satu kalimat udah displit jadi satu kata yang udah di praproses
                ArrayList<ArrayList<String>> sentenceSplittedDocument = new ArrayList<ArrayList<String>>();

                ArrayList<String> sentencesByRhetoric = doc.content.get(tagRhetoric);
                
                for (int j = 0; j < sentencesByRhetoric.size(); ++j) {
                    String a_sentence = sentencesByRhetoric.get(j);
                    //Buang tanda baca
                    a_sentence = a_sentence.replaceAll("[^A-Za-z0-9 ]", ""); 
                    a_sentence = a_sentence.toLowerCase();

                    //Remove stopword
                    StopwordRemover a = new StopwordRemover();
                    a_sentence = a.removeStopword(a_sentence);
                    a_sentence = Stemmer.stem(a_sentence);

                    //Split satu kalimat menjadi kata2:
                    String[] a_sentenceSplit = a_sentence.split(" ");
                    //Ubah jadi ArrayList
                    ArrayList<String> a_sentenceSplitArrayList = new ArrayList<String>();
                    for (int k = 0;k < a_sentenceSplit.length; ++k) {
                        if (a_sentenceSplit[k].length() != 0 &&a_sentenceSplit[k].length() > 3 && !a_sentenceSplit[k].matches("-?\\d+(.\\d+)?")){
                            a_sentenceSplitArrayList.add(a_sentenceSplit[k]);
                        }
                    }
                    sentenceSplittedDocument.add(a_sentenceSplitArrayList);
                }
                sentenceSplittedAllDocument.add(sentenceSplittedDocument);
            }
//            //PENGUJIAN 2
//            if (tagRhetoric.equals("aim")) {
//                for (int i=0; i<sentenceSplittedAllDocument.size(); ++i) {
//                    for (int j=0; j<sentenceSplittedAllDocument.get(i).size(); ++j) {
//                        System.out.println(""+sentenceSplittedAllDocument.get(i).get(j));   
//                    }
//                }
//            }
            sentenceSplittedAllDocumentHT.put(tagRhetoric, sentenceSplittedAllDocument);
        }
        
        this.processedDocuments = sentenceSplittedAllDocumentHT;

        //Bikin semua konsep dari hasil praproses dokumen:
        for (String tagRhetoric : Global.rhetoricalStatusList) {
            //Inisiasi variabel, semua konsep dari tiap dokumen
            //resultConceptStringsHT.get(1) -> semua konsep dokumen ke 1
            ArrayList<Hashtable<String, Integer>> resultConceptStringsAllDocs = new ArrayList<Hashtable<String, Integer>>();
            
            ArrayList<ArrayList<ArrayList<String>>> thisSentenceSplittedAllDocument = sentenceSplittedAllDocumentHT.get(tagRhetoric);
            for (int i = 0; i < thisSentenceSplittedAllDocument.size(); ++i) {
                //Semua kalimat dari dokumen ini:
                ArrayList<ArrayList<String>> sentenceSplittedDocument = thisSentenceSplittedAllDocument.get(i);
                //Hashtable buat nyimpen konsep dari dokumen ini, string = konsep, int = frekuensinya
                Hashtable<String, Integer> conceptStringsThisDocument = new Hashtable<String, Integer>();
                
                for (int j = 0; j < sentenceSplittedDocument.size(); ++j) {
                    //Semua kata dari kalimat ini:
                    ArrayList<String> a_sentenceSplit = sentenceSplittedDocument.get(j);
                    for (int k = 0; k < a_sentenceSplit.size(); ++k) {
                        if (!conceptStringsThisDocument.containsKey(a_sentenceSplit.get(k))) {
                            conceptStringsThisDocument.put(a_sentenceSplit.get(k), 1);
                        } else {
                            Integer newFreq = conceptStringsThisDocument.get(a_sentenceSplit.get(k))+ 1;
                            conceptStringsThisDocument.put(a_sentenceSplit.get(k), newFreq);
                        }
                    }
                }
                resultConceptStringsAllDocs.add(conceptStringsThisDocument);
            }
            

            //Inisiasi variabel penyimpan konsep akhir
            ArrayList<String> resultConceptStrings = new ArrayList<String>();
            
            //Buang conceptString yang kemunculannya hanya 1 kali di dokumen tersebut
            for (int i = 0; i < resultConceptStringsAllDocs.size(); ++i) {
                //Konsep dari dokumen ini:
                Hashtable<String, Integer> conceptStringThisDoc = resultConceptStringsAllDocs.get(i);
                //Iterasi semua konsep dari dokumen ini
                //Ambil yang freq > 1
                Enumeration concepts = conceptStringThisDoc.keys();
                while (concepts.hasMoreElements()) {
                    String a_concept = (String) concepts.nextElement();
                    Integer freqThisConcept = conceptStringThisDoc.get(a_concept);
                    if (freqThisConcept > 1) {
                        int exist = resultConceptStrings.indexOf(a_concept);
                        if (exist == -1) {
                            resultConceptStrings.add(a_concept);
                        }
                            
                    }
                }
            }
            
            this.conceptStrings.put(tagRhetoric, resultConceptStrings);
        }
        

        //Bikin transaksi dari semua dokumen untuk setiap kategori retorik:
        for (String tagRhetoric : Global.rhetoricalStatusList) {
            ArrayList<ArrayList<Boolean>> resultDocumentTransactions = new ArrayList<ArrayList<Boolean>>();
            ArrayList<ArrayList<ArrayList<String>>> thisSentenceSplittedAllDocument = sentenceSplittedAllDocumentHT.get(tagRhetoric);
            for (int i = 0; i < thisSentenceSplittedAllDocument.size(); ++i) {
                ArrayList<ArrayList<String>> sentenceSplittedDocument = thisSentenceSplittedAllDocument.get(i);

                ArrayList<Boolean> documentTransaction = new ArrayList<Boolean>(this.conceptStrings.get(tagRhetoric).size());
                for (int j = 0; j < this.conceptStrings.get(tagRhetoric).size(); ++j) {
                    documentTransaction.add(Boolean.FALSE);
                }

                for (int j = 0; j < sentenceSplittedDocument.size(); ++j) {
                    ArrayList<String> a_sentenceSplit = sentenceSplittedDocument.get(j);
                    for (int k = 0; k < a_sentenceSplit.size(); ++k) {
                        int indexInConceptStrings = this.conceptStrings.get(tagRhetoric).indexOf(a_sentenceSplit.get(k));
                        if (indexInConceptStrings != -1) {
                            documentTransaction.set(indexInConceptStrings, Boolean.TRUE);
                        }
                    }
                }
                resultDocumentTransactions.add(documentTransaction);
            }
            this.documentTransactions.put(tagRhetoric, resultDocumentTransactions);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Total elapsed time eksekusi Praproses semua dokumen is :" + (endTime - startTime));
    }

    /**
     * Method getDocumentByID: Mengembalikan Dokumen yang memiliki ID tertentu
     * 
     * @param ID
     * @return Document
     */
    public Document getDocumentByID(int ID) {
        boolean found = false;
        int index = 0;

        Document doc = _documentCollection.get(index);
        while (!found && index < _documentCollection.size()) {
            if (doc.getID() == ID) {
                found = true;
            } else {
                index++;
                doc = _documentCollection.get(index);
            }
        }

        if (found) {
            return _documentCollection.get(index);
        } else {
            return null;
        }
    }
}