package com.demo.xml.sax;

/**
 * Created by tbu on 11/3/2016.
 */

import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;


/**
 *

 java -Dorg.xml.sax.driver=org.apache.xerces.parsers.SAXParser SAXChecker http://www.cafeconleche.org/
 java -Dorg.xml.sax.driver=org.apache.crimson.parser.XMLReaderImpl
 java -Dorg.xml.sax.driver=org.apache.xerces.parsers.SAXParser


 Don’t forget that you’ll probably need to install a parser such as Xerces or Elfred
 somewhere in your class path before you can compile or run this program.
 Only Java 1.4 and later include a built-in parser.

 Bundling an XML parser has not been necessary since 1.4 when JAXP was added to the JRE. You should use JAXP and not directly call Xerces.

 Internally, the JRE bundles and uses Xerces anyways (with a "com.sun" prefix).The parser in the JDK was a fork of Xerces, but it is very buggy.

 The parser in the JDK was a fork of Xerces, but it is very buggy.
 I would recommend production applications always to use the Apache version of the parser in preference.
 The bugs are rare, but they are unpredictable, and they don't only affect corner cases that aren't seen in real life;
 I've seen many cases where quite boring XML documents are being parsed, and corrupt data is passed to the application for attribute values.
 Sun/Oracle have shown no interest in fixing the problem. Use Apache Xerces every time.

 in Xerces   it’s org.apache.xerces.parsers.SAXParser.
 In Crimson  it’s org.apache.crimson.parser.XMLReaderImpl    ( retired 2010: http://attic.apache.org/projects/crimson.html)


 The Java Platform, Standared Edition 1.4 (Java SE 1.4) included the Crimson reference implementation for JAXP 1.1.
 The Java platform, Standard Edition 6 (Java SE 6) includes a reference implementation for JAXP 1.4 based on the Apache Xerces library.
 JAXP 1.6.0 is released and can be downloaded in the download area. Please see Release Notes for more details. JAXP 1.6.0 is in Java SE 8.0
 Because these implementations come from entirely different codebases, and because the JAXP standard has evolved from 1.1 to 1.4,
 there are some subtle differences between the implementations, even they both conform to the JAXP standard.


 JAXP with newer versions extending older versions to preserve backward compatibility
 JAXP (Java API for XML Processing) is a standard component in the Java platform.
 An implementation of the JAXP 1.4 is included in Java SE 6.0 and OpenJDK7,
 JAXP 1.6 is now in Java SE 8.0.
 JAXP 1.4 is a maintenance release of JAXP 1.3 with support for the Streaming API for XML (StAX),
 JAXP 1.5 is a maintenance release of JAXP 1.4 with new security related properties,
 JAXP 1.5 in OpenJDK7 update 40 as well as in Java SE 8.0, and
 JAXP 1.6 is part of prepare for modularization.

 ####################
 JAXP 1.6 will be the final standalone release. All future revisions to the JAXP API will be part of the Java SE specification, (https://jaxp.java.net/1.6/1.6.0/ReleaseNotes.html)
 starting with Java SE 9. Future JAXP development will be done solely within the OpenJDK community.
#####################
 *
 */
public class SAXChecker {

    public static void main(String[] args) {
        System.setProperty("jaxp.debug","1");
        if (args.length <= 0) {
            System.out.println("Usage: java SAXChecker URL");
            return;
        }
        try {
            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.parse(args[0]);
            System.out.println(System.getProperty("org.xml.sax.driver"));
            System.out.println(System.getProperty("javax.xml.parsers.DocumentBuilderFactory"));
            System.out.println(args[0] + " is well-formed.");
        } catch (SAXException e) {
            System.out.println(args[0] + " is not well-formed.");
        } catch (IOException e) {
            System.out.println(
                    "Due to an IOException, the parser could not check "
                            + args[0]
            );
        }
    }
}
