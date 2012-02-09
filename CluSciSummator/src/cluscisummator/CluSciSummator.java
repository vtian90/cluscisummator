/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cluscisummator;

import data.Document;
import preprocessing.Parser;

/**
 *
 * @author ACER 4741
 */
public class CluSciSummator {

    /**
     * @param args the command line argumentsd
     */
    public static void main(String[] args) {
       String uri = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\Dataset PAPER\\final200511\\A92-1024(1).xml";
       Parser parser = new Parser();
       parser.parseDocument(uri);
       Document parsedDoc = parser.getParsedDocument();
       System.out.println(parsedDoc.toString());
    }
}
