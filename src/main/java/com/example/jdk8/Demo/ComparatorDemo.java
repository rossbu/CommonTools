package com.example.jdk8.Demo;

import com.example.pojo.Person;

import java.util.*;

/**
 * @author tbu
 */
public class ComparatorDemo {

    public static void main(String[] args) {


        // sort 0
        TreeSet<String> tree_set = new TreeSet<String>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        tree_set.add("G");
        tree_set.add("4");
        tree_set.add("E");
        tree_set.add("E");
        tree_set.add("S");
        tree_set.add("K");
        System.out.println("Set before using the comparator: "+ tree_set);
        System.out.println("The elements sorted in descending"+ "order:");
        for (String element : tree_set) {
            System.out.print(element + " ");
        }


        List<Person> personList = Person.createShortList();

        // Sort 1  : anonymous impl
        Comparator<Person> c = new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return p1.getSurName().compareTo(p2.getSurName());
            }
        };
        Collections.sort(personList, c);

        System.out.println("=== Sorted SurName ===");
        for (Person p : personList) {
            p.printName();
        }

        // Sort 2 , lambda
        System.out.println("=== Sorted SurName Lambda ===");
        Collections.sort(personList,
                (Person p1, Person p2) -> p1.getSurName().compareTo(p2.getSurName())
        );

        for (Person p : personList) {
            p.printName();
        }

        // sort 3 : class reference
        List<String> names2 = Arrays.asList("peter", null, "anna", "mike", null, "xenia");
        // Method References should be assignable to a FunctionalInterface
        Comparator<String> cmp = String::compareTo;

        Comparator<String> c1 = Comparator.nullsLast(String::compareToIgnoreCase);
        Comparator<String> c2 = Comparator.nullsLast(Comparator.naturalOrder());
        names2.sort(c1);
        System.out.println(names2);
        names2.stream().filter((a) -> a.startsWith("p")).forEach(System.out::println);




    }
}
