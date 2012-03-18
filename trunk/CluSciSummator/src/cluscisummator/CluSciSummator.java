/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cluscisummator;

import clustering.FrequentTermSetSearcher;
import data.DocumentCollection;
import data.Document;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
       ArrayList<String> URIS = new ArrayList<String>();
       URIS.add(URI1);
       URIS.add(URI2);
       
       DocumentCollectionReader corpusReader = new DocumentCollectionReader();
       corpusReader.parseDocumentCollection(URIS);
       
       DocumentCollection collectionPaper = corpusReader.getParsedDocumentCollection();
       
       
       //Print AIM dari semua dokumen
       System.out.println("AIM:");
       ArrayList<Document> docs = collectionPaper.getDocumentCollection();
       for (Document doc: docs) {
           System.out.println("From Paper '"+doc.getTitle()+"':");
           for (int i=0; i<doc.AIM.size(); ++i) {
               System.out.println(doc.AIM.get(i));
           }
           System.out.println("");
       }
       System.out.println("");

       collectionPaper.transactDocumentAIM(); 
       System.out.println("ALL CONCEPTS: ");
       for (String rhetStatus : _rhetoricalStatusList) {
           ArrayList<String> allConcepts = collectionPaper.conceptStringsAIM;
           for (int i=0; i<allConcepts.size(); ++i) {
               System.out.print(allConcepts.get(i)+"|");
           }
       }
       
       for (String rhetStatus : _rhetoricalStatusList) {
           ArrayList<String> allConcepts = collectionPaper.conceptStringsAIM;
           for (int i=0; i<allConcepts.size(); ++i) {
               System.out.println("@attribute "+allConcepts.get(i)+" {P}");
           }
       }    
       
       
       System.out.println("\n");
       System.out.println("Document Transactions: ");
       for (int i=0; i<collectionPaper.documentTransactionsAIM.size(); ++i){
           ArrayList<Boolean> thisTrans = collectionPaper.documentTransactionsAIM.get(i);
           for (int j=0; j<thisTrans.size();++j) {
               if (thisTrans.get(j)) {
                   System.out.print("P,");
               } else {
                   System.out.print("?,");
               }
           }
           System.out.println("");
       }
       
       
       FrequentTermSetSearcher frequentSearcher = new FrequentTermSetSearcher(collectionPaper.conceptStringsAIM, collectionPaper.documentTransactionsAIM);
       ArrayList<ArrayList<String>> result = frequentSearcher.searchFrequentTermSet();
        
        for (int i = 0; i < result.size(); ++i) {
            ArrayList<String> frequentItemSet = result.get(i);
            System.out.print("[");
            for (int j = 0; j < frequentItemSet.size(); ++j) {
                System.out.print(frequentItemSet.get(j));
                if (j!=frequentItemSet.size()-1)
                    System.out.print(",");
            }
            System.out.println("]");
        }
        
        System.out.println("----------------------");
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
        }
    }
}
