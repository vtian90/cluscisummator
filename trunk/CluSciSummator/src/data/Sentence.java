/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author ACER 4741
 */
public class Sentence {
    private String _content;
    private String _tag;

    public Sentence(String _content, String _tag) {
        this._content = _content;
        this._tag = _tag;
    }

    public String getContent() {
        return _content;
    }

    public String getTag() {
        return _tag;
    }

    @Override
    public String toString() {
        return "Sentence{" + "\n\t_content=" + _content + ", \n\t_tag=" + _tag +"\n"+ '}';
    }     
}
