/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preprocessing;

import data.Document;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import utility.Global;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class XMLParser {
    //Field Dokumen hasil parsing
    private Document _parsedDocument;

    /*
     * Konstruktor
     */
    public XMLParser() {
        _parsedDocument = new Document();
    }

    /**
     * Method parseDocument : Melakukan parsing satu dokumen
     * @param URI
     * @param docID 
     */
    public void parseDocument(String URI, int docID) {
        _parsedDocument.setID(docID);
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {

                StringBuffer textBuffer = new StringBuffer();
                ArrayList<String> authors = new ArrayList<String>();
                
                ArrayList<ArrayList<String>> contentContainer = new ArrayList<ArrayList<String>>();
                
                ArrayList<String> AIM = new ArrayList<String>();
                ArrayList<String> NOV_ADV = new ArrayList<String>();
                ArrayList<String> CO_GRO = new ArrayList<String>();
                ArrayList<String> OTHR = new ArrayList<String>();
                ArrayList<String> PREV_OWN = new ArrayList<String>();
                ArrayList<String> OWN_MTHD = new ArrayList<String>();
                ArrayList<String> OWN_FAIL = new ArrayList<String>();
                ArrayList<String> OWN_RES = new ArrayList<String>();
                ArrayList<String> OWN_CONC = new ArrayList<String>();
                ArrayList<String> CODI = new ArrayList<String>();
                ArrayList<String> GAP_WEAK = new ArrayList<String>();
                ArrayList<String> ANTISUPP = new ArrayList<String>();
                ArrayList<String> SUPPORT = new ArrayList<String>();
                ArrayList<String> USE = new ArrayList<String>();
                ArrayList<String> FUT = new ArrayList<String>();
                
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    textBuffer.setLength(0);
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.equalsIgnoreCase(Global.listTag.TAG_TITLE.toString())) {
                        String temp = textBuffer.toString().trim();
                        temp = temp.replaceAll("\\r\\n|\\r|\\n", " ");
                        _parsedDocument.setTitle(temp);
                    } else if (qName.equalsIgnoreCase(Global.listTag.TAG_AUTHOR.toString())) {
                        authors.add(textBuffer.toString().trim());
                    } else if (qName.equalsIgnoreCase(Global.listTag.TAG_PAPER.toString())) {
                        _parsedDocument.setAuthors(authors);
                        for (String rhetoricStatus : Global.rhetoricalStatusList) {
                            _parsedDocument.content.put(rhetoricStatus, AIM);
                        }
                        _parsedDocument.content.put(Global.listTag.TAG_AIM.toString(), AIM);
                        _parsedDocument.content.put(Global.listTag.TAG_ANTISUPP.toString(), ANTISUPP);
                        _parsedDocument.content.put(Global.listTag.TAG_CODI.toString(), CODI);
                        _parsedDocument.content.put(Global.listTag.TAG_CO_GRO.toString(), CO_GRO);
                        _parsedDocument.content.put(Global.listTag.TAG_FUT.toString(), FUT);
                        _parsedDocument.content.put(Global.listTag.TAG_GAP_WEAK.toString(), GAP_WEAK);
                        _parsedDocument.content.put(Global.listTag.TAG_NOV_ADV.toString(), NOV_ADV);
                        _parsedDocument.content.put(Global.listTag.TAG_OTHR.toString(), OTHR);
                        _parsedDocument.content.put(Global.listTag.TAG_OWN_CONC.toString(), OWN_CONC);
                        _parsedDocument.content.put(Global.listTag.TAG_OWN_FAIL.toString(), OWN_FAIL);
                        _parsedDocument.content.put(Global.listTag.TAG_OWN_MTHD.toString(), OWN_MTHD);
                        _parsedDocument.content.put(Global.listTag.TAG_OWN_RES.toString(), OWN_RES);
                        _parsedDocument.content.put(Global.listTag.TAG_PREV_OWN.toString(), PREV_OWN);
                        _parsedDocument.content.put(Global.listTag.TAG_SUPPORT.toString(), SUPPORT);
                        _parsedDocument.content.put(Global.listTag.TAG_USE.toString(), USE);
                    } else {
                        String temp = textBuffer.toString().trim();
                        temp = temp.replaceAll("\\r\\n|\\r|\\n", " ");
                        
                        int indexRhetoric = Global.rhetoricalStatusList.indexOf(qName.toLowerCase());
                        if (indexRhetoric != -1) {
                            
                        }
                        
                        if (qName.equalsIgnoreCase(Global.listTag.TAG_AIM.toString())) {
                            ArrayList<String> temp = contentContainer.get(index);
                            contentContainer.add(0, AIM.add(temp));
                            AIM.add(temp);
                        } else if (qName.equalsIgnoreCase(Global.listTag.TAG_ANTISUPP.toString())) {
                            ANTISUPP.add(temp);
                        } else if (qName.equalsIgnoreCase(Global.listTag.TAG_CODI.toString())) {
                            CODI.add(temp);
                        } else if (qName.equalsIgnoreCase(Global.listTag.TAG_CO_GRO.toString())) {
                            CO_GRO.add(temp);
                        } else if (qName.equalsIgnoreCase(Global.listTag.TAG_FUT.toString())) {
                            FUT.add(temp);
                        } else if (qName.equalsIgnoreCase(Global.listTag.TAG_GAP_WEAK.toString())) {
                            GAP_WEAK.add(temp);
                        } else if (qName.equalsIgnoreCase(Global.listTag.TAG_NOV_ADV.toString())) {
                            NOV_ADV.add(temp);
                        } else if (qName.equalsIgnoreCase(Global.listTag.TAG_OTHR.toString())) {
                            OTHR.add(temp);
                        } else if (qName.equalsIgnoreCase(Global.listTag.TAG_OWN_CONC.toString())) {
                            OWN_CONC.add(temp);
                        } else if (qName.equalsIgnoreCase(Global.listTag.TAG_OWN_FAIL.toString())) {
                            OWN_FAIL.add(temp);
                        } else if (qName.equalsIgnoreCase(Global.listTag.TAG_OWN_MTHD.toString())) {
                            OWN_MTHD.add(temp);
                        } else if (qName.equalsIgnoreCase(Global.listTag.TAG_OWN_RES.toString())) {
                            OWN_RES.add(temp);
                        } else if (qName.equalsIgnoreCase(Global.listTag.TAG_PREV_OWN.toString())) {
                            PREV_OWN.add(temp);
                        } else if (qName.equalsIgnoreCase(Global.listTag.TAG_SUPPORT.toString())) {
                            SUPPORT.add(temp);
                        } else if (qName.equalsIgnoreCase(Global.listTag.TAG_USE.toString())) {
                            USE.add(temp);
                        }
                    }
                }

                @Override
                public void characters(char ch[], int start, int length) throws SAXException {
                    textBuffer.append(ch, start, length);
                }
            };
            saxParser.parse(URI, handler);
        } catch (Exception e) {
        }
    }

    /*
     * Public access dari dokumen hasil parsing
     */
    public Document getParsedDocument() {
        return _parsedDocument;
    }
}
