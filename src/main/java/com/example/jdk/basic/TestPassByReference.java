package com.example.jdk.basic;


/**
 * Hello, Java world
 */
public class TestPassByReference {
    public static void main(String[] args) {
        // declare and initialize variables and objects
        int i = 25;
        String s = "Java is fun!";
        StringBuffer sb = new StringBuffer("Hello, world");

        // print variable i and objects s and sb
        System.out.println(i);     // print it (1)
        System.out.println(s);    // print it (2)
        System.out.println(sb);  // print it (3)

        // attempt to change i, s, and sb using methods
        iMethod(i);
        sMethod(s);
        sbMethod(sb);

        // print variable i and objects s and sb (again)
        System.out.println(i);    // print it (7)
        System.out.println(s);   // print it (8)
        System.out.println(sb); // print it (9)

    }

    public static void iMethod(int iTest) {
        iTest = 9;                          // change it
        System.out.println(iTest); // print it (4)
        return;
    }

    public static void sMethod(String sTest) {
        sTest = sTest.substring(8, 11); // change it
        System.out.println(sTest);        // print it (5)
        return;
    }

    public static void sbMethod(StringBuffer sbTest) {
        sbTest = sbTest.insert(7, "Java "); // change it
        System.out.println(sbTest);            // print it (6)
        return;
    }
}