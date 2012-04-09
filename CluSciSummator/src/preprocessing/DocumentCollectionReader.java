/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preprocessing;

import model.Document;
import model.DocumentCollection;
import java.util.ArrayList;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class DocumentCollectionReader {
    private DocumentCollection _parsedDocumentCollection;
        
    public DocumentCollectionReader() {
        _parsedDocumentCollection = new DocumentCollection();
    }

    public DocumentCollection getParsedDocumentCollection() {
        return _parsedDocumentCollection;
    }
        
    /**
     * Method parseDocumentCollection : Melakukan parsing semua dokumen XML dari alamat-alamat di URIS
     * 
     * @param URIS 
     */
    public void parseDocumentCollection(ArrayList<String> URIS){
        int docID = 0;
        ArrayList<Document> documents = new ArrayList<Document>();
        for (String URI : URIS) {
           XMLParser parser = new XMLParser();
           parser.parseDocument(URI, docID);
           Document parsedDoc = parser.getParsedDocument();
           documents.add(parsedDoc);

           ++docID;
       }
        _parsedDocumentCollection.setDocumentCollection(documents);
    }
        
        
}
