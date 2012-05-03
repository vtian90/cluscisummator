/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.sentenceselection;

import core.clustering.FTC;
import core.preprocessing.DocumentCollection;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import core.preprocessing.DocumentCollectionReader;
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
     * 2. summarization: hasil ringkasan. Key = cluster (term set), Value = Summary
     *    Summary terdiri dari ArrayList<A>, 
     *    A = ArrayList<String> A[0] = title, A[1] = linknya, A[2] = kalimatnya
     * 3. _collectionPaperProcessed : koleksi dokumen hasil preprocessing
     */
    private ArrayList<String> _documentsLocations;
    private DocumentCollection _collectionPaperProcessed; 
    public Hashtable<ArrayList<String>, ArrayList<ArrayList<String>>> summary;

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
     * status Summarize:
     * 0 = sukses
     * 1 = Minimum support terlalu kecil. Frequent term set harus ada di >= 2 dokumen
     * @return status summarize
     * @throws FileNotFoundException
     * @throws IOException
     * @throws Exception 
     */
    public int summarize(String tagRhetoric, double minimumSupport) throws Exception {
        this.summary = new Hashtable<ArrayList<String>, ArrayList<ArrayList<String>>>();
        int statusSummarize = 0;
        
        //Jumlah dokumen minimum 
        int minimumDocumentSupport = (int) Math.round(minimumSupport * this._collectionPaperProcessed.documentTransactions.get(tagRhetoric).size());
        
        if (minimumDocumentSupport < 2) {
            //termset harus ada di >= 2 dokumen. Minimum support terlalu kecil
            statusSummarize = 1;
        } else if (this._collectionPaperProcessed.processedDocuments.size()==0) {
            //Tidak ada kalimat dengan kategori retorik ini dari semua dokumen
            statusSummarize = 2;
        } else if (this._collectionPaperProcessed.conceptStrings.get(tagRhetoric).size() == 0) {
            //Tidak ada konsep dari kategori retorik ini
            statusSummarize = 3;
        } else {
            //Cluster Dokumen
            FTC ftc = new FTC(this._collectionPaperProcessed.conceptStrings.get(tagRhetoric), this._collectionPaperProcessed.documentTransactions.get(tagRhetoric), minimumSupport);
            int statusFilter = ftc.filterCluster();
            if (statusFilter == 1) {
                //Tidak ada frequent term set hasil Apriori
                statusSummarize = 4;
            } else {
                //Hasil cluster
                Enumeration clusters = ftc.finalCluster.keys();

                //Untuk setiap cluster di summarize
                while (clusters.hasMoreElements()) {
                        ArrayList<String> frequentTermSet = (ArrayList<String>) clusters.nextElement();
                        ArrayList<Integer> documentsIndex = ftc.finalCluster.get(frequentTermSet);
                        
                        ArrayList<ArrayList<String>> ringkasanCluster = new ArrayList<ArrayList<String>> ();
                        

                        //Summarize untuk cluster ini 
                        SentenceSelector sentenceSelector = new SentenceSelector(this._collectionPaperProcessed.processedDocuments.get(tagRhetoric), documentsIndex, frequentTermSet);
                        sentenceSelector.summarize();

                        for (int i=0; i<sentenceSelector.selectedSentence.size();++i) {
                            int[] thisSelectedSentencePoint = sentenceSelector.selectedSentence.get(i);
                            int docID = (int) thisSelectedSentencePoint[0];
                            int sentenceIndex = (int) thisSelectedSentencePoint[1];
                            
                            String title = this._collectionPaperProcessed.getDocumentByID(docID).getTitle();
                            String sentence = this._collectionPaperProcessed.getDocumentByID(docID).content.get(tagRhetoric).get(sentenceIndex);

                            ArrayList<String> detailKalimatRingkasan = new ArrayList<String>();
                            detailKalimatRingkasan.add(title);
                            detailKalimatRingkasan.add(sentence);
                            ringkasanCluster.add(detailKalimatRingkasan);
                        }
                        
                        //summarization.put(frequentTermSet, kalimatHasilRingkasan);
                        this.summary.put(frequentTermSet, ringkasanCluster);
                    }
            }
        }
        return statusSummarize;
    }
}
