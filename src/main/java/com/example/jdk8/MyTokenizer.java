package com.example.jdk8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class MyTokenizer {

    public List<String> getTokens(String str) {
        List<String> tokens = new ArrayList<>();
        // StringTokenizer tokenizer = new StringTokenizer( str );
        StringTokenizer tokenizer = new StringTokenizer(str, ",");
        // StringTokenizer tokenizer = new StringTokenizer( str , "," , true );
        while (tokenizer.hasMoreElements()) {
            tokens.add(tokenizer.nextToken());
            // tokens.add( tokenizer.nextToken("e") );
        }
        int tokenLength = tokens.size();
        return tokens;
    }

    public List<String> getTokensWithCollection(String str) {
        return Collections.list(new StringTokenizer(str, ",")).stream().map(token -> (String) token).collect(Collectors.toList());
    }

    public List<String> getTokensFromFile(String path, String delim) {
        List<String> tokens = new ArrayList<>();
        String currLine;
        StringTokenizer tokenizer;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(MyTokenizer.class.getResourceAsStream("/" + path)))) {
            while ((currLine = br.readLine()) != null) {
                tokenizer = new StringTokenizer(currLine, delim);
                while (tokenizer.hasMoreElements()) {
                    tokens.add(tokenizer.nextToken());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokens;
    }

    public void stringBreak() {

        String[] result = "this is a test".split("\\s");
        for (int x=0; x<result.length; x++) {
            System.out.println(result[x]);
        }

        StringTokenizer st = new StringTokenizer("this is a test");
        while (st.hasMoreTokens()) {
            System.out.println(st.nextToken());
        }


    }
}