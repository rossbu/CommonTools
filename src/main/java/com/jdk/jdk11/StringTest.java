package com.jdk.jdk11;
import java.util.*;



public class StringTest {
    public static void main(String[] args) {
        System.out.println(" ".isBlank()); //true
        String s = "Anupam";
        System.out.println(s.isBlank()); //false
        String s1 = "";
        System.out.println(s1.isBlank()); //true


        String str = " JD ";
        System.out.print("Start");
        System.out.print(str.strip());
        System.out.println("End");

        System.out.print("Start");
        System.out.print(str.stripLeading());
        System.out.println("End");

        System.out.print("Start");
        System.out.print(str.stripTrailing());
        System.out.println("End");


        String astr = "=".repeat(2);
        System.out.println(astr); //prints ==
    }
}
