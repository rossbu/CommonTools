package com.jdk.jdk8.Daily;

import java.security.PrivilegedAction;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.*;
import java.util.stream.*;

/**
 * Created by tbu on 4/17/2017.
 * <p>
 * tips:
 *
 *  http://www.java2s.com/Tutorials/Java/java.util.function/ToLongBiFunction/index.htm  for Function example
 *  http://www.java2s.com/Tutorials/Java/Java_Stream/index.htm for stream example
 *  when you do codeNow. use sout, souv to print all results in Intellij
 *  a unary operation is an operation with only one operand.

 */
public class CodeNow {

    public static void main(String... args) {

//        LambdaCode();
        IntStreamCode();
        StreamCode();
        OptionalCode();

        List<String> words = Arrays.asList("Hello", "World");

        words.stream()
                .flatMap((String line) -> Arrays.stream(line.split("")))
                .distinct()
                .forEach(System.out::println);
    }

    private static void StreamCode() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        int sum = numbers.stream()
                .filter(n -> n % 2  == 1)
                .map(n  -> n * n)
                .reduce(0, Integer::sum);
        System.out.println(sum);
    }

    private static void IntStreamCode() {

        // IntStream is a stream of primitive int values.  Replace traditional for loops with IntStreams
        IntStream.of(1, 2, 3).filter(x-> x > 2).findFirst().getAsInt();

        // IntStream range equivalent to :  for (int i = startInclusive; i < endExclusive ; i++) { ... }
        IntStream.range(1, 3);

        // IntStream rangeClosed  equivalent to : // for (int i = startInclusive; i <= endExclusive ; i++) { ... }
        IntStream.rangeClosed(1, 3);

        // IntStream iterate and generate
        IntStream.iterate(0, i -> i + 2).limit(3);
        IntStream.generate(() -> Integer.MAX_VALUE);
        IntStream.generate(() -> ThreadLocalRandom.current().nextInt(10)).limit(3);

        // IntStream to a Stream<Integer>
        Stream<Integer> boxedIntegerStream = IntStream.generate(()-> 20).filter(x -> x % 2 != 0).boxed();
        DoubleStream streamDouble = IntStream.range(1, 5).mapToDouble(i -> i);
        LongStream streamLong = IntStream.range(1, 5).mapToLong(i -> i);

        // IntStream then match ( None, Any and All )
        IntStream.range(1, 5).anyMatch(i -> i % 2 == 0);
        IntStream.range(1,10).noneMatch(x -> x % 2 ==0);
//        IntStream.generate(() -> 200).allMatch( x -> x < 201);  // take a long time........


        // reduce
        IntStream.range(1,5).reduce(2,(x,y) -> x* y);  // means result is :  2 * x*y

        // concurrency
//        IntStream.range(1, 5).parallel().forEach(i -> fetchResultsOverHttp(i));



    }

    private static void OptionalCode() {
        // create a Optional
        Optional<Integer> op = Optional.empty();

        // print value
        System.out.println("Optional: " + op);
    }

    private static void fetchResultsOverHttp(int i) {

    }

    private static void LambdaCode() {
        /*********************************************************************************************************************
         * Data Setup : create Array of Objects for below CodeNow
         *********************************************************************************************************************/
        List<String> listOfString = Arrays.asList("peter", "john", "Alex", "Mike H", "Mike K", "Mike B", "TBU");
        List<String> newListOfString = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
        String[] str = {"one", "two", "3", "four", "five", "sixsix", "sevennnnn", "eight"};
        Integer[] fixedLengthIntArray = {1, 2, 3, 4, 5, 5, 6, 6, 7, 8, 9, 10};
        List<Integer> integerArrayList = Arrays.asList(1, 2, 3, 4, 5, 5, 6, 6, 7, 8, 9, 10);
        List<Integer> newListOfInteger = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 5, 6, 6, 7, 8, 9, 10));


        /*********************************************************************************************************************
         * Lambda Expression : Lambda comes from the Lambda Calculus and refers to anonymous functions in programming
         lambda expression ====  Anonymous functions
         *********************************************************************************************************************/

        // Lambda expression 1: Empty brackets are used when there are no arguments - please do print "test"
        Runnable r = () -> System.out.println("test");
        r.run();

        // Lambda expression 2: single argument and ignore the brackets - do square and subtraction ( Not subtraction)
        IntUnaryOperator square = x -> x * x;
        System.out.println(" 10*10 = " + square.applyAsInt(10));
        IntUnaryOperator subtraction = x -> x - -x;
        System.out.println("2 9- -29 = " + subtraction.applyAsInt(29));

        // lambda expression 3: two arguments - do square of a plus square of b
        IntBinaryOperator plusOperation = (int a, int b) -> a * a + b * b;
        System.out.println("Sum of 10,34 : " + plusOperation.applyAsInt(10, 34));
        Comparator<String> stringComparator = (String x, String y) -> x.length() - y.length();

        // Lambda expression 4: as Parameter Names - do send lambda to Array.sort or Collectgion sort
        Arrays.sort(str, (String s1, String s2) -> {
            return (s1.length() - s2.length());
        });
        Arrays.sort(str, stringComparator);
        Collections.sort(listOfString, stringComparator);

        // lambda expression 5: composition - do sin and then log with Function Interface Class
        Function<Double, Double> sin = d -> Math.sin(d);
        Function<Double, Double> log = d -> Math.log(d);
        Function<Double, Double> exp = d -> Math.exp(d);
        System.out.println(sin.compose(log).andThen(exp).apply(5.0));


        /*********************************************************************************************************************
         the Type of Lambda Expression in Java, the lambda expressions are represented as objects,
         lambda expressions must be bound to a particular object type known as a functional interface. This is called the target type.
         *********************************************************************************************************************/
        Predicate<Integer> isOdd = n -> n % 2 != 0;
        BinaryOperator<Integer> sum = (x, y) -> x + y;
        Callable<Integer> callMe = () -> 42;
        Runnable runner = () -> {
            System.out.println("Hello World!");
        };
        PrivilegedAction<String> action = () -> "Hello";


        // Assign a lambda expression to a Function  ( Interface ) , aks, use lambda to create a Function argument multiplied by two
        Function<Integer, Integer> func = x -> x * 2;
        System.out.println(func.apply(2));
        Function<String, String> function2 = x -> x.toLowerCase();
        System.out.println(function2.apply("CAP"));
        Function<Integer, String> i2s = (i) -> Integer.toString(i);
        Function<String, Integer> s2i = (s) -> Integer.parseInt(s);
        System.out.println(i2s.compose(s2i).apply("30").length()); // s2i ->  i2s -> length()

        // Assign a lambda expression to a Supplier , A Supplier provides values. We call get() on it to retrieve its value—it may return different values when called more than once.
        Supplier<String> supplier1 = () -> "String1"; // return a string
        Supplier<String> supplier2 = () -> "a" + "b"; // return a concatation of string
        Supplier<Boolean> supplier3 = () -> 1 > 2;  // return boolean
        Boolean result = supplier3.get();


        // Assign a lambda expression to a BooleanSupplier  which returns true
        BooleanSupplier bs = () -> true;
        int m = 1, n =2;
        bs = () -> m > n;
        System.out.println(bs.getAsBoolean());

        // Assign a lambda to a IntSupplier which returns one int value -  do return the max of integer as a IntSupplier
        IntSupplier intSupplier = () ->Integer.MAX_VALUE;
        System.out.println(intSupplier.getAsInt());


        // Assign a lambda expression to a Consumer, Opposite a Supplier, Consumer acts upon a value but returns nothing,System.out.println return void , good use.
        Consumer<String> c2 = x -> System.out.println(x.toLowerCase()); // void accept(T t);

        // Assign a lambda expression to a IntConsumer, This function accepts a primitive int and does not return any value, IntConsumer is a specialized version of Consumer<T>.
        {
            IntConsumer intConsumer = x -> System.out.println(x);
            intConsumer.accept(1);

            IntConsumer addOneIntConsumer = x -> newListOfInteger.add(x); // looks boolan and void both accepted
            addOneIntConsumer.accept(100);
            addOneIntConsumer.accept(101);
            addOneIntConsumer.accept(102);

            intConsumer.andThen(addOneIntConsumer).accept(999);

            newListOfInteger.stream()
                    .forEach(i -> System.out.println(i)
                    );
        }

        // Assign a lambda expression to a Predicate (predicate is used in computer science to mean a boolean-returning method), aka Predicate Lambda.  RemoveIf: This method on ArrayList receives a Predicate. Here, we remove all elements starting with the letter "c."
        Predicate<Double> function = x -> x > 10;

        // create a IntPredicate that returns if a given number is even number or not.
        IntPredicate isEvenPredicate = s -> s % 2 == 0;

        // Assign a lambda expression to a BiConsumer, A BiConsumer is a functional object that receives two parameters. Here we use a BiConsumer in the forEach method on HashMap.

        // Assign a lambda expression to Runnable
        {
            Runnable runnable = () -> System.out.println("Hello!");
            runnable.run();
        }
        // Assign a lambda expression to Callable
        {
            Callable<Integer> pi = () -> 3;
            try {
                Integer p = pi.call();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Callable<String>[] callArrays = new Callable[]{() -> "Hello", () -> " callable ", () -> "arrays"};

        }

        // Assign a lambda expression to UnaryOperator
        {
            UnaryOperator<Integer> operator = v -> v * 100;
        }


        // Assign a lambda expression to a BiFunction, This function accepts two arguments and returns a value.

        // Assign a lambda expression to BinaryOperator  take two T's as input, return one T as output, useful for "reduce" operations

        // lambda expression can access final local variables or local-non-final-initialized-only-once variables.
        final String finalVar = "Hello";
        Function<String,String> func1 = y -> {return y + " "+ finalVar ;};

        // A lambda expression can appear to the right of the assignment operator.
        Calculator iCal = (x,y)-> x + y;

        // Return Context
        Calculator calculator2 = create();

        // Method Invocation Context  :   Functional Interface as Parameter
        engine((x,y)-> x / y);


        // Filter all null values from array
        filterNullValue();

        // for each test
        forEachOp();


    }
    private static void forEachOp() {
        List<String> alphabets = new ArrayList<>(Arrays.asList("aa", "bbb", "cac", "dog"));

        // looping over all elements using Iterable.forEach() method
        alphabets.forEach(s -> System.out.println(s));

        // You can even replace lambda expression with method reference because we are passing the lambda parameter as it is to the method
        alphabets.forEach(System.out::println);

        // you can even do something with lambda parameter e.g. adding a comma
        alphabets.forEach(s -> System.out.print(s + ","));

        // There is one more forEach() method on Stream class, which operates
        // on stream and allows you to use various stream methods e.g. filter() // map() etc
        alphabets.stream().forEach(System.out::println);

        // let's now only print elmements which startswith "a"
        alphabets.stream().filter(s -> s.startsWith("a")).forEach(System.out::println);

        // let's filter out only which has length greater than 2
        alphabets.stream().filter(s -> s.length() > 2).forEach(System.out::println);

        // now, let's print length of each string using map()
        alphabets.stream().mapToInt(s -> s.length()).forEach(System.out::println);

        // how about calculating sum of length of all string
        alphabets.stream().mapToInt(s -> s.length()).sum();
    }


    private static void filterNullValue() {
        List<String> cList = new ArrayList<String>();
        cList.add("United States of America");
        cList.add("Ecuador");
        cList.add("Denmark");
        cList.add(null);
        cList.add("Seychelles");
        cList.add("Germany");
        cList.add(null);

        System.out.println("<!-----Original list with null values-----!>");
        System.out.println(cList + "\n");

        List<?> mixedTypeList = Arrays.asList("a","b","",1,2,3,true,false,null);
        Set<?> filteredResult = mixedTypeList.stream().filter(obj -> obj != null).collect(Collectors.toSet());
        System.out.println("hmm: " + filteredResult);


        // EXAMPLE #1 = Filter Null Values from a Stream Using 'Lambda Expressions'
        List<String> result = cList.stream().filter(str -> str != null && str.length() > 0).collect(Collectors.toList());
        System.out.println("<!-----Result after null values filtered-----!>");
        System.out.println(result + "\n");

        // EXAMPLE #2 = Filter Null Values from a Stream Using 'Method Reference'
        List<String> nonNullResult = cList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        System.out.println("<!-----Result after null values filtered using nonNull-----!>");
        System.out.println(nonNullResult + "\n");

        // EXAMPLE #3 = Filter Null Values after Map intermediate operation
        List<String> mapNullResult = cList.stream().map(s -> s).filter(str -> str != null && str.length() > 0).collect(Collectors.toList());
        System.out.println("<!-----Result after null values filtered using Map intermediate operation-----!>");
        System.out.println(mapNullResult);
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
