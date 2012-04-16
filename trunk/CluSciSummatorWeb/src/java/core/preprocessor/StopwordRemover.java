/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.preprocessor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class StopwordRemover {
    /**
     * FIELD DARI CLASS
     * 1. _stopwordFileURL = URL dari file yang berisi daftar stopword
     * 2. _conjungtionFIleURL = URL dari file yang berisi daftar konjungsi
     * 3. _stopword = daftar stopword (data internal yang diproses dari file eksternal stopword)
     * 4. _conjungtion = daftar konjungsi (data internal yang diproses dari file eksternal konjungsi)
     */
    private final String _stopwordFileURL = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\cluscisummator\\CluSciSummator\\data\\stopword.txt";
    private final String _conjunctionFileURL = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\cluscisummator\\CluSciSummator\\data\\conjunction.txt";
    private ArrayList<String> _stopword;
    private ArrayList<String> _conjunction;

    
    /**
     * KONSTRUKTOR DARI CLASS
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public StopwordRemover() throws FileNotFoundException, IOException {
        //Load stopword ke data internal
        _stopword = new ArrayList<String>();
        FileInputStream fstreamStopword = new FileInputStream(_stopwordFileURL);
        
        DataInputStream in = new DataInputStream(fstreamStopword);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        //Read File Line By Line
        while ((strLine = br.readLine()) != null) {
            //_stopword.add(strLine.toLowerCase().replaceAll("[^a-z]", "")); //buang tanda baca
            _stopword.add(strLine.toLowerCase());
        }
        //Close the input stream
        in.close();
        
        
        //Load konjungsi ke data internal
        _conjunction = new ArrayList<String>();
        FileInputStream fstreamConjunction = new FileInputStream(_conjunctionFileURL);
        DataInputStream inConjunction = new DataInputStream(fstreamConjunction);
        BufferedReader brConjunction = new BufferedReader(new InputStreamReader(inConjunction));
        String strLineConjunction;
        //Read File Line By Line
        while ((strLineConjunction = brConjunction.readLine()) != null) {
            _conjunction.add(strLineConjunction.toLowerCase());
        }
        //Close the input stream
        inConjunction.close();
    }
    
    
    /*
     * METHOD DARI CLASS
     * 1. removeStopword(String)
     */
    
    /**
     * Method removeStopword: membuang stopword dari sebuah kalimat
     * @param input
     * @return 
     */
    public String removeStopword(String input) {
        String[] inputSplit = input.split(" "); 
        StringBuffer result = new StringBuffer();
        for (int i=0; i<inputSplit.length; ++i) {
            if (!_stopword.contains(inputSplit[i]) && !_conjunction.contains(inputSplit[i])) {
               result.append(inputSplit[i]).append(" ");
            }
        }
        return result.toString();
    }
    
    /**
     * Method Tester
     * @param args
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String tes = "a monkey is really silly creature. He about to eat ALL the bananas!";
        tes = tes.toLowerCase();
        StopwordRemover a = new StopwordRemover();
        String result = a.removeStopword(tes);
        result = Stemmer.stem(result);
        System.out.println("Result: "+result);
    }   
}