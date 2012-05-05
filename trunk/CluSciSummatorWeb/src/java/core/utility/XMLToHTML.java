/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.utility;

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author Akbar
 */
public class XMLToHTML {
    private StringWriter _htmlResult;
    private final String _XLSTransformer = "D:/Kuliah/Semester VIII/TA2/Implementasi/cluscisummator/CluSciSummatorWeb/src/java/core/utility/paper.xsl";
    private String _XMLURI;
    
    public XMLToHTML(String XMLURI){
        this._XMLURI = XMLURI;
    }
    
    public void transformXMLToHTML() throws TransformerConfigurationException, TransformerException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        
        StringWriter stringWriter = new StringWriter();
        StreamResult result = new StreamResult(stringWriter);

        Transformer transformer = tFactory.newTransformer(new StreamSource(this._XLSTransformer));
        transformer.transform(new StreamSource(this._XMLURI), result);
        
        this._htmlResult = stringWriter;
    }
    
    public StringWriter getHTMLResult(){
        return _htmlResult;
    }
    
    public static void main(String[] args){
        String XMLURI = "D:/Kuliah/Semester VIII/TA2/Implementasi/Dataset PAPER/pengujian 1/tes.xml";
        XMLToHTML transformer = new XMLToHTML(XMLURI);
        try {
            transformer.transformXMLToHTML();
            System.out.println(""+transformer.getHTMLResult());
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(XMLToHTML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(XMLToHTML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
