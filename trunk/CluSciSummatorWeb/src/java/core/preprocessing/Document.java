/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.preprocessing;

import core.utility.Global;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class Document {
    /**
     * FIELD DARI CLASS
     * 1. _ID: ID dokumen
     * 2. _authors: daftar author dokumen
     * 3. _title: Judul makalah
     * 4. _fileName: nama file
     * 5. content: Field retorik dokumen. Format : content(rhetoricalStatus, semua kalimat dari dokumen yang memiliki tag kategori retorik = rhetoricalStatus)
     */
    private int _ID;
    private ArrayList<String> _authors;
    private String _title;
    private String _fileName;
    public Hashtable<String, ArrayList<String>> content;
    
    
    /**
     * KONSTRUKTOR DARI CLASS
     */
    public Document() {
        content = new Hashtable<String, ArrayList<String>> ();
        for (String rhet : Global.rhetoricalStatusList) {
            content.put(rhet, new ArrayList<String>());
        }
    }
    
    /**
     * METHOD DARI CLASS
     * 1. getID(): mengembalikan ID makalah
     * 2. getAuthors(): mengembalikan daftar author makalah
     * 3. getTitle(): mengembalikan judul makalah
     * 4. setID(): mengeset ID makalah
     * 5. setAuthors(): mengeset daftar author makalah
     * 6. setTitle(): mengeset judul makalah
     */
    
    /**
     * Method getID: mengembalikan ID makalah
     * @return int ID
     */        
    public int getID() {
        return _ID;
    }

    
    /**
     * Method getAuthors: mengembalikan daftar author makalah
     * @return ArrayList<String>
     */
    public ArrayList<String> getAuthors() {
        return _authors;
    }

    /**
     * Method getTitle: mengembalikan judul makalah
     * @return String
     */
    public String getTitle() {
        return _title;
    }
    
    /**
     * Method getFileName: mengembalikan nama file makalah
     * @return String
     */
    public String getFilename() {
        return _fileName;
    }

    /**
     * Method setID: mengeset ID makalah
     * @param _ID 
     */
    public void setID(int _ID) {
        this._ID = _ID;
    }

    /**
     * Method setAuthors: mengeset daftar author makalah
     * @param _author 
     */
    public void setAuthors(ArrayList<String> _author) {
        this._authors = _author;
    }

    /**
     * Method setTitle(): mengeset judul makalah
     * @param _title 
     */
    public void setTitle(String _title) {
        this._title = _title;
    }     
    
    /**
     * Method setFilename(): mengeset filename makalah
     * @param _title 
     */
    public void setFilename(String _filename) {
        this._fileName = _filename;
    }   
}
