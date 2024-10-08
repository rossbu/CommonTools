package com.demo.lombok;
;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
class Person {
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int age;
}

public class LombokDemo {
    public static void main(String[] args) {
        Person person = new Person();
        person.name("John").age(25);

        System.out.println("Name: " + person.name());
        System.out.println("Age: " + person.age());
    }
}