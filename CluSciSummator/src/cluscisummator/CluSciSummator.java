/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cluscisummator;

import data.DocumentCollection;
import data.Document;
import data.Sentence;
import java.util.ArrayList;
import preprocessing.CorpusReader;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class CluSciSummator {

    /**
     * @param args the command line argumentsd
     */
    public static void main(String[] args) {    
       String[] _rhetoricalStatusList = {
        "aim", "nov_adv", "co_gro", "othr", "prev_own", "own_mthd", "own_fail", "own_res", "own_conc", "codi", "gap_weak", "antisupp", "support", "use", "fut"
       };
       
       String URI1 = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\Dataset PAPER\\final200511\\A92-1024(1).xml";
       String URI2 = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\Dataset PAPER\\final200511\\A97-1049_FINAL_1.xml";
       ArrayList<String> URIS = new ArrayList<String>();
       URIS.add(URI1);
       URIS.add(URI2);
       
       CorpusReader corpusReader = new CorpusReader();
       corpusReader.parseDocumentCollection(URIS);
       
       DocumentCollection collectionPaper = corpusReader.getParsedDocumentCollection();
       
       for (String rhetStatus : _rhetoricalStatusList) {
           ArrayList<Sentence> content = collectionPaper.getContentByRhetoricalStatus(rhetStatus);
           System.out.println(rhetStatus.toUpperCase()+":");
           for (Sentence senten : content) {
               Document doc = collectionPaper.getDocumentByID(senten.getDocID());
               System.out.println("From Paper '"+doc.getTitle()+"':");
               System.out.println(senten.getContent());
               System.out.println("");
           }
           System.out.println("");
       }
    }
}
