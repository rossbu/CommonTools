package com.example.jdk8;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by tbu on 4/4/2017.
 * The Java 8 Collectors class provides useful implementations, which can be used by Streams' collect() method.
 * Some of these operations presented here include computing sums, averages, maximums, and minimums.
 * Partitioning a List based on a predicate returns a pair of Lists enclosed in a Map.
 *
 *
 * https://dzone.com/articles/using-java-collectors
 */
public class StreamDemo1 {

    public static void main(String []args){
        Random random = new Random();
        List<Integer> numbers = random
                .ints(1, 100)
                .limit(10)
                .boxed()
                .collect(Collectors.toList());

        // Summing an Integer List
        int sum = numbers.stream().reduce(0, (x, y) -> x+y);
        System.out.println("sum : " + sum);


        // Computing the average of a list of numbers is similarly a piece of cake. The Collectors provide an averagingInt() method for the purpose.
        double avg = numbers.stream().collect(Collectors.averagingInt(x->x));
        System.out.println("avg : " + avg);

//        Maximum and Minimum
        Optional<Integer> max = numbers.stream().collect(Collectors.maxBy(Integer::compare));
        Optional<Integer> min = numbers.stream().collect(Collectors.minBy(Integer::compare));
        System.out.println("max : " + max);
        System.out.println("min : " + min);

//        Summarizing in One Shot
//        Or why bother computing sum, average, etc. separately? Just use summarizingInt() as shown.
        IntSummaryStatistics r = numbers.stream().collect(Collectors.summarizingInt(x -> x));
        System.out.println("r : " + r);


//        Let us see how to partition a List of numbers into two lists using a criterion (such as values greater than 50):
        Map<Boolean,List<Integer>> parts = numbers.stream().collect(Collectors.partitioningBy(x -> x > 50));
        System.out.println(numbers + " =>\n" +
                "  > 50 : " + parts.get(true) + "\n" +
                "  <=50 : " + parts.get(false) + "\n");
    }
}
