/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preprocessor;

import datamodel.Document;
import datamodel.DocumentCollection;
import java.util.ArrayList;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class DocumentCollectionReader {
    /**
     * FIELD DARI CLASS
     * 1. _parsedDocumentCollection: koleksi dokumen yang telah di parse
     */
    private DocumentCollection _parsedDocumentCollection;
        
    
    /**
     * KONSTRUKTOR DARI CLASS
     */
    public DocumentCollectionReader() {
        _parsedDocumentCollection = new DocumentCollection();
    }

    
    /*
     * METHOD DARI CLASS
     * 1. getParsedDocumentCollection() 
     * 2. parseDocumentCollection(ArrayList<String> URIS)
     */
    
    /**
     * Method getParsedDocumentCollection : Akses _parsedDocumentCollection oleh kelas lain
     * @return 
     */
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
