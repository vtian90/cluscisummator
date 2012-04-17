/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package summarizer;

import datamodel.VectorSpaceModel;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class SentenceSelector {
    /* 
     * FIELD
     */
    //Parameter lambda untuk perhitungan MMR

    private double _lambda;
    //Koleksi dokumen yang telah diproses untuk 1 kategori retorik
    private ArrayList<ArrayList<ArrayList<String>>> _processedDocuments;
    //Koleksi kalimat yang akan di-summarize:
    private ArrayList<ArrayList<String>> _R;
    //Indeks dokumen-dokumen yang ingin di ringkas
    private ArrayList<Integer> _docsIndexes;
    //Query / ClusterDescription:
    private ArrayList<String> _query;
    //Array Kalimat yang terpilih hasil selection, (X,Y): X = indeks dokumen dalam koleksi, Y = indeks kalimat dalam dokumen tsb
    public ArrayList<int[]> selectedSentence;

    /**
     * CONSTRUCTOR
     * @param processedDocuments
     * @param docsIndexes
     * @param query 
     */
    public SentenceSelector(ArrayList<ArrayList<ArrayList<String>>> processedDocuments, ArrayList<Integer> docsIndexes, ArrayList<String> query) {
        this._processedDocuments = processedDocuments;
        this._docsIndexes = docsIndexes;
        this._query = query;
        this.selectedSentence = new ArrayList<int[]>();
        this._R = new ArrayList<ArrayList<String>>();
    }

    /*
     * METHOD
     */
    /**
     * Method initSentenceSelection: inisiasi pemilihan kalimat
     * 1. Membuat R dari _processedDocument, R = koleksi kalimat yang akan disummarize
     * 2. Menghitung nilai lambda
     */
    private void initSentenceSelection() {
        //1. Membuat R
        for (int i = 0; i < _docsIndexes.size(); ++i) {
            Integer docIndex = _docsIndexes.get(i);
            ArrayList<ArrayList<String>> thisProcessedDoc = _processedDocuments.get(docIndex);
            _R.addAll(thisProcessedDoc);
        }

        //2. Menghitung nilai lambda dari R
        double averageSimilaritySentences = 0;
        int sizeR = this._R.size();
        int numberOfSentenceCombination = (sizeR) * (sizeR - 1) / 2;
        System.out.println("NUMBER OF SENTENCE COMBINATION = "+numberOfSentenceCombination);
        double sumOfSentenceSimilarity = 0;
        for (int i = 0; i < sizeR - 1; ++i) {
            ArrayList<String> sentence1 = this._R.get(i);
            for (int j = i + i; j < sizeR; ++j) {
                ArrayList<String> sentence2 = this._R.get(j);
                double similaritySentence1to2 = sentenceSimilarity(sentence1, sentence2);
                System.out.println("SEN1"+sentence1);
                System.out.println("SEN2"+sentence2);
                sumOfSentenceSimilarity += similaritySentence1to2;
                System.out.println("SENTENCE SIMILARITY = "+similaritySentence1to2);
            }
        }
        System.out.println("SUM OF SENTENCE SIMILARITY = "+sumOfSentenceSimilarity);
        averageSimilaritySentences = (double) sumOfSentenceSimilarity/numberOfSentenceCombination;
        System.out.println("average Sentence Similarity" + averageSimilaritySentences);

        this._lambda = 0.5 + 0.5 * (1 - averageSimilaritySentences);
        System.out.println("LAMDA = "+this._lambda);
    }

    /*
     * Method summarize: Memilih kalimat-kalimat
     * R: koleksi kalimat yang akan di summarize
     * S: koleksi kalimat yang telah terpilih
     * R_S: koleksi kalimat yang belum terpilih
     */
    public void summarize() {
        initSentenceSelection();
        ArrayList<ArrayList<String>> S = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> R_S = new ArrayList<ArrayList<String>>(this._R);

        //Tahap Iterasi MMR, pemilihan kalimat
        //Berhenti jika Jumlah kalimat pada S atau batas nilai relevansi tertentu tercapai
        //batasNilaiRelevansi akan jadi false jika tidak ada satu kalimat pun yang MRnya > 0.1
        boolean batasNilaiRelevansi = true;
        while (S.size() != this._R.size() && batasNilaiRelevansi) {
            //Iterasi R_S:
            double MMR = -999;
            ArrayList<String> sentenceSelected = new ArrayList<String>();
            for (int i = 0; i < R_S.size(); ++i) {
                ArrayList<String> theSentence = R_S.get(i);
                double[] MRThisSentence = marginalRelevance(theSentence, _query, S);
                
                //Selain MMR tinggi, kalimat diambil juga apabila max(similarity sentence ini terhadap S) < 0.5
                if (MRThisSentence[0] > MMR && MRThisSentence[1] < 0.5) {
                    MMR = MRThisSentence[0];
                    sentenceSelected = theSentence;
                }
                System.out.println("MRThisSen: " + theSentence + " = " + MRThisSentence[0]+" Redun = "+MRThisSentence[1]);
            }
            if (MMR < 0.1) {
                batasNilaiRelevansi = false;
            } else {
                System.out.println("SENTENCE SELECTED: " + sentenceSelected);
                S.add(sentenceSelected);
                R_S.remove(sentenceSelected);
            }
        }

        //Cari semua kalimat di S ada di docIndex dan sentenceIndex mana
        for (int i = 0; i < S.size(); ++i) {
            ArrayList<String> theSentence = S.get(i);

            for (int j = 0; j < _docsIndexes.size(); ++j) {
                Integer docIndex = _docsIndexes.get(j);
                ArrayList<ArrayList<String>> thisProcessedDoc = _processedDocuments.get(docIndex);
                int indexSentence = thisProcessedDoc.indexOf(theSentence);
                //Index !=-1 artinya theSentence ada di Dokumen ini
                if (indexSentence != -1) {
                    int[] selectedSentencePoint = new int[2];
                    selectedSentencePoint[0] = docIndex;
                    selectedSentencePoint[1] = indexSentence;
                    this.selectedSentence.add(selectedSentencePoint);
                }
            }
        }
        System.out.println("");

    }

    /**
     * Method marginalRelevance: menghitung MR dari sebuah sentence
     * @param lambda  = paramater dengan interval [0,1] untuk mengatur tingkat kepentingan relatif antara relevansi dan redundansi
     * @param theSentence = kalimat yang akan dievaluasi nilai MRnya
     * @param query = query
     * @param S = kumpulan kalimat yang telah terambil
     * @return double[]. result[0] = MR keseluruhan, result[1] = max(simSentenceToS)
     */
    public double[] marginalRelevance(ArrayList<String> theSentence, ArrayList<String> query, ArrayList<ArrayList<String>> S) {
        double[] result = new double[2];

        double similaritySentenceToQuery = sentenceSimilarity(theSentence, query);

        double maxSimilaritySentenceToS = 0;
        for (int i = 0; i < S.size(); ++i) {
            ArrayList<String> thisSentenceInS = S.get(i);
            double similarityThisSentenceInSToTheSentence = sentenceSimilarity(theSentence, thisSentenceInS);
            if (similarityThisSentenceInSToTheSentence > maxSimilaritySentenceToS) {
                maxSimilaritySentenceToS = similarityThisSentenceInSToTheSentence;
            }
        }

        result[0] = this._lambda * similaritySentenceToQuery - ((1 - this._lambda) * maxSimilaritySentenceToS);
        result[1] = maxSimilaritySentenceToS;
        return result;
    }

    /**
     * Method sentenceSimilarity
     * @param sentence1
     * @param sentence2
     * 
     * Menghitung similarity antara 2 kalimat
     * 
     * @return nilai similiary (double)
     */
    public double sentenceSimilarity(ArrayList<String> sentence1, ArrayList<String> sentence2) {
        double result = 0;
        VectorSpaceModel sentence1_VSM = new VectorSpaceModel(sentence1);
        VectorSpaceModel sentence2_VSM = new VectorSpaceModel(sentence2);

        result = sentence1_VSM.cosineSimilarity(sentence2_VSM);
        return result;
    }

    /*
     * Method Tester
     */
    public static void main(String[] args) {
        ArrayList<String> tesString = new ArrayList<String>();
        tesString.add("a");
        tesString.add("boy");
        tesString.add("is");
        tesString.add("crying");
        tesString.add("out");
        tesString.add("loud");
        tesString.add("in");
        tesString.add("a");
        tesString.add("room");
        tesString.add("a");
        tesString.add("boy");

        ArrayList<String> tesString2 = new ArrayList<String>();
        tesString2.add("a");
        tesString2.add("boy");
        tesString2.add("is");
        tesString2.add("crying");
        tesString2.add("out");
        tesString2.add("loud");
        tesString2.add("in");
        tesString2.add("a");
        tesString2.add("room");
        tesString2.add("garut");
        tesString2.add("dodol");

        SentenceSelector a = new SentenceSelector(null, null, null);
        double similarity = a.sentenceSimilarity(tesString, tesString2);
        System.out.println("String 1: " + tesString);
        System.out.println("String 2: " + tesString2);
        System.out.println("SIM: " + similarity);
    }
}
