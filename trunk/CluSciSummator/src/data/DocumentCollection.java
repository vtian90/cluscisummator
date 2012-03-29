/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import preprocessing.StopwordRemover;
import utility.Global;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class DocumentCollection {

    private ArrayList<Document> _documentCollection;
    public Hashtable<String, ArrayList<String>> conceptStrings;
    public Hashtable<String, ArrayList<ArrayList<Boolean>>> documentTransactions;

    public DocumentCollection() {
        conceptStrings = new Hashtable<String, ArrayList<String>>();
        documentTransactions = new Hashtable<String, ArrayList<ArrayList<Boolean>>>();
    }

    public ArrayList<Document> getDocumentCollection() {
        return _documentCollection;
    }

    public void setDocumentCollection(ArrayList<Document> _documentCollection) {
        this._documentCollection = _documentCollection;
    }

    public void transactDocuments() throws FileNotFoundException, IOException {
        long startTime = System.currentTimeMillis();

        Hashtable<String, ArrayList<ArrayList<String[]>>> sentenceSplittedAllDocumentHT = new Hashtable<String, ArrayList<ArrayList<String[]>>>(); //sentenceSplittedAllDocument disimpen di HT dengan keynya yaitu tag rhetoric

        //Praproses dokumen:
        for (String tagRhetoric : Global.rhetoricalStatusList) {
            ArrayList<ArrayList<String[]>> sentenceSplittedAllDocument = new ArrayList<ArrayList<String[]>>(); //ArrayList dari kalimat, satu kalimat udah displit jadi satu kata yang udah di praproses
            for (int i = 0; i < _documentCollection.size(); ++i) {
                Document doc = _documentCollection.get(i);

                ArrayList<String[]> sentenceSplittedDocument = new ArrayList<String[]>();

                ArrayList<String> sentencesByRhetoric = doc.content.get(tagRhetoric);
                for (int j = 0; j < sentencesByRhetoric.size(); ++j) {
                    String a_sentence = sentencesByRhetoric.get(j);
                    a_sentence = a_sentence.replaceAll("[^A-Za-z0-9 ]", ""); //buang tanda baca
                    a_sentence = a_sentence.toLowerCase();

                    StopwordRemover a = new StopwordRemover();
                    a_sentence = a.removeStopword(a_sentence);
                    a_sentence = preprocessing.Stemmer.stem(a_sentence);

                    String[] a_sentenceSplit = a_sentence.split(" ");
                    sentenceSplittedDocument.add(a_sentenceSplit);
                }
                sentenceSplittedAllDocument.add(sentenceSplittedDocument);
            }
            sentenceSplittedAllDocumentHT.put(tagRhetoric, sentenceSplittedAllDocument);
        }


        //Bikin semua konsep dari hasil praproses dokumen:
        for (String tagRhetoric : Global.rhetoricalStatusList) {
            ArrayList<String> resultConceptStrings = new ArrayList<String>();
            ArrayList<ArrayList<String[]>> thisSentenceSplittedAllDocument = sentenceSplittedAllDocumentHT.get(tagRhetoric);
            for (int i = 0; i < thisSentenceSplittedAllDocument.size(); ++i) {
                ArrayList<String[]> sentenceSplittedDocument = thisSentenceSplittedAllDocument.get(i);
                for (int j = 0; j < sentenceSplittedDocument.size(); ++j) {
                    String[] a_sentenceSplit = sentenceSplittedDocument.get(j);
                    for (int k = 0; k < a_sentenceSplit.length; ++k) {
                        
                        if (!resultConceptStrings.contains(a_sentenceSplit[k]) && a_sentenceSplit[k].length() != 0 &&a_sentenceSplit[k].length() > 3 && !a_sentenceSplit[k].matches("-?\\d+(.\\d+)?")){
                            resultConceptStrings.add(a_sentenceSplit[k]);
                        }
                    }
                }
            }
            this.conceptStrings.put(tagRhetoric, resultConceptStrings);
        }


        //Bikin transaksi dari semua dokumen untuk setiap kategori retorik:
        for (String tagRhetoric : Global.rhetoricalStatusList) {
            ArrayList<ArrayList<Boolean>> resultDocumentTransactions = new ArrayList<ArrayList<Boolean>>();
            ArrayList<ArrayList<String[]>> thisSentenceSplittedAllDocument = sentenceSplittedAllDocumentHT.get(tagRhetoric);
            for (int i = 0; i < thisSentenceSplittedAllDocument.size(); ++i) {
                ArrayList<String[]> sentenceSplittedDocument = thisSentenceSplittedAllDocument.get(i);

                ArrayList<Boolean> documentTransaction = new ArrayList<Boolean>(this.conceptStrings.get(tagRhetoric).size());
                for (int j = 0; j < this.conceptStrings.get(tagRhetoric).size(); ++j) {
                    documentTransaction.add(Boolean.FALSE);
                }

                for (int j = 0; j < sentenceSplittedDocument.size(); ++j) {
                    String[] a_sentenceSplit = sentenceSplittedDocument.get(j);
                    for (int k = 0; k < a_sentenceSplit.length; ++k) {
                        int indexInConceptStrings = this.conceptStrings.get(tagRhetoric).indexOf(a_sentenceSplit[k]);
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
