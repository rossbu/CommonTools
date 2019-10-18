package com.example.jdk8.Demo;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class PredicateDemo {

    public static Predicate<String> hasLengthOf10 = new Predicate<String>() {
        @Override
        public boolean test(String t)
        {
            return t.length() >= 10;
        }
    };
    static Predicate<String> containsLetterA = p -> { return p.contains("A"); };

    static Predicate<String> nonNullPredicate = Objects::nonNull;

    static Predicate<Integer> isOdd = n -> n % 2 != 0;

    static IntPredicate isEvenPredicate = n -> n % 2 == 0;

    Predicate<Integer> isPrimeNumber = n -> n % 2!=0;

    //BiPredicate performs the defined operation and returns boolean value
    static BiPredicate<Integer, String> isQualifiedUser = (i, s) -> i > 20 && s.startsWith("R");

    public static void main(String[] args) {
        System.out.println("13's square foot is : " + Math.sqrt(13)); // 3.6, so < 3.6 is 3
        String testString = "Bu And KUn";
        System.out.println(hasLengthOf10.or(containsLetterA).test(testString));
        System.out.println(hasLengthOf10.or(containsLetterA).negate().test(testString));
        System.out.println(hasLengthOf10.and(nonNullPredicate).test(testString));
        System.out.println(isEvenPredicate.test(6));
        System.out.println("17 is prime? : " + isPrime2(17));


        System.out.println(isQualifiedUser.test(10,"Ram"));
        System.out.println(isQualifiedUser.test(30,"Shyam"));
        System.out.println(isQualifiedUser.test(30,"Ram"));
    }


    /**
     * A prime number is a number which is divisible by only two numbers: 1 and itself. So, if any number is divisible by any other number, it is not a prime number.  so 1,3,5,7,11,13 are prime numbers
     * @param num
     */
    public void isPrime(Integer num){
        boolean flag = false;
        for(int i = 2; i <= num/2; ++i)
        {
            // condition for nonprime number
            if(num % i == 0)
            {
                flag = true;
                break;
            }
        }
        if (!flag) {
            System.out.println(num + " is a prime number.");
        } else {
            System.out.println(num + " is not a prime number.");
        }
    }
    public static boolean isPrime(int number) {
        return !IntStream.rangeClosed(2, number/2).anyMatch(i -> number%i == 0);
    }
    public static boolean isPrime2(int i) {
        IntPredicate isDivisible = index -> i % index == 0;
        return i > 1 && IntStream.range(2, i/2).noneMatch(isDivisible);
    }
}


