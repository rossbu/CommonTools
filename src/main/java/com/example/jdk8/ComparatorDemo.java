package com.example.jdk8;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author tbu
 */
public class ComparatorDemo {

    public static void main(String[] args) {
                
        List<Person> personList = Person.createShortList();

        // Sort 1
        Collections.sort(personList, new Comparator<Person>() {
            public int compare(Person p1, Person p2) {
                return p1.getSurName().compareTo(p2.getSurName());
            }
        });

        System.out.println("=== Sorted SurName ===");
        for (Person p : personList) {
            p.printName();
        }

        // Sort 2
        System.out.println("=== Sorted SurName Lambda ===");
        Collections.sort(personList,
                (Person p1, Person p2) -> p1.getSurName().compareTo(p2.getSurName())
        );

        for (Person p : personList) {
            p.printName();
        }

        /////////////////////////////////////////////////////////////////////
        List<String> names2 = Arrays.asList("peter", null, "anna", "mike", "xenia");
        // Method References should be assignable to a FunctionalInterface
        Comparator<String> cmp = String::compareTo;

        Comparator<String> c1 = Comparator.nullsLast(String::compareToIgnoreCase);
        Comparator<String> c2 = Comparator.nullsLast(Comparator.naturalOrder());
        names2.sort(c1);
        System.out.println(names2);
        names2.stream().filter((a) -> a.startsWith("p")).forEach(System.out::println);


    }
}
