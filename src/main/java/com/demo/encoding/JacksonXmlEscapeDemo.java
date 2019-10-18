package com.demo.encoding;


import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;

// First, definition of what to escape
public class JacksonXmlEscapeDemo extends CharacterEscapes {
    private final int[] asciiEscapes;

    public JacksonXmlEscapeDemo() {
        // start with set of characters known to require escaping (double-quote, backslash etc)
//        int[] esc = CharacterEscapes.standardAsciiEscapesForJSON();
        int[] esc = new int[200];
        // and force escaping of a few others:
        esc['<'] = CharacterEscapes.ESCAPE_STANDARD;
        esc['>'] = CharacterEscapes.ESCAPE_STANDARD;
        esc['&'] = CharacterEscapes.ESCAPE_STANDARD;
        esc['\''] = CharacterEscapes.ESCAPE_STANDARD;
        asciiEscapes = esc;
    }

    // this method gets called for character codes 0 - 127
    @Override
    public int[] getEscapeCodesForAscii() {
        return asciiEscapes;
    }

    // and this for others; we don't need anything special here
    @Override
    public SerializableString getEscapeSequence(int ch) {
        // no further escaping (beyond ASCII chars) needed:
        return null;
    }

    public static void main(String... args) {
        JacksonXmlEscapeDemo htmlCharacterEscapes = new JacksonXmlEscapeDemo();
        int[] escapeCodesForAscii = htmlCharacterEscapes.getEscapeCodesForAscii();
        System.out.println("length of array : " + escapeCodesForAscii.length);
        for (int i = 0; i < escapeCodesForAscii.length; i++) {
//            System.out.println(escapeCodesForAscii[i]);
        }

        String text = "&*^%$#@";
        for (int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);
            int value = c;
            System.out.println(value);
        }
        System.out.println("------------------------------------------------------------------");
        for (int i = 0; i < text.length(); i++)
        {
            int codePoint = text.codePointAt(i);
            // Skip over the second char in a surrogate pair
            if (codePoint > 0xffff)
            {
                i++;
            }
            System.out.println(codePoint);
        }

    }





}