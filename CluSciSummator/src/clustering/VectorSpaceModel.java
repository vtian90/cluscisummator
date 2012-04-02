/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import java.util.ArrayList;
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
     * Membentuk vectorSpaceModel dari sebuah kalimat
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
