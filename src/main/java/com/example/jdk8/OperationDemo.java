/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.jdk8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author tbu
 */
public class OperationDemo {


    // An interface with only one abstract method is a functional interface
    interface IntegerMath {
        int operation(int a, int b);
    }
    @FunctionalInterface
    interface PlayString {
        String operate(String... arg);
    }
    

    public int operateBinary(int a, int b, IntegerMath im) {
        return im.operation(a, b);
    }
    
    public String operateTest(PlayString ps, String... args) {
        return ps.operate(args);
    }

    public static void main(String... args) {

        //  @functionInterface =   $lambdaexpression ( which is a anonymous class )
        IntegerMath addition = (a, b) -> a + b; //        (type argument, ….) –> { java statements; }
        IntegerMath subtraction = (a, b) -> a - b;//        (type argument, ….) –> { java statements; }

        // testing now
        OperationDemo myApp = new OperationDemo();
        System.out.println("40 + 2 =" + myApp.operateBinary(40, 2, addition));
        System.out.println("20 – 10 = " + myApp.operateBinary(20, 10, subtraction));


        // test string operation
        PlayString findFirstPS = (a) -> Arrays.stream(a).findFirst().get();
        System.out.println("find the first parameter : " + myApp.operateTest(findFirstPS, "aaaa", "bbbb", "cccc"));


        // be careful of Arrays.asList("h","b","c","h1","h2")
        List<String> strs = new ArrayList<>(Arrays.asList("h","b","c","h1","h2","uuu","uu","u"));
        strs.stream().
                filter(a -> a.startsWith("h"))
                .map(b  -> b.toUpperCase())
                .sorted()
                .forEach(System.out::println);
        System.out.println(strs);

        boolean isMatched  = strs.stream().anyMatch((s) ->s.startsWith("h"));
        Predicate<String> predicate = (s) -> s.length() < 2;

        strs.removeIf( predicate );
        strs.removeIf( e -> isMatched );

        Optional<String> reducedList =
                strs.stream()
                .sorted()
                .reduce((s1, s2) -> s1 + s2);
        reducedList.ifPresent(System.out::println);

        List<String> list = new ArrayList<>();
        list.add("cat");
        list.add("dog");
        list.add("cheetah");
        list.add("deer");

        // Remove elements that start with c.
        list.removeIf(element -> element.length() < 4 );

//        list.removeIf(element -> element.startsWith("c"));
        System.out.println(list.toString());

    }


}
