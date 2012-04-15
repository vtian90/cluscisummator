/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class Document {
    //Metadata dokumen: ID, authors, title
    private int _ID;
    private ArrayList<String> _authors;
    private String _title;
    
    //Field retorik dokumen
    //Format : content(rhetoricalStatus, semua kalimat dari dokumen yang memiliki tag kategori retorik = rhetoricalStatus)
    public Hashtable<String, ArrayList<String>> content;
    
    /*
     * Konstruktor
     */
    public Document() {
        content = new Hashtable<String, ArrayList<String>> ();
    }
            
    public int getID() {
        return _ID;
    }

    public ArrayList<String> getAuthors() {
        return _authors;
    }

    public String getTitle() {
        return _title;
    }

    public void setID(int _ID) {
        this._ID = _ID;
    }

    public void setAuthors(ArrayList<String> _author) {
        this._authors = _author;
    }

    public void setTitle(String _title) {
        this._title = _title;
    }     
}
