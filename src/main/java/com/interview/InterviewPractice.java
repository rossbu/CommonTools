package com.interview;

import com.pojo.Gender;
import com.pojo.Person;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InterviewPractice {

    public static void main(String[] args) {
        /*
         req-1: given a list of persons, consider adding the persons to map where the key is the person id and the values are the person themselves.
         Then sort the list by id, and print the results.
         hint -1 :
         use java 8 comparator to compare
         Java 8 has added static methods comparingByKey and comparingByValue to Map.Entry. Printing the elements sorted by key is shown in Sorting Map elements by key and printing.
         */
        sortListByJava8MapComparator();




    }

    private static void sortListByJava8MapComparator() {
        // prepare test data
        Person bu = new Person.Builder().age(10).surName("bu").build();
        Person oldman = new Person.Builder().age(100).surName("oldman").build();
        Person dong = new Person.Builder().age(40).surName("Dong").gender(Gender.FEMALE).build();
        List<Person> people = Arrays.asList(bu,oldman,dong);

        // streaming the list and map to key-value and sort and print
        Map<Integer, Person> personMap = people.stream().filter(e -> e.getSurName() != null).collect(Collectors.toMap(Person::getAge, Function.identity()));
        personMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(System.out::println);
    }
}
