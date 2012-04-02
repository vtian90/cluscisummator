/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import java.util.ArrayList;
import java.util.Enumeration;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class SentenceSelector {

    public SentenceSelector() {
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
        System.out.println("VSM 1:" +sentence1_VSM.VSM);
        System.out.println("VSM 2:" +sentence2_VSM.VSM);
        
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
    public static void main(String[] args){
        ArrayList<String> tesString = new ArrayList<String>();
        tesString.add("a"); tesString.add("boy"); tesString.add("is"); tesString.add("crying"); tesString.add("out"); tesString.add("loud"); tesString.add("in"); tesString.add("a"); tesString.add("room"); tesString.add("a"); tesString.add("boy");
        
        ArrayList<String> tesString2 = new ArrayList<String>();
        tesString2.add("a"); tesString2.add("boy"); tesString2.add("is"); tesString2.add("crying"); tesString2.add("out"); tesString2.add("loud"); tesString2.add("in"); tesString2.add("a"); tesString2.add("room"); tesString2.add("garut"); tesString2.add("dodol");
        
        SentenceSelector a = new SentenceSelector();
        double similarity = a.cosineSimilarity(tesString, tesString2);
        System.out.println("String 1: "+tesString);
        System.out.println("String 2: "+tesString2);
        
        System.out.println("SIM: "+similarity);
    }
}
