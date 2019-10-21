package com.jdk.jdk8.Demo;

import com.pojo.CarInfo;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * ## Functional interface
 * From Java 8 onwards, lambda expressions can be used to represent the instance of a functional interface [ The lambda expression is an anonymous method (a method without a name) ]
 * it has Single Abstract Method ( SAM) can be called as Functional Interface.
 * it can have multiple default methods.
 * it can have multiple static methods.
 * e.g,
 * Runnable, Comparator,Cloneable are some of the examples for Functional Interface.
 * <p>
 * Any interface with a SAM(Single Abstract Method) is a functional interface, and its implementation may be treated as lambda  expressions.
 */
public class FunctionDemo {
    public static void main(String[] args) {
//        simpleFunction();
//        functionCompose();
//        TriFunction();
        functionIndentity();

        intFunctionTest();

//        customizedFunction();



    }

    public static <T, U> List<U> convertToAnotherList(List<T> sourceList, Function<T,U> convertFunction) {
        Objects.requireNonNull(sourceList,"sourceLIst can't be null");
        List<U> collect = sourceList.stream().map(convertFunction).collect(toList());
        System.out.println(collect);
        return collect;
    }

    public static <T,U> U[] convertToAnotherArray(T[] sourceArray, Function<T,U> convertFunction, IntFunction<U[]> generator) {
        return Arrays
                .stream(sourceArray)
                .map(convertFunction)
                .toArray(generator);
    }

    private static void customizedFunction() {
        convertToAnotherList(Arrays.asList("abc", "efg", "hij"), e -> e.toUpperCase());
        String[] array = new String[20];
        Arrays.stream(array).forEach(System.out::println);
        Arrays.setAll(array, idx -> String.valueOf(idx));

        Double[] doubles = convertToAnotherArray(array, Double::parseDouble, Double[]::new);  // Double[]::new is calling new Double[]
        System.out.println(Arrays.toString(doubles));




    }

    private static void intFunctionTest() {
        IntFunction<String[]> generator = String[]::new;
        String[] apply = generator.apply(1);

        IntFunction<CarInfo[]> carInfoGenerator = CarInfo[]::new;  // new CarInfo[]
        IntFunction<CarInfo> carInfoGenerator2 = CarInfo::new;  // new CarInfo[]
        CarInfo apply1 = carInfoGenerator2.apply(2);
        System.out.println(apply1);
        IntFunction<Double> ob = a -> a / 2.0;
        System.out.println(ob.apply(3));

        //      How to declare an array without specifying its size?  Can't be done. Arrays are immutable (in size, not in contents) and you have to specify a size. You either have to use java.util.ArrayList (which requires Object elements) or create your own dynamic array class.

        int[] testarray = new int[3]; // without 3, it throws compilation error.
        String[] testStringArray = new String[50];
        Function<Integer,int[]>  arrayCreator3 = int[]::new;
        int[] intArray  = arrayCreator3.apply(5);
        System.out.println(Arrays.toString(intArray));
    }

    /**
     * this is used when you want ot convert a list employees to a map[emptyid, employee]  toMap(Employee::getId, Function.indentity
     * As we now know, the identity function does nothing, it just returns back the object it receives as input.
     * So, what we get back is the same object which we passed to the applyIdentityToObject() method! And the same i.e. original employee list is printed as output!!
     */
    private static void functionIndentity() {
        Map<String, String> collectedMap = Arrays.asList("bu", "kun", "florina", "fern", "mohan").stream()
//                .map(Function.identity())
//                .map(str -> str)
                .collect(Collectors.toMap(
                        Function.identity(), str -> str
                ));
        System.out.println(collectedMap);
    }

    private static void TriFunction() {
        TriFunction<Sum, String, String, Integer> lambda = (Sum s, String arg1, String arg2) -> s.doSum(arg1, arg1);
        System.out.println(lambda.apply(new Sum(), "1", "4"));
    }

    private static void functionCompose() {
        Function<Double, Double> sin = d -> Math.sin(d);
        Function<Double, Double> log = d -> Math.log(d);
        Function<Double, Double> exp = d -> Math.exp(d);
        System.out.println(sin.compose(log).andThen(exp).apply(5.0));


        Function<Integer, String> i2s = (i) -> Integer.toString(i);
        Function<String, Integer> s2i = (s) -> Integer.parseInt(s);
        System.out.println(i2s.compose(s2i).apply("30").length()); // s2i ->  i2s -> length()
    }

    private static void simpleFunction() {
        Function<Integer, Integer> func = x -> x * 2;
        System.out.println(func.apply(2));

        Function<String, String> function2 = x -> x.toLowerCase();
        System.out.println(function2.apply("CAP"));


        Function<Integer, Integer> intToIntFunction = Integer::valueOf;
        Function<String, Integer> StringToIntFun = Integer::valueOf;
        Function<String, String> backToString = StringToIntFun.andThen(String::valueOf);
        backToString.apply("123");
    }


    static class Sum {
        Integer doSum(String s1, String s2) {
            return Integer.parseInt(s1) + Integer.parseInt(s1);
        }
    }

    /**
     * Java has a Function interface that takes one parameter, a BiFunction that takes two parameters, but there's no TriFunction that takes three parameters, so let's make one: TriFunction
     *
     * @param <T>
     * @param <U>
     * @param <V>
     * @param <R>
     */
    interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }
}
