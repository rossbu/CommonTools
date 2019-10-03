package com.example.jdk8;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author tbu
 */


class Something {
    String startsWith(String s) {
        return String.valueOf(s.charAt(0));
    }
}
@FunctionalInterface  // informative // can be omitted // An interface with only one abstract method is a functional interface
interface ConvertFI<F, T> {
  abstract T convert(F from);  // abstract can be ommited
}


@FunctionalInterface // informative and can be ignored.
interface TestFI {
    boolean test(int num);
}


public class Main {


    Thread thread = new Thread( new Runnable() {
        @Override
        public void run() {
            System.out.println("print sth");
        }
    });


    public static List<Integer> filter(TestFI testNum, List<Integer> listItems) {
        List<Integer> result = new ArrayList<Integer>();
        for(Integer item: listItems) {
            if(testNum.test(item)) {
                result.add(item);
            }
        }
        return result;
    }
    public static void main(String[] args) throws UnsupportedEncodingException {


        // random coding

        List<? extends Number> foo1 = new ArrayList<Number>();  // Number "extends" Number (in this context)
        List<? extends Number> foo2 = new ArrayList<Integer>(); // Integer extends Number
        List<? extends Number> foo3 = new ArrayList<Double>();  // Double extends Number




        // calling filter method with a lambda expression
        // as one of the param
        List<Integer> myList = new ArrayList<Integer>();
        myList.add(1);
        myList.add(4);
        myList.add(6);
        myList.add(7);
        Collection<Integer> results = filter(n -> n > 5, myList);
//        RunnableTest.main(args);
//        ComparatorTest.main(args);
//        ListenerTest.main(args);

        // Encode 
        String asB64 = Base64.getEncoder().encodeToString("some string".getBytes("utf-8"));
        System.out.println(asB64); // Output will be: c29tZSBzdHJpbmc=

        // Decode
        byte[] asBytes = Base64.getDecoder().decode("c29tZSBzdHJpbmc=");
        System.out.println(new String(asBytes, "utf-8")); // And the output is: some string


        // demo  usage of "::"  for instance methods and constructor
        ConvertFI<String, Integer> converter = Integer::valueOf;
        Integer convertedValue = converter.convert("123");
        System.out.println(convertedValue); // 123

        Something something = new Something();
        ConvertFI<String, String> converter1 = something::startsWith;
        String converted1 = converter1.convert("Java");
        System.out.println(converted1);   // J

        Function<String, Integer> toInteger = Integer::valueOf;
        Function<String, String> backToString = toInteger.andThen(String::valueOf);
        backToString.apply("123");     // "123"

        int max = 1000000;
        List<String> values = new ArrayList<>(max);
        for (int i = 0; i < max; i++) {
            UUID uuid = UUID.randomUUID();
            values.add(uuid.toString());
        }
        long t0 = System.nanoTime();
        long count = values.stream().sorted().count();
        System.out.println(count);

        long t1 = System.nanoTime();
        long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        System.out.println(String.format("sequential sort took: %d ms", millis));

        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);

        Map<Integer, String> map = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            map.putIfAbsent(i, "val" + i);
        }
        map.forEach((id, val) -> System.out.println(val));
        
        map.computeIfPresent(3, (num, val) -> val + num);
        map.get(3);
        // val33

        map.computeIfPresent(9, (num, val) -> null);
        map.containsKey(9);
        // false

        map.computeIfAbsent(23, num -> "val" + num);
        map.containsKey(23);
        // true

        map.computeIfAbsent(3, num -> "bam");
        map.get(3);
        // val33

    }

}
