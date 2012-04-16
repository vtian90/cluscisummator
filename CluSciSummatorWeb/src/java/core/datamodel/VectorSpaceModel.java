/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.datamodel;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class VectorSpaceModel {
    //Field vectorSpaceModel yang dihasilkan dari konstruktor
    public Hashtable<String, Integer> VSM;

    /*
     * Konstruktor
     * 
     * Membentuk VectorSpaceModel dari sebuah kalimat
     * cth: 
     * input: a boy is crying out loud in a room a boy
     * output: hashmap dengan isi: 
     * ("a", 3),   ("boy", 2),
     * ("is", 1),  ("crying", 1),
     * ("out", 1), ("loud", 1),
     * ("in", 1),  ("room", 1)
     */
    public VectorSpaceModel(ArrayList<String> sentence) {
        VSM = new Hashtable<String, Integer>();
        for (int i = 0; i < sentence.size(); ++i) {
            String term = sentence.get(i);
            Integer value = VSM.get(term);
            
            if (value==null)
                VSM.put(term, 1);
            else
                VSM.put(term, value+1);   
        }
    }
    
    /**
     * Method cosineSimilarity: Menghitung similarity antara VSM ini dengan VSM lain
     * 
     * @param otherVSM
     * @return double
     */
    public double cosineSimilarity(VectorSpaceModel otherVSM) {
        double result = 0;

        //Hitung euclid VSM ini
        Enumeration allTermSentence1_euclid = this.VSM.keys();
        double euclidSentence1 = 0;
        while (allTermSentence1_euclid.hasMoreElements()) {
            String a_term = (String) allTermSentence1_euclid.nextElement();
            Integer tfThisTerm = this.VSM.get(a_term);
            euclidSentence1 += (tfThisTerm) * (tfThisTerm);
        }
        euclidSentence1 = Math.sqrt(euclidSentence1);

        //Hitung euclid sentence 2:
        Enumeration allTermSentence2_euclid = otherVSM.VSM.keys();
        double euclidSentence2 = 0;
        while (allTermSentence2_euclid.hasMoreElements()) {
            String a_term = (String) allTermSentence2_euclid.nextElement();
            Integer tfThisTerm = otherVSM.VSM.get(a_term);
            euclidSentence2 += (tfThisTerm) * (tfThisTerm);
        }
        euclidSentence2 = Math.sqrt(euclidSentence2);

        Enumeration allTermSentence1 = this.VSM.keys();
        while (allTermSentence1.hasMoreElements()) {
            String a_term = (String) allTermSentence1.nextElement();
            Integer tfInSentence1 = this.VSM.get(a_term);
            Integer tfInSentence2 = 0;
            if (otherVSM.VSM.containsKey(a_term)) {
                tfInSentence2 = otherVSM.VSM.get(a_term);
            }

            result += tfInSentence1 * tfInSentence2;
        }
        
        if (euclidSentence1 != 0 && euclidSentence2 != 0) 
            result = result / (euclidSentence1 * euclidSentence2);
        return result;
    }
    
    /*
     * Method Tester
     */
    public static void main(String[] args){
        ArrayList<String> tesString = new ArrayList<String>();
        tesString.add("a"); tesString.add("boy"); tesString.add("is"); tesString.add("crying"); tesString.add("out"); tesString.add("loud"); tesString.add("in"); tesString.add("a"); tesString.add("room"); tesString.add("a"); tesString.add("boy");
        
        VectorSpaceModel tes = new VectorSpaceModel(tesString);
        System.out.println("VSM: "+tes.VSM);
    }
}
