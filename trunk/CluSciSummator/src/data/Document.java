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
    private ArrayList<Sentence> _content;

    public Document() {
    }

    public int getID() {
        return _ID;
    }

    public ArrayList<String> getAuthors() {
        return _authors;
    }

    public ArrayList<Sentence> getContent() {
        return _content;
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

    public void setContent(ArrayList<Sentence> _content) {
        this._content = _content;
    }

    public void setTitle(String _title) {
        this._title = _title;
    }

            
    @Override
    public String toString() {
        return "Document{" + "\n\t_ID=" + _ID + ", \n\t_authors=" + _authors + ", \n\t_title=" + _title + ", \n\t_content=" + _content + "\n"+ '}';
    }
}
