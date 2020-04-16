package com.interview;

import com.google.common.cache.RemovalListener;

import java.io.FilenameFilter;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by tbu on 4/17/2017.
 * <p>
 * Don't check in this code template after you finished it. just revert it
 * <p>
 * tips:
 * 1. when you do codeNow. use sout, souv to print all results in Intellij
 * 2. a unary operation is an operation with only one operand.
 * 3. http://www.java2s.com/Tutorials/Java/Java_Lambda/
 * <p>
 * <p>
 * for you local only, rollback after you play with it.
 */
public class LambdaCodePractice {

    public static void main(String... args) {

        /*********************************************************************************************************************
         Preparation with Sample data for Code Challenges

         Create Sample Data -  do listOfString, newListOfString, fixedLengthIntArray,integerArrayList,newListOfInteger
         *********************************************************************************************************************/


       /*********************************************************************************************************************
         Code Challenge  -  1
         Basic Lambda Expression
         *********************************************************************************************************************/

        // Lambda expression 1: Empty brackets are used when there are no arguments - please do print "test"
        Supplier<String> stringSupplier = () -> "hello world";

        // Lambda expression 2: Single argument and ignore the brackets - do square and subtraction ( Not subtraction)
        Consumer test = x -> System.out.println(x);
        test.accept("it's me");

        // lambda expression 3: Create a lambda with arguments - do square of a plus square of b
        DoubleBinaryOperator doubleBinaryOperator = (min, max) -> Math.sqrt(min) + Math.sqrt(max);
        double v = doubleBinaryOperator.applyAsDouble(1.22, 44);
        System.out.println("double result : " + v);

        // Lambda expression 4:  Create a lambda ( Comparator?) as Parameter  - do send it to Arrays.sort or Collection sort
        Comparator<String> comparator = (o1, o2) -> o1.length() - o2.length();
        String[] arrayOfStr = {"z", "1dd", "badfaf", "dc", "aafdfd"};
        Arrays.sort(arrayOfStr, comparator);
        Stream.of(arrayOfStr).forEach(s -> System.out.println(s));

        // lambda expression 5: composition - do Math sin, log , exp in 3 functions and combine the together to work. x.andThen(y) is the same as y.compose(x)
        Function<Double, Double> sinF = d -> Math.sin(d);
        Function<Double, Double> sinF1 = d -> Math.sin(d);
        Function<Double, Double> logF = d -> Math.log(d);
        Function<Double, Double> logF1 = d -> Math.log(d);
        Function<Double, Double> exp  = d -> Math.exp(d);
        Function<Double, Double> exp1  = d -> Math.exp(d);
        System.out.println("composition of 3 math func 1 : " + sinF.compose(logF).compose(exp).apply(55.2));
        System.out.println("composition of 3 math func 2 : " + exp1.andThen(sinF1.compose(logF1)).apply(55.2));


        // Assign a lambda expression to a Function  ( Interface ) , aks, use lambda to create a Function argument multiplied by two
        Function<Integer, Integer> doubleTheIntegerFunc = x -> x * 2;
        System.out.println(doubleTheIntegerFunc.apply(3));

        // Assign a lambda expression to a Supplier , A Supplier provides values. We call get() on it to retrieve its value—it may return different values when called more than once.
        Supplier<String> doATest =  () -> "abac";

        // Assign a lambda expression to a BooleanSupplier -  do simply return a true BooleanSupplier
        BooleanSupplier isFinished = () -> true;
        int call5MillTimes = 2000;
        int call3millTimes = 1000;
        BooleanSupplier isBigger  = () -> call5MillTimes > call3millTimes; // which means don't call this unless you have to compare them.

        // Assign a lambda expression to a IntSupplier , which returns one int value -  do return the max of integer as a IntSupplier


        // Assign a lambda expression to a Consumer, Opposite a Supplier, Consumer acts upon a value but returns nothing,System.out.println return void , good use.

        // Assign a lambda expression to a IntConsumer, This function accepts a primitive int and does not return any value, IntConsumer is a specialized version of Consumer<T>.

        // Assign a lambda expression to a Predicate (predicate is used in computer science to mean a boolean-returning method), aka Predicate Lambda.  RemoveIf: This method on ArrayList receives a Predicate. Here, we remove all elements starting with the letter "c."

        // Assign a lambda expression to a IntPredicate

        // Assign a lambda expression to a BiConsumer, A BiConsumer is a functional object that receives two parameters. Here we use a BiConsumer in the forEach method on HashMap.

        // Assign a lambda expression to Runnable

        // Assign a lambda expression to Callable

        // Assign a lambda expression to UnaryOperator

        // Assign a lambda expression to a BiFunction, This function accepts two arguments and returns a value.

        // Assign a lambda expression to BinaryOperator  take two T's as input, return one T as output, useful for "reduce" operations



        /*********************************************************************************************************************
         Code Challenge  -  2

             Lambda Context:
             Lambda expressions can be used only in the following four contexts.
             Assignment Context
             Method Invocation Context
             Return Context
             Cast Context
         *********************************************************************************************************************/

        // lambda expression can access final local variables or local-non-final-initialized-only-once variables.

        // lambda expression can appear to the right of the assignment operator. - do 2 arguments return Calculator

        // lambda expression Return Context -  do return Calculator from Create method

        // lambda expression FunctionalInterface/Lambda as Method parameter



        sortPersonMapByJava8MapComparator();
        filterNullValue();
        allkindsOfforEach();


    }

    private static void allkindsOfforEach() {

    }

    private static void filterNullValue() {
        List<String> cList = new ArrayList<String>();
        cList.add("United States of America");
        cList.add("Ecuador");
        cList.add("Denmark");
        cList.add(null);
        cList.add("Seychelles");
        cList.add("Germany");
        cList.add("Germany");
        cList.add(null);
        List<?> mixedTypeList = Arrays.asList("a","b","",1,2,3,true,false,null);

        // EXAMPLE #1 = Filter Null Values from a Stream Using 'Lambda Expressions'


        // EXAMPLE #2 = Filter Null Values from a Stream Using 'Method Reference'


        // EXAMPLE #3 = Filter Null Values after Map intermediate operation

        // EXAMPLE #4 = Filter null from mixedTypeList
    }


    /**
     Req-1: given a list of persons (Person object builder mode)
     consider adding the persons to map where the key is the person id and the values are the person themselves object.
     Then sort the list by id, and print the results.
     use java 8 comparator to compare
     Java 8 has added static methods comparingByKey and comparingByValue to Map.Entry.
     Printing the elements sorted by key is shown in Sorting Map elements by key and printing.
     */
    private static void sortPersonMapByJava8MapComparator() {
        // prepare Data : create 3 Persons  ps1, ps2, ps3  use Person.java Object

        // streaming the list and map to key-value and sort and print

    }

    public static void printSuppliedString(Supplier<String> supplier) {
        System.out.println(supplier.get());
    }

    public static void consumeString(Consumer<String> consumer, String x) {
        consumer.accept(x);
    }

    public static void testValue(Predicate<Double> predicate, Double d) {
        predicate.test(d);
    }

    public static Double sin(Double d) {
        System.out.println("sin:");
        return Math.sin(d);
    }

    public static Double log(Double d) {
        System.out.print("log:");
        return Math.log(d);
    }

    public static Double exp(Double d) {
        System.out.print("exp:");
        return Math.exp(d);
    }

    private static void engine(Calculator calculator) {
        long x = 2, y = 4;
        long result = calculator.calculate(x, y);
        System.out.println(result);
    }

    private static Calculator create() {
        return (x, y) -> x / y;
    }

    @FunctionalInterface
    interface Calculator {
        long calculate(long x, long y);
    }
}
