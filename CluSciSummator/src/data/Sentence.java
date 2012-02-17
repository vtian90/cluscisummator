/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class Sentence {
    private String _content;
    private String _tag;
    private int _docID;

    public Sentence(String _content, String _tag) {
        this._content = _content;
        this._tag = _tag;
    }

    public Sentence(String _content, String _tag, int _docID) {
        this._content = _content;
        this._tag = _tag;
        this._docID = _docID;
    }

    
    public String getContent() {
        return _content;
    }

    public String getTag() {
        return _tag;
    }

    public int getDocID() {
        return _docID;
    }
    
    @Override
    public String toString() {
        return "Sentence{" + "\n\t_content=" + _content + ", \n\t_tag=" + _tag +"\n"+ '}';
    }     
}
