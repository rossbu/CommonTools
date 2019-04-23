package com.example.jdk.ext.comparator;

/**
 * Created by tbu on 6/25/2014.
 */

import java.util.Collections;
import java.util.Vector;

public class CompareToken implements Comparable<CompareToken> {

    int valN;
    String valS;
    String repr;

    public String toString() {
        return repr;
    }

    public CompareToken(String s) {
        int l = 0;
        char data[] = new char[s.length()];
        repr = s;
        valN = 0;
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                valN = valN * 10 + (c - '0');
            } else {
                data[l++] = c;
            }
        }

        valS = new String(data, 0, l);
    }

    public int compareTo(CompareToken b) {
        int r = valS.compareTo(b.valS);
        if (r != 0) {
            return r;
        }
        return valN - b.valN;
    }

    public static void main(String[] args) {
        String[] strings = {
                "aaa",
                "bbb3ccc",
                "bbb12ccc",
                "ccc 11",
                "ddd",
                "eee3dddjpeg2000eee",
                "eee12dddjpeg2000eee",
                "123"
        };

        Vector<CompareToken> data = new Vector<CompareToken>();
        for (String s : strings) {
            data.add(new CompareToken(s));
        }
        Collections.shuffle(data);

        Collections.sort(data);
        for (CompareToken c : data) {
            System.out.println("" + c);
        }
    }
}
