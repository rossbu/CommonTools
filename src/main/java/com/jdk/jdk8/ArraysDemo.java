package com.jdk.jdk8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *
 <pre>
    please know:
        Arrays are immutable (in size, not in contents) and you have to specify a size, othersize use arraylist
        Array is a fixed size data structure while ArrayList is not. One need not to mention the size of Arraylist while creating its object. Even if we specify some initial capacity, we can add more elements.
        for example:
            ArrayList<Integer> arrL = new ArrayList<Integer>(2);  // you still can add more

        Array can contain both primitive data types as well as objects of a class depending on the definition of the array. However, ArrayList only supports object entries, not the primitive data types.
        for example:
         int[] arr = new int[3];
         arr[0] = 1;
         arr[1] = 2;
         arr[2] = 3;
 Array Constructor References
    We can create an array using array constructor as follows.

    ArrayTypeName::new
    e.g.
    int[]::new is calling new int[]
    Double[]::new is calling new Double[]

 </pre>
 */
public class ArraysDemo {
    public static void main(String[] args) {

        int[] testarray = new int[10]   ;

        replaceAllByLambda();

        String[] array = new String[20];
        Arrays.setAll(array, idx -> String.valueOf(idx).concat(" yes"));
        Arrays.stream(array).forEach(System.out::println);
        Arrays.parallelSetAll(array, (index) -> Integer.toString(index + 1));
        Arrays.stream(array).forEach(System.out::println);
    }

    private static void replaceAllByLambda() {
        String[] arrTest = {"sunday","monday", "tuesday", "wednesday", "friday", "saturday"};
        List<String> arrTest1 = List.of(arrTest);
        ArrayList<String> arrayList = new ArrayList(); // you can't operate on arrtest1 or arrtest directly.
        arrayList.addAll(arrTest1);

        arrayList.replaceAll(s -> s.replaceFirst("day$", ""));
        arrayList.stream().forEach(System.out::println);
    }
}
