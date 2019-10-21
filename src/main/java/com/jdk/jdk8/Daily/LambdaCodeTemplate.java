package com.jdk.jdk8.Daily;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by tbu on 4/17/2017.
 *
 * Don't check in this code template after you finished it. just revert it
 * <p>
 * tips:
 * 1. when you do codeNow. use sout, souv to print all results in Intellij
 * 2. a unary operation is an operation with only one operand.
 * 3. http://www.java2s.com/Tutorials/Java/Java_Lambda/
 */
public class LambdaCodeTemplate {

    public static void main(String... args) {

        /*********************************************************************************************************************
         * Create Sample Data -  do listOfString, newListOfString, fixedLengthIntArray,integerArrayList,newListOfInteger
         *********************************************************************************************************************/


        /*********************************************************************************************************************
         * Lambda Expression
         *********************************************************************************************************************/
        // Lambda expression 1: Empty brackets are used when there are no arguments - please do print "test"

        // Lambda expression 2: Single argument and ignore the brackets - do square and subtraction ( Not subtraction)

        // lambda expression 3: 2 arguments - do square of a plus square of b

        // Lambda expression 4: as Parameter Names - do send lambda to Array.sort or Collection sort

        // lambda expression 5: composition - do sin and then log  with Function Interface Class

        /*********************************************************************************************************************
         Types of Lambda Expression
         in Java, the lambda expressions are represented as objects,
         lambda expressions must be bound to a particular object type known as a functional interface. This is called the target type. see below
         *********************************************************************************************************************/

        // Assign a lambda expression to a Function  ( Interface ) , aks, use lambda to create a Function argument multiplied by two

        // Assign a lambda expression to a Supplier , A Supplier provides values. We call get() on it to retrieve its value—it may return different values when called more than once.

        // Assign a lambda expression to a BooleanSupplier -  do simply return a true BooleanSupplier

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

        // A lambda expression can access final local variables or local-non-final-initialized-only-once variables.


        /*********************************************************************************************************************
         Lambda Context:
             Lambda expressions can be used only in the following four contexts.
             Assignment Context
             Method Invocation Context
             Return Context
             Cast Context

         *********************************************************************************************************************/
        // lambda expression can appear to the right of the assignment operator. - do 2 arguments return Calculator

        // lambda expression Return Context -  do return Calculator from Create method

        // lambda expression  Method Invocation Context  :   Functional Interface as Parameter - do lambda as parameter
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

    public static Double sin(Double d){
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

    private static void engine(Calculator calculator){
        long x = 2, y = 4;
        long result = calculator.calculate(x,y);
        System.out.println(result);
    }

    private static Calculator create(){
        return (x,y)-> x / y;
    }

    @FunctionalInterface
    interface Calculator{
        long calculate(long x, long y);
    }
}
