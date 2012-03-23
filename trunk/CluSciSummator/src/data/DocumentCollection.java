/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import preprocessing.StopwordRemover;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class DocumentCollection {

    private ArrayList<Document> _documentCollection;
    public ArrayList<String> conceptStringsAIM;
    public ArrayList<ArrayList<Boolean>> documentTransactionsAIM;

    public DocumentCollection() {
    }

    public ArrayList<Document> getDocumentCollection() {
        return _documentCollection;
    }

    public void setDocumentCollection(ArrayList<Document> _documentCollection) {
        this._documentCollection = _documentCollection;
    }

    public void transactDocumentAIM() throws FileNotFoundException, IOException {
        long startTime = System.currentTimeMillis();
        
        ArrayList<String> resultConceptStrings = new ArrayList<String>();
        ArrayList<ArrayList<Boolean>> resultDocumentTransactions = new ArrayList<ArrayList<Boolean>>();

        ArrayList<ArrayList<String[]>> sentenceSplittedAllDocument = new ArrayList<ArrayList<String[]>>(); //ArrayList dari kalimat, satu kalimat udah displit jadi satu kata yang udah di praproses

        //Praproses dokumen:
        for (int i = 0; i < _documentCollection.size(); ++i) {
            Document doc = _documentCollection.get(i);
            ArrayList<String> sentences = doc.AIM;
            ArrayList<String[]> sentenceSplittedDocument = new ArrayList<String[]>();
            
            for (int j = 0; j < sentences.size(); ++j) {
                String a_sentence = sentences.get(j);
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

        //Bikin semua konsep dari hasil praproses dokumen:
        for (int i = 0; i < sentenceSplittedAllDocument.size(); ++i) {
            ArrayList<String[]> sentenceSplittedDocument = sentenceSplittedAllDocument.get(i);
            for (int j = 0; j < sentenceSplittedDocument.size(); ++j) {
                String[] a_sentenceSplit = sentenceSplittedDocument.get(j);
                for (int k = 0; k < a_sentenceSplit.length; ++k) {
                    if (!resultConceptStrings.contains(a_sentenceSplit[k]) && a_sentenceSplit[k].length()!=0) {
                        resultConceptStrings.add(a_sentenceSplit[k]);
                    }
                }
            }
        }
        conceptStringsAIM = resultConceptStrings;
        
        for (int i = 0; i < sentenceSplittedAllDocument.size(); ++i) {
            ArrayList<String[]> sentenceSplittedDocument = sentenceSplittedAllDocument.get(i);
            
            ArrayList<Boolean> documentTransaction = new ArrayList<Boolean>(resultConceptStrings.size());
            for (int j=0; j< resultConceptStrings.size(); ++j) {
                documentTransaction.add(Boolean.FALSE);
            }
            
            for (int j = 0; j < sentenceSplittedDocument.size(); ++j) {
                String[] a_sentenceSplit = sentenceSplittedDocument.get(j);
                for (int k = 0; k < a_sentenceSplit.length; ++k) {
                    int indexInConceptStrings = resultConceptStrings.indexOf(a_sentenceSplit[k]);
                    if (indexInConceptStrings!=-1)
                        documentTransaction.set(indexInConceptStrings, Boolean.TRUE);
                }
            }
            resultDocumentTransactions.add(documentTransaction);
        }
     
        documentTransactionsAIM = resultDocumentTransactions;
        long endTime = System.currentTimeMillis();
        System.out.println("Total elapsed time in execution of method callMethod() is :"+ (endTime-startTime));

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
