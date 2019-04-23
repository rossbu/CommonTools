package com.example.jdk8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by tbu on 4/12/2017.
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * Lambda Form	    List primeNumbers = ReferenceToStaticMethod.testPredicate(numbers, a -> ReferenceToStaticMethod.isPrime(a));
 * Method Reference	List primeNumbers = ReferenceToStaticMethod.testPredicate(numbers, ReferenceToStaticMethod::isPrime);
 * <p>
 * <p>
 * Method Reference is NOT a Predicate  such as  String::isEmpty is NOT  a Predicate
 */
public class ReferenceMethodDemo {
    /**
     * @param args the command line arguments
     */
    public static void main(String... args) {

        // BU1
        List numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15, 16);
        List primeNumbers = ReferenceMethodDemo.findPrimeNumbers(numbers, (number) -> ReferenceMethodDemo.isPrime((int) number));
        System.out.println("Prime Numbers are " + primeNumbers);

        // BU02
        List squaredNumbers = ReferenceMethodDemo.findSquareRoot(numbers, Integer::new);
        System.out.println("Square root of numbers = "+squaredNumbers);

        // very shocking code below
        Predicate<String> p1 = s -> s.isEmpty();                        // Predicate = lambda
        Predicate<String> p2 = String::isEmpty;                     // Predicate = Method Reference  but Predicate<Integer> is not compiled  since there is no String::isEmpty method with Integer argument
        Function<String, Boolean> f1 = String::isEmpty;                 // Function = Method Reference
//        Function<String, Boolean> f2 = (Function<String, Boolean>) p1;  // cannot cast Predicate to Function, it's no longer about inference
        System.out.println("p1: " + p1.test("abc") );
        System.out.println("p2: " + p2.test(null) );
    }
    /**
     * @param list
     * @param predicate
     * @return
     */
    public static List findPrimeNumbers(List list, Predicate predicate) {
        List sortedNumbers = new ArrayList();
        list.stream()
                .filter((i) -> (predicate.test(i)))
                .forEach((i) -> {
                    sortedNumbers.add(i);
                });
        return sortedNumbers;
    }

    private static List findSquareRoot(List list, Function<Integer, Integer> f) {
        List result = new ArrayList();
        list.forEach(x -> result.add(Math.sqrt(f.apply((Integer) x))));
        return result;
    }

    public static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }

    public static boolean isPrime(int number) {
        if (number == 1) {
            return false;
        }
        for (int i = 2; i < number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }


}