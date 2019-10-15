package com.demo;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URLEncoder;

/**
  1. URLEncoder has nothing to do with URLs
  2. The URL specification is separate from the HTML spec.
 *
 */
public class Dom4jTest {
    public static void main (String [] args) throws Exception {
        Document doc = DocumentHelper.createDocument ();
        doc.setXMLEncoding ("GBK");
        Element root = doc.addElement ("root");
        root.addText ("\"");
        System.out.println (doc.asXML ());
        System.out.println (Dom4jTest.formatXml (doc, "utf-8", false));

        // html encoder
        String q = "random word £500 bank $";
        String url = "http://example.com/query?q=" + URLEncoder.encode(q, "UTF-8");
    }


    public static String formatXml (Document document, String charset, boolean istrans) {
        OutputFormat format = OutputFormat.createPrettyPrint ();
        format.setEncoding (charset);
        StringWriter sw = new StringWriter ();
        XMLWriter xw = new XMLWriter (sw, format);
        xw.setEscapeText (istrans);
        try {
            xw.write (document) ;
            xw.flush ();
            xw.close ();
        } catch (IOException e) {
            System.out.println ( " formatting XML document exception occurs, please check ! " ) ;
            e.printStackTrace ();
        }
        return sw.toString ();
    }

}
