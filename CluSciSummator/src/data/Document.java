/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.ArrayList;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class Document {
    private int _ID;
    private ArrayList<String> _authors;
    private String _title;
    
    public ArrayList<String> AIM;
    public ArrayList<String> NOV_ADV;
    public ArrayList<String> CO_GRO;
    public ArrayList<String> OTHR;
    public ArrayList<String> PREV_OWN;
    public ArrayList<String> OWN_MTHD;
    public ArrayList<String> OWN_FAIL;
    public ArrayList<String> OWN_RES;
    public ArrayList<String> OWN_CONC;
    public ArrayList<String> CODI;
    public ArrayList<String> GAP_WEAK;
    public ArrayList<String> ANTISUPP;
    public ArrayList<String> SUPPORT;
    public ArrayList<String> USE;
    public ArrayList<String> FUT;
    
    
    public Document() {
        AIM = new ArrayList<String>();
        NOV_ADV = new ArrayList<String>();
        CO_GRO = new ArrayList<String>();
        OTHR = new ArrayList<String>();
        PREV_OWN = new ArrayList<String>();
        OWN_MTHD = new ArrayList<String>();
        OWN_FAIL = new ArrayList<String>();
        OWN_RES = new ArrayList<String>();
        OWN_CONC = new ArrayList<String>();
        CODI = new ArrayList<String>();
        GAP_WEAK = new ArrayList<String>();
        ANTISUPP = new ArrayList<String>();
        SUPPORT = new ArrayList<String>();
        USE = new ArrayList<String>();
        FUT = new ArrayList<String>();
        
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
