/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package summarization;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class SentenceSelector {

    private final double LAMBDA = 0.5;
    //Koleksi dokumen yang telah diproses untuk 1 kategori retorik
    private ArrayList<ArrayList<ArrayList<String>>> _processedDocuments;
    //Indeks dokumen-dokumen yang ingin di ringkas
    private ArrayList<Integer> _docsIndexes;
    //Query / ClusterDescription:
    private ArrayList<String> _query;
    //Array Kalimat yang terpilih hasil selection, (X,Y): X = indeks dokumen dalam koleksi, Y = indeks kalimat dalam dokumen tsb
    public ArrayList<Point> selectedSentence;

    public SentenceSelector(ArrayList<ArrayList<ArrayList<String>>> processedDocuments, ArrayList<Integer> docsIndexes, ArrayList<String> query) {
        this._processedDocuments = processedDocuments;
        this._docsIndexes = docsIndexes;
        this._query = query;
        this.selectedSentence = new ArrayList<Point>();
    }

    /*
     * Method summarize: Memilih kalimat-kalimat
     * R: koleksi kalimat yang akan di summarize
     * S: koleksi kalimat yang telah terpilih
     * R_S: koleksi kalimat yang belum terpilih
     */
    public void summarize() {
        ArrayList<ArrayList<String>> R = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < _docsIndexes.size(); ++i) {
            Integer docIndex = _docsIndexes.get(i);
            ArrayList<ArrayList<String>> thisProcessedDoc = _processedDocuments.get(docIndex);
            R.addAll(thisProcessedDoc);
        }
        System.out.println("R: "+R);
        
        ArrayList<ArrayList<String>> S = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> R_S = new ArrayList<ArrayList<String>>(R);
        
        //Tahap Iterasi MMR, pemilihan kalimat
        //Berhenti jika Jumlah kalimat pada S atau batas nilai relevansi tertentu tercapai
        //batasNilaiRelevansi akan jadi false jika tidak ada satu kalimat pun yang MRnya > 0
        boolean batasNilaiRelevansi = true;
        while (S.size()!= R.size() && batasNilaiRelevansi) {
            //Iterasi R_S:
            double MMR = -999;
            ArrayList<String> sentenceSelected = new ArrayList<String>();
            for (int i = 0; i<R_S.size(); ++i) {
                ArrayList<String> theSentence = R_S.get(i);
                double MRThisSentence = marginalRelevance(theSentence, _query, S);
                if (MRThisSentence > MMR) {
                    MMR = MRThisSentence;
                    sentenceSelected = theSentence;
                }
//                System.out.println("MRThisSen: "+MRThisSentence);
            }
            if (MMR < 0)
                batasNilaiRelevansi = false;
            else {   
                System.out.println("SENTENCE SELECTED: "+sentenceSelected);
                S.add(sentenceSelected);
                R_S.remove(sentenceSelected);
            }
        }
        
        //Cari semua kalimat di S ada di docIndex dan sentenceIndex mana
        for (int i=0; i<S.size();++i) {
            ArrayList<String> theSentence = S.get(i);
            
            for (int j = 0; j < _docsIndexes.size(); ++j) {
                Integer docIndex = _docsIndexes.get(j);
                ArrayList<ArrayList<String>> thisProcessedDoc = _processedDocuments.get(docIndex);
                int indexSentence = thisProcessedDoc.indexOf(theSentence);
                //Index !=-1 artinya theSentence ada di Dokumen ini
                if (indexSentence != -1) {
                    Point selectedSentencePoint = new Point(docIndex, indexSentence);
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
     * @return 
     */
    public double marginalRelevance(ArrayList<String> theSentence, ArrayList<String> query, ArrayList<ArrayList<String>> S) {
        double result = 0;

        double similaritySentenceToQuery = cosineSimilarity(theSentence, query);

        double maxSimilaritySentenceToS = 0;
        for (int i = 0; i < S.size(); ++i) {
            ArrayList<String> thisSentenceInS = S.get(i);
            double similarityThisSentenceInSToTheSentence = cosineSimilarity(theSentence, thisSentenceInS);
            if (similarityThisSentenceInSToTheSentence > maxSimilaritySentenceToS) {
                maxSimilaritySentenceToS = similarityThisSentenceInSToTheSentence;
            }
        }

        result = this.LAMBDA * similaritySentenceToQuery - ((1 - this.LAMBDA) * maxSimilaritySentenceToS);
        return result;
    }

    /**
     * Method cosineSimilarity
     * @param sentence1
     * @param sentence2
     * 
     * Menghitung similarity antara 2 kalimat
     * 
     * @return nilai similiary (double)
     */
    public double cosineSimilarity(ArrayList<String> sentence1, ArrayList<String> sentence2) {
        double result = 0;
        VectorSpaceModel sentence1_VSM = new VectorSpaceModel(sentence1);
        VectorSpaceModel sentence2_VSM = new VectorSpaceModel(sentence2);
//        System.out.println("VSM 1:" + sentence1_VSM.VSM);
//        System.out.println("VSM 2:" + sentence2_VSM.VSM);

        Enumeration allTermSentence1 = sentence1_VSM.VSM.keys();
        while (allTermSentence1.hasMoreElements()) {
            String a_term = (String) allTermSentence1.nextElement();
            Integer tfInSentence1 = sentence1_VSM.VSM.get(a_term);
            Integer tfInSentence2 = 0;
            if (sentence2_VSM.VSM.containsKey(a_term)) {
                tfInSentence2 = sentence2_VSM.VSM.get(a_term);
            }

            result += tfInSentence1 * tfInSentence2;
        }

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

//        SentenceSelector a = new SentenceSelector();
//        double similarity = a.cosineSimilarity(tesString, tesString2);
//        System.out.println("String 1: " + tesString);
//        System.out.println("String 2: " + tesString2);
//
//        System.out.println("SIM: " + similarity);
    }
}
