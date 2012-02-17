/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.ArrayList;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class DocumentCollection {

    private ArrayList<Document> _documentCollection;

    public DocumentCollection() {
    }

    public ArrayList<Document> getDocumentCollection() {
        return _documentCollection;
    }

    public void setDocumentCollection(ArrayList<Document> _documentCollection) {
        this._documentCollection = _documentCollection;
    }

    /**
     * Method getContentByRhetoricalStatus : Memfilter sentence dengan rhetorical status tertentu dari semua koleksi dokumen
     * 
     * @param tag
     * @return 
     */
    public ArrayList<Sentence> getContentByRhetoricalStatus(String tag) {
        ArrayList<Sentence> result = new ArrayList<Sentence>();
        for (Document doc : _documentCollection) {
            for (Sentence sentence : doc.getContent()) {
                if (sentence.getTag().equalsIgnoreCase(tag)) {
                    Sentence tobeadd = new Sentence(sentence.getContent(), sentence.getTag(), sentence.getDocID());
                    result.add(sentence);
                }
            }
        }
        return result;
    }

    /**
     * Method getDocumentByID: Mengembalikan Dokumen yang memiliki ID tertentu
     * 
     * @param ID
     * @return 
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
