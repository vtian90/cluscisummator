/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import clustering.FTC;
import java.awt.Point;
import preprocessing.DocumentCollection;
import preprocessing.Document;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import preprocessing.DocumentCollectionReader;
import sentenceselection.CluSciSummator;
import sentenceselection.SentenceSelector;
import utility.Global;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {

        String URI1 = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\Dataset PAPER\\final200511\\A92-1024(1).xml";
        String URI2 = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\Dataset PAPER\\final200511\\A97-1049_FINAL_1.xml";
        String URI3 = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\Dataset PAPER\\final200511\\C02-1144_FINAL_2.xml";
        //String URI4 = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\Dataset PAPER\\final200511\\A97-1053(1).xml";
        ArrayList<String> URIS = new ArrayList<String>();
        URIS.add(URI1);
        URIS.add(URI2);
        URIS.add(URI3);
        //URIS.add(URI4);

        CluSciSummator summarizer = new CluSciSummator(URIS);
        summarizer.processDocuments();
        summarizer.summarize("aim", 0.7);
        
        Enumeration clusters = summarizer.summarization.keys();
        while (clusters.hasMoreElements()) {
            ArrayList<String> cluster = (ArrayList<String>) clusters.nextElement();
            System.out.println(""+cluster+" : ");
            ArrayList<String> sentences = summarizer.summarization.get(cluster);
            for (int i=0; i<sentences.size();++i) {
                System.out.println(""+sentences.get(i));
            }
            System.out.println("");
        }
        
//        /*
//         * PENGUJIAN KOMPONEN PREPROCESSING
//         * 1.	Semua kalimat dengan kategori retorik aim ditampilkan.
//         * 2.	Perbandingan antara kalimat-kalimat asli dengan kalimat-kalimat yang telah dibuang tanda baca, stopword, dan konjungsinya.
//         * 3.	Perbandingan antara kalimat-kalimat yang telah dibuang tanda baca, stopword, dan konjungsinya dengan kalimat-kalimat yang telah di word stemming.
//         * 4.	Semua konsep dan transaksi konsep ditampilkan.
//         */
//        
//         DocumentCollectionReader corpusReader = new DocumentCollectionReader();
//         corpusReader.parseDocumentCollection(URIS);
//         DocumentCollection collectionPaper = corpusReader.getParsedDocumentCollection();
//         collectionPaper.processDocuments();
//         
//         /*
//          * PENGUJIAN 1
//          */
//         ArrayList<Document> docs = collectionPaper.getDocumentCollection();
//         for (Document doc : docs) {
//             System.out.println("From Paper '" + doc.getTitle() + "':");
//             for (int i = 0; i < doc.content.get("aim").size(); ++i) {
//                 System.out.println(doc.content.get("aim").get(i));
//             }
//             System.out.println("");
//         }
//         
//         /*
//          * PENGUJIAN 2
//          */
//         
//         /**
//          * PENGUJIAN 3
//          */
//          System.out.println("ALL CONCEPTS: ");
//          ArrayList<String> allConcepts = collectionPaper.conceptStrings.get("aim");
//          System.out.println("Concepts dari " + "aim" + " : " + allConcepts);
//          System.out.println("Jumlah Concepts dari " + "aim" + " : " + allConcepts.size());
//          for (int i = 0; i < allConcepts.size(); ++i) {
//              System.out.print(allConcepts.get(i) + "|");
//          }
//          
//          /* System.out.println("WEKA FORMAT:");
//            for (int i = 0; i < allConcepts.size(); ++i) {
//            System.out.println("@attribute " + allConcepts.get(i) + " {P}");
//            } */
//
//            ArrayList<Integer> spaces = new ArrayList<Integer>();
//            System.out.println("\n");
//            for (int i = 0; i < allConcepts.size(); ++i) {
//                System.out.print(allConcepts.get(i) + "|");
//                spaces.add(allConcepts.get(i).length() - 1);
//            }
//
//            System.out.println("");
//            for (int i = 0; i < collectionPaper.documentTransactions.get("aim").size(); ++i) {
//                ArrayList<Boolean> thisTrans = collectionPaper.documentTransactions.get("aim").get(i);
//                for (int j = 0; j < thisTrans.size(); ++j) {
//                    if (thisTrans.get(j)) {
//                        for (int k = 0; k < spaces.get(j); k++) {
//                            System.out.print(" ");
//                        }
//                        System.out.print("P|");
//                    } else {
//                        for (int k = 0; k < spaces.get(j); k++) {
//                            System.out.print(" ");
//                        }
//                        System.out.print("?|");
//                    }
//                }
//                System.out.println("");
//            }
//         
//            
//            /**
//             * PENGUJIAN KOMPONEN CLUSTERING
//             * 1. Frequent term set dengan minimum support = 0.7
//             * 2. Nilai EO dari setiap frequent term set dan frequent term set yang terpilih pada setiap iterasi.
//             */
//            
//            double minimumSupport = 0.7;
//            int minimumDocumentSupport = (int) Math.round(minimumSupport * collectionPaper.documentTransactions.get("aim").size());
//            if (minimumDocumentSupport < 2) {
//                System.out.println("Itemset harus ada di >= 2 dokumen! Naikkan minimum support");
//            } else {
//                FTC ftc = new FTC(collectionPaper.conceptStrings.get("aim"), collectionPaper.documentTransactions.get("aim"), minimumSupport);
//                ftc.filterCluster();
//
//                System.out.println("\n-------HASIL FILTERING----------");
//                Enumeration keys2 = ftc.finalCluster.keys();
//                while (keys2.hasMoreElements()) {
//                    ArrayList<String> frequentTermSet = (ArrayList<String>) keys2.nextElement();
//                    System.out.println(frequentTermSet + ": "
//                            + ftc.finalCluster.get(frequentTermSet));
//                }
//            }
//        DocumentCollectionReader corpusReader = new DocumentCollectionReader();
//        corpusReader.parseDocumentCollection(URIS);
//
//        DocumentCollection collectionPaper = corpusReader.getParsedDocumentCollection();
//
//        collectionPaper.processDocuments();
//
//        for (String rhetStatus : Global.rhetoricalStatusList) {
//            //Print semua kalimat dengan kategoriretorik = rhetStatus dari semua dokumen
//            System.out.println("------------------------------------START OF THIS RHETORIC-----------------------------------------");
//            System.out.println("----------------------------------------" + rhetStatus.toUpperCase() + "---------------------------------------");
//            System.out.println("------------------------------------------------------------------------------------");
//            ArrayList<Document> docs = collectionPaper.getDocumentCollection();
//            for (Document doc : docs) {
//                System.out.println("From Paper '" + doc.getTitle() + "':");
//                for (int i = 0; i < doc.content.get(rhetStatus).size(); ++i) {
//                    System.out.println(doc.content.get(rhetStatus).get(i));
//                }
//                System.out.println("");
//            }
//            System.out.println("");
//
//            System.out.println("ALL CONCEPTS: ");
//            ArrayList<String> allConcepts = collectionPaper.conceptStrings.get(rhetStatus);
//            System.out.println("Concepts dari " + rhetStatus + " : " + allConcepts);
//            System.out.println("Jumlah Concepts dari " + rhetStatus + " : " + allConcepts.size());
//            for (int i = 0; i < allConcepts.size(); ++i) {
//                System.out.print(allConcepts.get(i) + "|");
//            }
//            System.out.println("");
//            System.out.println("");
//
//            /* System.out.println("WEKA FORMAT:");
//            for (int i = 0; i < allConcepts.size(); ++i) {
//            System.out.println("@attribute " + allConcepts.get(i) + " {P}");
//            } */
//
//            ArrayList<Integer> spaces = new ArrayList<Integer>();
//            System.out.println("\n");
//            for (int i = 0; i < allConcepts.size(); ++i) {
//                System.out.print(allConcepts.get(i) + "|");
//                spaces.add(allConcepts.get(i).length() - 1);
//            }
//
//            System.out.println("");
//            for (int i = 0; i < collectionPaper.documentTransactions.get(rhetStatus).size(); ++i) {
//                ArrayList<Boolean> thisTrans = collectionPaper.documentTransactions.get(rhetStatus).get(i);
//                for (int j = 0; j < thisTrans.size(); ++j) {
//                    if (thisTrans.get(j)) {
//                        for (int k = 0; k < spaces.get(j); k++) {
//                            System.out.print(" ");
//                        }
//                        System.out.print("P|");
//                    } else {
//                        for (int k = 0; k < spaces.get(j); k++) {
//                            System.out.print(" ");
//                        }
//                        System.out.print("?|");
//                    }
//                }
//                System.out.println("");
//            }
//
//
//            double minimumSupport = 0.7;
//            int minimumDocumentSupport = (int) Math.round(minimumSupport * collectionPaper.documentTransactions.get(rhetStatus).size());
//            if (minimumDocumentSupport < 2) {
//                System.out.println("Itemset harus ada di >= 2 dokumen! Naikkan minimum support");
//            } else {
//                FTC ftc = new FTC(collectionPaper.conceptStrings.get(rhetStatus), collectionPaper.documentTransactions.get(rhetStatus), minimumSupport);
//                ftc.filterCluster();
//
//                System.out.println("\n-------HASIL FILTERING----------");
//                Enumeration keys2 = ftc.finalCluster.keys();
//                /* while (keys2.hasMoreElements()) {
//                    ArrayList<String> frequentTermSet = (ArrayList<String>) keys2.nextElement();
//                    System.out.println(frequentTermSet + ": "
//                            + ftc.finalCluster.get(frequentTermSet));
//                } */
//                System.out.println("");
//                System.out.println("CLUSTER BELUM DISUMMARY:");
//                
//                while (keys2.hasMoreElements()) {
//                    ArrayList<String> frequentTermSet = (ArrayList<String>) keys2.nextElement();
//                    System.out.println(frequentTermSet + ": ");
//                    ArrayList<Integer> documentsIndex = ftc.finalCluster.get(frequentTermSet);
//                    for (int i = 0; i < documentsIndex.size(); ++i) {
//                        int docIndex = documentsIndex.get(i);
//                        System.out.println("docIndex: "+docIndex);
//                        System.out.println("From Paper '" + collectionPaper.getDocumentByID(docIndex).getTitle() + "':");
//                        for (int j = 0; j < collectionPaper.getDocumentByID(docIndex).content.get(rhetStatus).size(); ++j) {
//                            System.out.println(collectionPaper.getDocumentByID(docIndex).content.get(rhetStatus).get(j));
//                        }
//                        
//                        System.out.println("PROCESSED DOC: ");
//                        ArrayList<ArrayList<String>> tes = collectionPaper.processedDocuments.get(rhetStatus).get(docIndex);
//                        System.out.println(""+tes);
//                        System.out.println("");
//                    }
//                    
//                    SentenceSelector sum = new SentenceSelector(collectionPaper.processedDocuments.get(rhetStatus), documentsIndex, frequentTermSet);
//                    sum.summarize();
//                    System.out.println("HASIL SUMMARY: ");
//                    for (int i=0; i<sum.selectedSentence.size();++i) {
//                        int[] thisSelectedSentencePoint = sum.selectedSentence.get(i);
//                        int docID = (int) thisSelectedSentencePoint[0];
//                        int sentenceIndex = (int) thisSelectedSentencePoint[1];
//                        String sentence = collectionPaper.getDocumentByID(docID).content.get(rhetStatus).get(sentenceIndex);
//                        System.out.println(""+sentence);
//                    }
//                    System.out.println("");
//                }
//            }
//            System.out.println("\n------------------------------------END OF THIS RHETORIC-------------------------------------");
//            System.out.println("----------------------------------------------------------------------");
//            System.out.println("");
//        }
//        

        /* System.out.println("----------------------");
        System.out.println("BUAT CONFIG SI URIGENA:");
        System.out.println(""+collectionPaper.conceptStringsAIM.size());
        System.out.println(""+collectionPaper.documentTransactionsAIM.size());
        System.out.println("50");
        System.out.println("1");
        
        System.out.println("----------------------");
        System.out.println("BUAT TRANSA SI URIGENA:");
        for (int i=0; i<collectionPaper.documentTransactionsAIM.size(); ++i) {
        ArrayList<Boolean> thisDocTransact = collectionPaper.documentTransactionsAIM.get(i);
        for (int j=0; j< thisDocTransact.size(); ++j) {
        if (thisDocTransact.get(j))
        System.out.print("1 ");
        else
        System.out.print("0 ");
        }
        System.out.println("");
        } */
    }
}
