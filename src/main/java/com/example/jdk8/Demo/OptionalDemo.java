package com.example.jdk8.Demo;

import com.example.pojo.Person;

import java.util.Optional;


/**
 Optional classes were introduced in order to prevent NullPointerException,
 but the method get() used to retrieve the value inside the Optional might still throw a NoSuchElementException,
 so
 if(myString.isPresent()){
    doSomething(myString.get());
 }
 *
 *
 */
public class OptionalDemo {
    public static void main(String[] args) {
        Optional<String> optional =  Optional.empty();
        System.out.println(optional);

        // If you still use the get() functionality on optionals, you're effectively opening youselvies to the same problems as null checks.
        // Don't use Optional get without isPresent
        System.out.println(optional.get());

        Optional<String> petNameOptional = Optional.of("Bobby");
        String petName = "";
        if(petNameOptional.isPresent()){
            petName = petNameOptional.get();
        }

        Optional<Person> opt = Optional.empty();
        Person x = opt.orElse( new Person() );
        Person y = opt.orElseGet( Person::new ); // lazy loading for heavy resource

        // use ifpresent otherwise like Optional.ofNullable<List>.map(Collection::stream).findfirst().ifpresent ( lllambda)

    }
}
