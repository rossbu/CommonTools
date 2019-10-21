package com.jdk.jdk11;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * JDK11 enhances java.util.function.Predicate with a static not() method
 */
public class PredicateTest {
    public static void main(String[] args) {
        String[] names = { "TEST", "MARY", " ", "" };
        List loweCaseList =
                Stream.of(names)
                .filter(Predicate.not(String::isBlank))
                .collect(Collectors.toList());
    }
}
