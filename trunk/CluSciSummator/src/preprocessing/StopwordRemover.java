/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preprocessing;

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
    private final String _stopwordFileURL = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\cluscisummator\\CluSciSummator\\stopword.txt";
    private ArrayList<String> _stopword;

    public StopwordRemover() throws FileNotFoundException, IOException {
        _stopword = new ArrayList<String>();
        FileInputStream fstream = new FileInputStream(_stopwordFileURL);
        
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        //Read File Line By Line
        while ((strLine = br.readLine()) != null) {
            _stopword.add(strLine.toLowerCase().replaceAll("[^a-z]", "")); //buang tanda baca
        }
        //Close the input stream
        in.close();
    }
    
    public String removeStopword(String input) {
        String[] inputSplit = input.split(" "); 
        StringBuffer result = new StringBuffer();
        for (int i=0; i<inputSplit.length; ++i) {
            if (!_stopword.contains(inputSplit[i])) {
               result.append(inputSplit[i]).append(" ");
            }
        }
        return result.toString();
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String tes = "a monkey is really silly creature. He about to eat ALL the bananas!";
        tes = tes.toLowerCase();
        StopwordRemover a = new StopwordRemover();
        String result = a.removeStopword(tes);
        result = Stemmer.stem(result);
        System.out.println("Result: "+result);
    }
    
}

