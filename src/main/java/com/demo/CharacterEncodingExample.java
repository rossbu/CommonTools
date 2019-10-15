package com.demo;

import java.io.*;
import java.nio.charset.Charset;

public class CharacterEncodingExample {

    public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        String defaultCharacterEncoding = System.getProperty("file.encoding");
        System.out.println("defaultCharacterEncoding by property: " + defaultCharacterEncoding);
        System.out.println("defaultCharacterEncoding by code: " + getDefaultCharEncoding());
        System.out.println("defaultCharacterEncoding by charSet: " + Charset.defaultCharset());

        System.setProperty("file.encoding", "UTF-16");

        System.out.println("defaultCharacterEncoding by property after updating file.encoding : " + System.getProperty("file.encoding"));

        System.out.println("defaultCharacterEncoding by code after updating file.encoding : " + getDefaultCharEncoding());

        System.out.println("defaultCharacterEncoding by java.nio.Charset after updating file.encoding : " + Charset.defaultCharset());

    }

    public static String getDefaultCharEncoding(){
        byte [] bArray = {'w'};
        InputStream is = new ByteArrayInputStream(bArray);
        InputStreamReader reader = new InputStreamReader(is);
        String defaultCharacterEncoding = reader.getEncoding();
        return defaultCharacterEncoding;
    }
}