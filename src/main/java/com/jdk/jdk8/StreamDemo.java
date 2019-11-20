package com.jdk.jdk8;

import com.google.common.collect.Streams;
import com.pojo.CarInfo;
import one.util.streamex.StreamEx;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Created by tbu on 4/4/2017.
 * The Java 8 Collectors class provides useful implementations, which can be used by Streams' collect() method.
 * Some of these operations presented here include computing sums, averages, maximums, and minimums.
 * Partitioning a List based on a predicate returns a pair of Lists enclosed in a Map.
 * <p>
 * <p>
 * https://dzone.com/articles/using-java-collectors
 */
public class StreamDemo {

    public static void main(String[] args) {

        List<Integer> numbers = boxedStream();

        streamBuilder();

//        skipFirstElementInStream();
//
//        reduce(numbers);
//
//        averagingInt(numbers);
//
//        maximumAndMinimum(numbers);
//
//        summarizingInt(numbers);
//
//        partitioningBy(numbers);
//
//        peek();

//        mapTest();

//        flatmapTest();
//
//        filterList2BasedOnList1();

//        filterPrimeNumber();

//        distinctByProperty();

//        streamClosed();

        streamExZip();

        guavaStreamZip();


    }

    private static void guavaStreamZip() {
        List<String> carBrands = Arrays.asList("BMW", "HONDA", "Mecedes", "Volvo", "Accura");
        List<CarInfo> cars = Arrays.asList(
                new CarInfo("BMW", 1995, "200", "300", 200),
                new CarInfo("HONDA", 1996, "200", "300", 200),
                new CarInfo("Datsun", 1495, "200", "300", 200),
                new CarInfo("Volvo", 1695, "200", "300", 200),
                new CarInfo("Mitustishi", 1955, "200", "300", 200)
        );

        Streams.zip(carBrands.stream(), carBrands.stream(), (a , b) ->{return a;})
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /*
    # Stream is closed after that was used once        Issue: expectation: throw IllegalStateException: Stream has been operated upon or closed
        Solution: so use Supplier, Simply put create a new Stream whenever you need to use the stream.

     */
    private static void streamClosed() {



        Stream<String> streamTest = Stream.of("A","B","C");
        Optional<String> anyResult = streamTest.findAny();
        System.out.println(anyResult.get());
        Optional<String> firstReuslt = streamTest.findFirst(); // throw exception that the Stream was closed after findAny() operation

        Supplier<Stream<String>> streamSupplier = () -> Stream.of("A","B","C");
        Optional<String> anyResultSupplied = streamSupplier.get().findAny();
        System.out.println(anyResultSupplied.get());
        Optional<String> secondresult = streamSupplier.get().findFirst();
        System.out.println(secondresult.get());
    }

    /**
     *
     Stream.flatMap, as it can be guessed by its name, is the combination of a map and a flat operation. That means that you first apply a function to your elements, and then flatten it.
     Stream.map only applies a function to the stream without flattening the stream.
     To understand what flattening a stream consists in, consider a structure like [ [1,2,3],[4,5,6],[7,8,9] ] which has "two levels".
     Flattening this means transforming it in a "one level" structure : [ 1,2,3,4,5,6,7,8,9 ].

     */
    private static void flatmapTest() {
        List<String> list = Arrays.asList("bu","tianshi","shi","hI","HOW ","are","you","!");
        list.stream().flatMap(e -> Stream.of(e)).forEach(System.out::print);

        // Creating a list of Prime Numbers
        List<Integer> PrimeNumbers = Arrays.asList(5, 7, 11,13);

        // Creating a list of Odd Numbers
        List<Integer> OddNumbers = Arrays.asList(1, 3, 5);

        // Creating a list of Even Numbers
        List<Integer> EvenNumbers = Arrays.asList(2, 4, 6, 8);

        List<List<Integer>> listOfListofInts =  Arrays.asList(PrimeNumbers, OddNumbers, EvenNumbers);

        System.out.println("The Structure before flattening is : " +  listOfListofInts);

        // Using flatMap for transformating and flattening.
        List<Integer> listofInts  = listOfListofInts.stream()
                .flatMap(e -> e.stream())
                .collect(Collectors.toList());
        System.out.println("The Structure after flattening is : " + listofInts);

        System.out.println("Comparing with comparing");
        Stream.of(list).flatMap(Collection::stream).sorted( Comparator.comparing( String::length)).collect(toList()).forEach(System.out::println);
        System.out.println("Comparing with comparingInt");
        Stream.of(list).flatMap(Collection::stream).sorted( Comparator.comparingInt(String::length)).collect(toList()).forEach(System.out::println);





    }

    /**
        Note:
            map takes a 'Function<inputObject,OutputObject> + the function that converts the InputObject to OutputObject' .
            for example:  input Person and map it to Student.
             Stream<Student> students = persons.stream()
             .filter(p -> p.getAge() > 18)
             .map(new Function<Person, Student>() {
                @Override
                public Student apply(Person person) {
                return new Student(person);
                }
            });

            or
             Stream<Student> students = persons.stream()
             .filter(p -> p.getAge() > 18)
             .map(Student::new);

            or map then reduce

             double priceSum2 = items.stream().map(Item__1::getTotalPrice).reduce(0.0, Double::sum);
     */
    private static void mapTest() {
        // Creating a list of Strings
        List<String> list = Arrays.asList("Geeks", "FOR", "GEEKSQUIZ", "Computer", "Science", "gfg");

        // Using Stream map(Function mapper) and displaying the length of each String
        list.stream().map(str -> str.length()).forEach(System.out::println);


        // Creating a list of Integers
        List<String> list1 = Arrays.asList("geeks", "gfg", "g", "e", "e", "k", "s");

        // Using Stream map(Function mapper) to convert the Strings in stream to UpperCase form
        List<String> answer = list1.stream()
                .map(String::toUpperCase).
                collect(Collectors.toList());

        // displaying the new stream of UpperCase Strings
        System.out.println(answer);

        Optional.ofNullable(list1).map(Collection::stream);


    }

    private static void streamBuilder() {
        // Using Stream builder()
        Stream.Builder<String> builder = Stream.builder();

        // Adding elements in the stream of Strings
        Stream<String> stream = builder.add("Geeks")
                .add("for")
                .add("Geeks")
                .add("GeeksQuiz")
                .build();

        // Displaying the elements in the stream
        stream.forEach(System.out::println);
    }


    private static <T, R> Collector<T, ?, Stream<T>> removeDuplicateBy3(Function<T, R> keyExtractor) {
        return Collectors.collectingAndThen(
                toMap(
                        keyExtractor,
                        t -> t,
                        (t1, t2) -> t1
                ),
                (Map<R, T> map) -> map.values().stream()
        );
    }

    /**
     * ConvertFI<String, Integer> converter = Integer::valueOf;
     * @param keyExtractor
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T,R> Predicate<T> removeDuplicateBy2(Function<? super T, R> keyExtractor) {
        Set<Object> set = ConcurrentHashMap.newKeySet();
        Predicate<T> tPredicate = t -> {
            R value = keyExtractor.apply(t);
            return set.add(value);
        };
        return tPredicate;
    }

    /**
     * The easiest way to remove duplicate entries from the given array is, create TreeSet object and add array entries to the TreeSet. Since the set does NOT support duplicate entries, you will get only unique elements left with TreeSet.
     *
     * and There is a new method added into ConcurrentHashMap in JDK 8, newKeySet(), which allows you to create a concurrent hash set backed by a concurrent hash map.
     * Since map doesn't allow duplicate keys, it can be used as a set, as long as you only care for keys or just one element. That's why Java designers are added newKeySet() method to convert a map to set
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> removeDuplicateBy(Function<? super T, ?> keyExtractor) {
        Set<Object> set = ConcurrentHashMap.newKeySet();
        Predicate<T> tPredicate = t -> {
            Object value = keyExtractor.apply(t);
            return set.add(value);
        };
        return tPredicate;
    }

    private static void distinctByProperty() {

        List<CarInfo> cars = Arrays.asList(
                new CarInfo("BMW", 1995, "200", "300", 200),
                new CarInfo("HONDA", 1996, "200", "300", 200),
                new CarInfo("Datsun", 1495, "200", "300", 200),
                new CarInfo("Volvo", 1695, "200", "300", 200),
                new CarInfo("Volvo", 1489, "4589", "300", 78),
                new CarInfo("Mitustishi", 1955, "200", "500", 462),
                new CarInfo("Mitustishi", 1955, "200", "300", 200)
        );
        // java 8
//        cars.stream().collect(Collectors.toCollection(() -> new TreeSet<>((p1, p2) -> p1.getBrand().compareTo(p2.getBrand())))).forEach(System.out::println);
//        Predicate<CarInfo> predicate = removeDuplicate(CarInfo::getBrand);

        cars.stream().filter(removeDuplicateBy(CarInfo::getBrand)).collect(toList()).stream().forEach(System.out::println);
        cars.stream().filter(removeDuplicateBy2(CarInfo::getBrand)).collect(toList()).stream().forEach(System.out::println);
        cars.stream().collect(removeDuplicateBy3(CarInfo::getBrand)).collect(toList()).stream().forEach(System.out::println);

        // java 8
//        Collector<CarInfo, Object, ArrayList<CarInfo>> collector = Collectors.collectingAndThen(
//                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(CarInfo::getBrand))),
//                ArrayList::new);
//        cars.stream().collect(collector).stream().forEach(System.out::println);

        // java 8
        Collector<CarInfo, ?, Map<String, CarInfo>> carInfoMapCollector = toMap(CarInfo::getBrand, t -> t, (t, v) -> t);
        cars.stream().collect(carInfoMapCollector).values();

        // streamEx
        StreamEx.of(cars).distinct(CarInfo::getBrand).toList().forEach(System.out::println);

        // vavr
        io.vavr.collection.List<CarInfo> carInfos1 = io.vavr.collection.List.ofAll(cars);
        carInfos1.distinctBy(CarInfo::getBrand).toJavaSet();

        // by Rxjava
//        io.reactivex.rxjava3.core.Observable.fromArray(cars).distinct().forEach(System.out::println);

    }

    private static void streamExZip() {
        StreamEx<String> givenNames = StreamEx.of("Leo", "Fyodor");
        StreamEx<String> familyNames = StreamEx.of("Tolstoy", "Dostoevsky");
        StreamEx<String> fullNames = givenNames.zipWith(familyNames, (gn, fn) -> gn + " " + fn);
        fullNames.forEach(System.out::println);  // prints: "Leo Tolstoy\nFyodor Dostoevsky\n"

    }

    /**
     * filter produces a new stream that contains elements of the original stream that pass a given test (specified by a Predicate).
     * try use below ways to filter list2 by elements in list1
     */
    private static void filterList2BasedOnList1() {
        List<String> carBrands = Arrays.asList("BMW", "HONDA", "Mecedes", "Volvo", "Accura");
        List<CarInfo> cars = Arrays.asList(
                new CarInfo("BMW", 1995, "200", "300", 200),
                new CarInfo("HONDA", 1996, "200", "300", 200),
                new CarInfo("Datsun", 1495, "200", "300", 200),
                new CarInfo("Volvo", 1695, "200", "300", 200),
                new CarInfo("Mitustishi", 1955, "200", "300", 200)
        );

        List<CarInfo> listOutput =
                cars.stream()
                        .filter(e -> carBrands.contains(e.getBrand()))
                        .collect(Collectors.toList());
    }

    /**
     * // use skip intermediate operation to disgard the first element in the stream.
     */
    private static void skipFirstElementInStream() {
        List<String> words = Arrays.asList("Java8", "Lambdas", "In", "Action");
        List<Integer> wordLengths = words.stream()
                .map(String::length)
                .skip(1)
                .limit(2)
                .collect(toList());
    }

    private static List<Integer> boxedStream() {
        Random random = new Random();
        return random
                .ints(1, 100)
                .limit(10)
                .boxed()
                .collect(toList());
    }

    /**
     * https://stackoverflow.com/questions/34179062/intstream-foreach-method-in-java-8  , how foreach implemented
     */
    private static void filterPrimeNumber() {
        List<Integer> numbers2 = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        numbers2.stream()
                .filter(i -> i % 2 == 0)
                .distinct()
                .limit(1)
                .forEach(System.out::println);
    }


    /**
     * Summing an Integer List  given a array of number, reduce to one result like x , y -> x+y for all elements
     *
     * @param numbers
     */
    private static void reduce(List<Integer> numbers) {
        int sum = numbers
                .stream()
                .reduce(0, (x, y) -> x + y);
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

    /**
     * why bother computing sum, average, etc. separately? Just use summarizingInt() as shown. Just   Summarizing in One Shot
     *
     * @param numbers
     */
    private static void summarizingInt(List<Integer> numbers) {

        IntSummaryStatistics r = numbers.stream().collect(Collectors.summarizingInt(x -> x));
        System.out.println("r : " + r);
    }

    /**
     * Let us see how to partition a List of numbers into two lists using a criterion (such as values greater than 50):
     *
     * @param numbers
     */
    private static void partitioningBy(List<Integer> numbers) {
        Map<Boolean, List<Integer>> parts = numbers.stream().collect(Collectors.partitioningBy(x -> x > 50));
        System.out.println(numbers + " =>\n" +
                "  > 50 : " + parts.get(true) + "\n" +
                "  <=50 : " + parts.get(false) + "\n");
    }

    /**
     * Stream.peek() is a non-interfering Stream operation.
     * Non-interfering methods are those which guarantee that they will not modify the Stream’s data source during their execution.
     * Non-interfering nature is required in multi-threaded environments
     * <p>
     * <p>
     * Explanation of the code
     * PeekingInStreams class first creates an infinite stream using Stream.iterate() method by specifying logic to generate consecutive integers starting from 1.
     * Next iterate() method is pipelined to the peek() method which uses a lambda expression equivalent of the Consumer interface – (n -> System.out.println("number generated: - " + n)). This expressions simply prints the generated number.
     * Next, a filter is applied to the Stream using the Stream.filter() method. The filter’s predicate only allows even numbers to pass through using the logic n%2==0.
     * peek() is again pipelined next, and it prints the numbers obtained from the filter indicating which numbers passed the even number filter.
     * Stream.limit() method is used to restrict the infinite stream to 5 numbers.
     * Lastly, to end the pipeline of operations, Stream.count() method is invoked which is a terminal operation.
     * As expected, the output shows that only even numbers – 2,4,6,8,10 pass through the filter.
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
