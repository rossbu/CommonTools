package com.jdk.jdk.basic;


/**
 * Hello, Java world
 */
public class TestPassByReference {
    public static void main(String[] args) {
        // int, Integer, String are immutable type.  you can't pass by reference of value but pass by value only ,but apache commons has mutableInt and etc.
        int i = 25;
        Integer boxedI  = new Integer(25);
        String s = "Java is fun!";

        // StringBuffer and other classes that have Set(sth). which means mutable.
        StringBuffer sb = new StringBuffer("Hello, world");

        // print variable i and objects s and sb
        System.out.println(i);     // print it (1)
        System.out.println(s);    // print it (2)
        System.out.println(sb);  // print it (3)

        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");


        // attempt to change i, s, and sb using methods
        iMethod(i);
        sMethod(s);
        sbMethod(sb);

        integerBoxedMethod(boxedI);

        System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");


        // print variable i and objects s and sb (again)
        System.out.println(i);    // print it (7)
        System.out.println(s);   // print it (8)
        System.out.println(sb); // print it (9)
        System.out.println(boxedI);
    }

    public static void iMethod(int iTest) {
        iTest = 9;                          // change it
        System.out.println(iTest); // print it (4)
        return;
    }

    public static void integerBoxedMethod(Integer iTest) {
        iTest = new Integer(100);                          // change it
        System.out.println(iTest); // print it (4)
        return;
    }

    public static void sMethod(String sTest) {
        sTest = sTest.substring(8, 11); // change it
        System.out.println(sTest);        // print it (5)
        return;
    }

    public static void sbMethod(StringBuffer sbTest) {  // StringBuffer is just like other customized Class , that you can change the 'content' of it .
        sbTest = sbTest.insert(7, " [extra Java insertion] "); // change it
        System.out.println(sbTest);            // print it (6)
        return;
    }
}