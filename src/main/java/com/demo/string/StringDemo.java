package com.demo.string;

public class    StringDemo {
    public static void main(String[] args) {
        testSplitFromLast();
    }
    public static String[] splitAtLastString(String s, String delimiter) {
        if (s == null || delimiter == null) return new String[0];
        int pos = s.lastIndexOf(delimiter);
        if (pos == -1)
            return new String[]{s};
        return new String[]{s.substring(0, pos), s.substring(pos+delimiter.length() )};
    }

    public static void testSplitFromLast() {
        String testString = "username--test--tokenstring";
        String[] strings = splitAtLastString(testString,"--");
        for (String string : strings) {
            System.out.println("string = " + string);
        }
        testString = "username--testtokenstring";
        strings = splitAtLastString(testString,"--");
        for (String string : strings) {
            System.out.println("string = " + string);
        }

        testString = "usernamdsd--3e--testt..oksadffdsenstring";
        strings = splitAtLastString(testString,"--");
        for (String string : strings) {
            System.out.println("string = " + string);
        }
    }
}
