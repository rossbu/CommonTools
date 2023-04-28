package com.demo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.pojo.Person;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import static java.util.Arrays.asList;

/**
 * <p>
 * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/zip.i.png" alt="">
 * <p>

 * @author tbu
 *
 */
class Something {

  String startsWith(String s) {
    return String.valueOf(s.charAt(0));
  }
}

/**
 * @FunctionalInterface is informative can be omitted An interface with only one abstract method is
 * a functional interface
 */
@FunctionalInterface
interface ConvertFI<F, T> {
  abstract T convert(F from); // abstract can be omitted
}

@FunctionalInterface
interface TestFI {
  boolean test(int num);
}

/** sometimes, you just want to code some simple things, here we go. */
public class NoBrainCoding {
    public static <T extends Object & Comparable<? super T>> T max(
            Collection<? extends T> coll) {
        return null;
    }

  public static void main(String[] args) {

      Predicate<String> filteredByTermPredicate =
     Pattern.compile("^slug(:+)$").asPredicate();

    List mylist = List.of();
    String resultme = StringUtils.join(List.of(), " OR ");
    System.out.println("result me is:" + resultme.equals("") + ":sthhere");
    //    System.out.println(" (sth here |" + mylist.get(0) + "| sth there )");

    System.out.println();
    System.out.println();
    System.out.println();


    String regex = "^[1,4]$";
    String json = "{\n" + "  \"sth\" : \"json\"\n" + "}";
    Thread thread =
        new Thread(
            new Runnable() {

              @Override
              public void run() {
                System.out.println("print sth");
              }
            });

    List.of("1", "2", "e", "s");
    Map<Integer, String> integerStringMap = Map.of(1, "2");
    Set<Person> people = Set.of(new Person()); // FIXME: 6/2/2020

    // random coding
    List<? extends Number> foo1 =
        new ArrayList<Number>(); // Number "extends" Number (in this context)
    List<? extends Number> foo2 = new ArrayList<Integer>(); // Integer extends Number
    List<? extends Number> foo3 = new ArrayList<Double>(); // Double extends Number

    // calling filter method with a lambda expression
    // as one of the param
    List<Integer> myList = new ArrayList<Integer>();
    myList.add(1);
    myList.add(4);
    myList.add(6);
    myList.add(7);
    Collection<Integer> results = filter(n -> n > 5, myList);


      List<String> resultAfterTransform = Lists.transform(myList, String::valueOf);
      resultAfterTransform.forEach(System.out::println);


      // demo  usage of "::"  for instance methods and constructor
    ConvertFI<String, Integer> converter = Integer::valueOf;
    Integer convertedValue = converter.convert("123");
    System.out.println(convertedValue); // 123

    Something something = new Something();
    ConvertFI<String, String> converter1 = something::startsWith;
    String converted1 = converter1.convert("Java");
    System.out.println(converted1); // J

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

    String postfix = "postfix";
    Boolean me = null;
    Optional<Boolean> me1 = Optional.ofNullable(me);
    System.out.println("postfix = " + postfix);
    Validate.isTrue(postfix != null, "the expression postfix require not null");
    File f;
    if (!ObjectUtils.isEmpty(me)) {}
  }

  public static List<Integer> filter(TestFI testNum, List<Integer> listItems) {
    System.out.println("testNum = " + testNum + ", listItems = " + listItems);
    List<Integer> result = new ArrayList<Integer>();
    for (Integer item : listItems) {
      if (testNum.test(item)) {
        result.add(item);
      }
    }
    return new ArrayList<>();
  }

  public static void hashCodeTest() {
    for (int i = 0; i < 5; i++) {
      // if I remove the third parameter, it works fine
      System.out.println(Objects.hash("getDemoCache", "1", new int[] {1, 2}));
    }
  }

  public static void demoFunction1() {
    //        https://benjiweber.co.uk/blog/2015/01/14/implicit-conversions-with-identity-functions/
    List<Library> libraries =
        asList(
            Library.library(Book.book("The Hobbit"), Book.book("LoTR")),
            Library.library(Book.book("Build Quality In"), Book.book("Lean Enterprise")));

    /*
             you can't use Library::books, because flatMap requires a function that returns a Stream
            <R> Stream<R> map(Function<? super T, ? extends R> mapper)
            <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);
    */
    libraries.stream()
        .flatMap(library -> library.books().stream())
        .map(Book::name)
        .forEach(System.out::println);

    /*
           Library::books is equivalent to a Function<Library, List<Book>>, so passing it to the f() method implicitly converts it to that type.
           andThen method which returns a new function which composes the two functions.
    */
    libraries.stream()
        .flatMap(f(Library::books).andThen(Collection::stream))
        .map(Book::name)
        .forEach(System.out::print);
  }

  interface Library {
    List<Book> books();

    static Library library(Book... books) {
      return () -> asList(books);
    }
  }

  interface Book {
    String name();

    static Book book(String name) {
      return () -> name;
    }
  }

  interface User {
    Integer getId();
  }

  void test(List<User> users) {
    Map<Integer, User> userMap =
        users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
  }

  public static <T, R> Function<T, R> f(Function<T, R> f) {
    return f;
  }
}
