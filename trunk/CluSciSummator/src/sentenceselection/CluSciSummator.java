/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sentenceselection;

import clustering.FTC;
import preprocessing.DocumentCollection;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import preprocessing.DocumentCollectionReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class CluSciSummator {
    /*
     * FIELD DARI CLASS
     * 1. _documentsLocations: URI dari setiap dokumen sumber
     * 2. summarization: hasil ringkasan. Key = cluster (term set), Value = kalimat2 hasil ringkasan
     * 3. _collectionPaperProcessed : koleksi dokumen hasil preprocessing
     */
    private ArrayList<String> _documentsLocations;
    private DocumentCollection _collectionPaperProcessed; 
    public Hashtable<ArrayList<String>, ArrayList<String>> summarization;

    /**
     * KONSTRUKTOR DARI CLASS
     * 
     * @param documentsLocations
     * @param tagRhetoric
     * @param minimumSupport 
     */
    public CluSciSummator(ArrayList<String> documentsLocations) {
        this._documentsLocations = documentsLocations;
        this._collectionPaperProcessed = new DocumentCollection();
    }

    
    /**
     * METHOD DARI CLASS
     * 1. processDocuments() : melakukan pemrosesan dokumen yang diupload
     * 2. summarize(tagRhetoric, minimumSupport) : melakukan pemrosesan peringkasan
     */
    
    /**
     * Method processDocuments() : melakukan pemrosesan dokumen yang diupload
     * 
     */
    public void processDocuments(){
        //1. Parsing semua file dokumen
        DocumentCollectionReader corpusReader = new DocumentCollectionReader();
        corpusReader.parseDocumentCollection(this._documentsLocations);
        //2. Ambil koleksi dokumen hasil parsing
        _collectionPaperProcessed = corpusReader.getParsedDocumentCollection();
        try {
            //3. Proses koleksi dokumen
            _collectionPaperProcessed.processDocuments();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CluSciSummator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CluSciSummator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Method summarize: melakukan pemrosesan peringkasan
     * 
     * tagRhetoric: rhetoric apa yang akan disummarize
     * minimumSupport: minimum support untuk Apriori
     * 
     * @return status summarize
     * @throws FileNotFoundException
     * @throws IOException
     * @throws Exception 
     */
    public int summarize(String tagRhetoric, double minimumSupport) throws Exception {
        this.summarization = new Hashtable<ArrayList<String>, ArrayList<String>>();
        int statusSummarize = 0;
        
        //Jumlah dokumen minimum 
        int minimumDocumentSupport = (int) Math.round(minimumSupport * this._collectionPaperProcessed.documentTransactions.get(tagRhetoric).size());
        if (minimumDocumentSupport < 2) {
            //termset harus ada di >= 2 dokumen. Minimum support terlalu kecil
            statusSummarize = 1;
        } else {
            //Cluster Dokumen
            FTC ftc = new FTC(this._collectionPaperProcessed.conceptStrings.get(tagRhetoric), this._collectionPaperProcessed.documentTransactions.get(tagRhetoric), minimumSupport);
            ftc.filterCluster();
            
            //Hasil cluster
            Enumeration clusters = ftc.finalCluster.keys();
            
            //Untuk setiap cluster di summarize
            while (clusters.hasMoreElements()) {
                    ArrayList<String> frequentTermSet = (ArrayList<String>) clusters.nextElement();
                    ArrayList<Integer> documentsIndex = ftc.finalCluster.get(frequentTermSet);
                    ArrayList<String> kalimatHasilRingkasan = new ArrayList<String> ();
                    
                    //Summarize untuk cluster ini 
                    SentenceSelector sentenceSelector = new SentenceSelector(this._collectionPaperProcessed.processedDocuments.get(tagRhetoric), documentsIndex, frequentTermSet);
                    sentenceSelector.summarize();
                    
                    for (int i=0; i<sentenceSelector.selectedSentence.size();++i) {
                        int[] thisSelectedSentencePoint = sentenceSelector.selectedSentence.get(i);
                        int docID = (int) thisSelectedSentencePoint[0];
                        int sentenceIndex = (int) thisSelectedSentencePoint[1];
                        String sentence = this._collectionPaperProcessed.getDocumentByID(docID).content.get(tagRhetoric).get(sentenceIndex);
                        kalimatHasilRingkasan.add(sentence);
                    }
                    summarization.put(frequentTermSet, kalimatHasilRingkasan);
                }
        }
        
        return statusSummarize;
    }
}
