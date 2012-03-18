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
        ArrayList<String> resultConceptStrings = new ArrayList<String>();
        ArrayList<ArrayList<Boolean>> resultDocumentTransactions = new ArrayList<ArrayList<Boolean>>();

        for (Document doc : _documentCollection) {
            ArrayList<String> sentences = doc.AIM;

            for (int i = 0; i < sentences.size(); ++i) {
                String a_sentence = sentences.get(i);
                a_sentence = a_sentence.toLowerCase();
                StopwordRemover a = new StopwordRemover();
                a_sentence = a.removeStopword(a_sentence);
                a_sentence = preprocessing.Stemmer.stem(a_sentence);

                String[] a_sentenceSplit = a_sentence.split(" ");
                for (int j = 0; j < a_sentenceSplit.length; ++j) {
                    if (!resultConceptStrings.contains(a_sentenceSplit[j])) {
                        resultConceptStrings.add(a_sentenceSplit[j]);
                    }
                }
            }
        }
        
        conceptStringsAIM = resultConceptStrings;

        for (Document doc : _documentCollection) {
            ArrayList<String> sentences = doc.AIM;
            
            ArrayList<Boolean> documentTransaction = new ArrayList<Boolean>(resultConceptStrings.size());
            for (int i=0; i< resultConceptStrings.size(); ++i) {
                documentTransaction.add(Boolean.FALSE);
            }
            
            for (int i = 0; i < sentences.size(); ++i) {
                String a_sentence = sentences.get(i);
                a_sentence = a_sentence.toLowerCase();
                StopwordRemover a = new StopwordRemover();
                a_sentence = a.removeStopword(a_sentence);
                a_sentence = preprocessing.Stemmer.stem(a_sentence);

                String[] a_sentenceSplit = a_sentence.split(" ");
                for (int j = 0; j < a_sentenceSplit.length; ++j) {
                    int indexInConceptStrings = resultConceptStrings.indexOf(a_sentenceSplit[j]);
                    documentTransaction.set(indexInConceptStrings, Boolean.TRUE);
                }
            }
            resultDocumentTransactions.add(documentTransaction);
        }
        documentTransactionsAIM = resultDocumentTransactions;
        
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
