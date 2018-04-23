package com.example.jdk8;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Created by tbu on 4/4/2017.
 * The Java 8 Collectors class provides useful implementations, which can be used by Streams' collect() method.
 * Some of these operations presented here include computing sums, averages, maximums, and minimums.
 * Partitioning a List based on a predicate returns a pair of Lists enclosed in a Map.
 *
 *
 * https://dzone.com/articles/using-java-collectors
 */
public class StreamDemo {

    public static void main(String []args){
        Random random = new Random();
        List<Integer> numbers = random
                .ints(1, 100)
                .limit(10)
                .boxed()
                .collect(toList());

        // https://stackoverflow.com/questions/34179062/intstream-foreach-method-in-java-8  , how foreach implemented
        List<Integer> numbers2 = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        numbers2.stream()
                .filter(i -> i % 2 == 0)
                .distinct()
                .limit(1)
                .forEach(System.out::println);

        List<String> words = Arrays.asList("Java8", "Lambdas", "In", "Action");

        // mapping it into a new element (the word mapping is used because it has a meaning similar to transforming but with the nuance of “creating a new version of” rather than“modifying”).
        List<Integer> wordLengths = words.stream()
                .map(String::length)
                .skip(1)
                .limit(2)
                .collect(toList());



        // Summing an Integer List
        reduce(numbers);


        // Computing the average of a list of numbers is similarly a piece of cake. The Collectors provide an averagingInt() method for the purpose.
        averagingInt(numbers);

        //  Maximum and Minimum
        maximumAndMinimum(numbers);


        // why bother computing sum, average, etc. separately? Just use summarizingInt() as shown. Just   Summarizing in One Shot
        summarizingInt(numbers);


        // Let us see how to partition a List of numbers into two lists using a criterion (such as values greater than 50):
        partitioningBy(numbers);

        // peek
        peek();

    }

    private static void reduce(List<Integer> numbers) {
        int sum = numbers.stream().reduce(0, (x, y) -> x+y);
        System.out.println("sum : " + sum);
    }

    private static void averagingInt(List<Integer> numbers) {
        double avg = numbers.stream().collect(Collectors.averagingInt(x -> x));
        System.out.println("avg : " + avg);
    }

    private static void maximumAndMinimum(List<Integer> numbers) {
        Optional<Integer> max = numbers.stream().collect(Collectors.maxBy(Integer::compare));
        Optional<Integer> min = numbers.stream().collect(Collectors.minBy(Integer::compare));
        System.out.println("max : " + max);
        System.out.println("min : " + min);
    }

    private static void summarizingInt(List<Integer> numbers) {

        IntSummaryStatistics r = numbers.stream().collect(Collectors.summarizingInt(x -> x));
        System.out.println("r : " + r);
    }

    private static void partitioningBy(List<Integer> numbers) {
        Map<Boolean,List<Integer>> parts = numbers.stream().collect(Collectors.partitioningBy(x -> x > 50));
        System.out.println(numbers + " =>\n" +
                "  > 50 : " + parts.get(true) + "\n" +
                "  <=50 : " + parts.get(false) + "\n");
    }

    /**
     * Stream.peek() is a non-interfering Stream operation.
     * Non-interfering methods are those which guarantee that they will not modify the Stream’s data source during their execution.
     * Non-interfering nature is required in multi-threaded environments
     *

     Explanation of the code
     PeekingInStreams class first creates an infinite stream using Stream.iterate() method by specifying logic to generate consecutive integers starting from 1.
     Next iterate() method is pipelined to the peek() method which uses a lambda expression equivalent of the Consumer interface – (n -> System.out.println("number generated: - " + n)). This expressions simply prints the generated number.
     Next, a filter is applied to the Stream using the Stream.filter() method. The filter’s predicate only allows even numbers to pass through using the logic n%2==0.
     peek() is again pipelined next, and it prints the numbers obtained from the filter indicating which numbers passed the even number filter.
     Stream.limit() method is used to restrict the infinite stream to 5 numbers.
     Lastly, to end the pipeline of operations, Stream.count() method is invoked which is a terminal operation.
     As expected, the output shows that only even numbers – 2,4,6,8,10 pass through the filter.
     */
    private static void peek() {
        Stream.iterate(1, (Integer n) -> n + 1)
                .peek(n -> System.out.println("number generated: - " + n))
                .filter(n -> (n % 2 == 0))
                .peek(n -> System.out.println("Even number filter passed for - " + n))
                .limit(5)
                .count();
    }
}
