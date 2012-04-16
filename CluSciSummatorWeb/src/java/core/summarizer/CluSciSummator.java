/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.summarizer;

import core.clusterer.FTC;
import core.datamodel.DocumentCollection;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import core.preprocessor.DocumentCollectionReader;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class CluSciSummator {
    /*
     * FIELD DARI CLASS
     * 1. _documentsLocations: URI dari setiap dokumen sumber
     * 2. _tagRhetoric: rhetoric apa yang akan disummarize
     * 3. _minimumSupport: minimum support untuk Apriori
     * 4. summarization: hasil ringkasan. Key = cluster (term set), Value = kalimat2 hasil ringkasan
     */
    private ArrayList<String> _documentsLocations;
    private String _tagRhetoric;
    private double _minimumSupport;
    public Hashtable<ArrayList<String>, ArrayList<String>> summarization;

    /**
     * KONSTRUKTOR DARI CLASS
     * 
     * @param documentsLocations
     * @param tagRhetoric
     * @param minimumSupport 
     */
    public CluSciSummator(ArrayList<String> documentsLocations, String tagRhetoric, double minimumSupport) {
        this._documentsLocations = documentsLocations;
        this._tagRhetoric = tagRhetoric;
        this._minimumSupport = minimumSupport;
        this. summarization = new Hashtable<ArrayList<String>, ArrayList<String>>();
    }

    /**
     * METHOD DARI CLASS
     */
    /**
     * Method summarize: melakukan pemrosesan peringkasan
     * 
     * @return status summarize
     * @throws FileNotFoundException
     * @throws IOException
     * @throws Exception 
     */
    public int summarize() throws FileNotFoundException, IOException, Exception {
        int statusSummarize = 0;
        //1. Parsing semua file dokumen
        DocumentCollectionReader corpusReader = new DocumentCollectionReader();
        corpusReader.parseDocumentCollection(this._documentsLocations);
        //2. Ambil koleksi dokumen hasil parsing
        DocumentCollection collectionPaper = corpusReader.getParsedDocumentCollection();
        //3. Proses koleksi dokumen
        collectionPaper.processDocuments();

        //Jumlah dokumen minimum 
        int minimumDocumentSupport = (int) Math.round(this._minimumSupport * collectionPaper.documentTransactions.get(this._tagRhetoric).size());
        if (minimumDocumentSupport < 2) {
            //termset harus ada di >= 2 dokumen. Minimum support terlalu kecil
            statusSummarize = 1;
        } else {
            //Cluster Dokumen
            FTC ftc = new FTC(collectionPaper.conceptStrings.get(this._tagRhetoric), collectionPaper.documentTransactions.get(this._tagRhetoric), this._minimumSupport);
            ftc.filterCluster();
            
            //Hasil cluster
            Enumeration clusters = ftc.finalCluster.keys();
            
            //Untuk setiap cluster di summarize
            while (clusters.hasMoreElements()) {
                    ArrayList<String> frequentTermSet = (ArrayList<String>) clusters.nextElement();
                    ArrayList<Integer> documentsIndex = ftc.finalCluster.get(frequentTermSet);
                    ArrayList<String> kalimatHasilRingkasan = new ArrayList<String> ();
                    
                    //Summarize untuk cluster ini 
                    SentenceSelector sentenceSelector = new SentenceSelector(collectionPaper.processedDocuments.get(this._tagRhetoric), documentsIndex, frequentTermSet);
                    sentenceSelector.summarize();
                    
                    for (int i=0; i<sentenceSelector.selectedSentence.size();++i) {
                        int[] thisSelectedSentencePoint = sentenceSelector.selectedSentence.get(i);
                        int docID = (int) thisSelectedSentencePoint[0];
                        int sentenceIndex = (int) thisSelectedSentencePoint[1];
                        String sentence = collectionPaper.getDocumentByID(docID).content.get(this._tagRhetoric).get(sentenceIndex);
                        kalimatHasilRingkasan.add(sentence);
                    }
                    summarization.put(frequentTermSet, kalimatHasilRingkasan);
                }
        }
        
        return statusSummarize;
    }
}
