/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cluscisummator;

import clustering.FTC;
import data.DocumentCollection;
import data.Document;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import preprocessing.DocumentCollectionReader;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class CluSciSummator {

    /**
     * @param args the command line argumentsd
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
        String[] _rhetoricalStatusList = {
            "aim"//, "nov_adv", "co_gro", "othr", "prev_own", "own_mthd", "own_fail", "own_res", "own_conc", "codi", "gap_weak", "antisupp", "support", "use", "fut"
        };

        String URI1 = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\Dataset PAPER\\final200511\\A92-1024(1).xml";
        String URI2 = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\Dataset PAPER\\final200511\\A97-1049_FINAL_1.xml";
        String URI3 = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\Dataset PAPER\\final200511\\C02-1144_FINAL_2.xml";
        String URI4 = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\Dataset PAPER\\final200511\\A97-1053(1).xml";
        ArrayList<String> URIS = new ArrayList<String>();
        URIS.add(URI1);
        URIS.add(URI2);
        URIS.add(URI3);
        URIS.add(URI4);

        DocumentCollectionReader corpusReader = new DocumentCollectionReader();
        corpusReader.parseDocumentCollection(URIS);

        DocumentCollection collectionPaper = corpusReader.getParsedDocumentCollection();


        //Print AIM dari semua dokumen
        System.out.println("AIM:");
        ArrayList<Document> docs = collectionPaper.getDocumentCollection();
        for (Document doc : docs) {
            System.out.println("From Paper '" + doc.getTitle() + "':");
            for (int i = 0; i < doc.AIM.size(); ++i) {
                System.out.println(doc.AIM.get(i));
            }
            System.out.println("");
        }
        System.out.println("");

        collectionPaper.transactDocumentAIM();
        System.out.println("ALL CONCEPTS: ");
        for (String rhetStatus : _rhetoricalStatusList) {
            ArrayList<String> allConcepts = collectionPaper.conceptStringsAIM;
            for (int i = 0; i < allConcepts.size(); ++i) {
                System.out.print(allConcepts.get(i) + "|");
            }
        }
        System.out.println("");
        for (String rhetStatus : _rhetoricalStatusList) {
            ArrayList<String> allConcepts = collectionPaper.conceptStringsAIM;
            for (int i = 0; i < allConcepts.size(); ++i) {
                System.out.println("@attribute " + allConcepts.get(i) + " {P}");
            }
        }

        ArrayList<Integer> spaces = new ArrayList<Integer>();
        System.out.println("\n");
        for (String rhetStatus : _rhetoricalStatusList) {
            ArrayList<String> allConcepts = collectionPaper.conceptStringsAIM;
            for (int i = 0; i < allConcepts.size(); ++i) {
                System.out.print(allConcepts.get(i) + "|");
                spaces.add(allConcepts.get(i).length() - 1);
            }
        }
        System.out.println("");
        for (int i = 0; i < collectionPaper.documentTransactionsAIM.size(); ++i) {
            ArrayList<Boolean> thisTrans = collectionPaper.documentTransactionsAIM.get(i);
            for (int j = 0; j < thisTrans.size(); ++j) {
                if (thisTrans.get(j)) {
                    for (int k = 0; k < spaces.get(j); k++) {
                        System.out.print(" ");
                    }
                    System.out.print("P|");
                } else {
                    for (int k = 0; k < spaces.get(j); k++) {
                        System.out.print(" ");
                    }
                    System.out.print("?|");
                }
            }
            System.out.println("");
        }

        double minimumSupport = 0.5;
        int minimumDocumentSupport = (int) Math.round(minimumSupport * collectionPaper.documentTransactionsAIM.size());
        if (minimumDocumentSupport < 2) {
            System.out.println("Itemset harus ada di >= 2 dokumen! Naikkan minimum support");
        } else {
            FTC frequentSearcher = new FTC(collectionPaper.conceptStringsAIM, collectionPaper.documentTransactionsAIM, minimumSupport);
            ArrayList<ArrayList<String>> result = frequentSearcher.searchFrequentTermSet();

            for (int i = 0; i < result.size(); ++i) {
                ArrayList<String> frequentItemSet = result.get(i);
                System.out.println("" + frequentItemSet);
            }
            System.out.println("BATAS");
            Hashtable<ArrayList<String>, ArrayList<Integer>> ht = frequentSearcher.clusterFrequentTermSet();
            Enumeration keys = ht.keys();
            while (keys.hasMoreElements()) {
                ArrayList<String> frequentTermSet = (ArrayList<String>) keys.nextElement();
                System.out.println(frequentTermSet + ": "
                        + ht.get(frequentTermSet));
            }
            System.out.println("BATAS 2");
            Hashtable<ArrayList<String>, ArrayList<Integer>> ht2 = frequentSearcher.filterCluster();
            Enumeration keys2 = ht2.keys();
            while (keys2.hasMoreElements()) {
                ArrayList<String> frequentTermSet = (ArrayList<String>) keys2.nextElement();
                System.out.println(frequentTermSet + ": "
                        + ht.get(frequentTermSet));
            }
        }

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
