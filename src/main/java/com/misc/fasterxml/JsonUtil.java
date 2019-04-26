package com.misc.fasterxml;

/**
 * Created by tbu on 12/13/2017.
 */

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/*
    Demonstrate serialization/deserialization of Java objects using Jackson library
 */
public class JsonUtil {
    public static void testUnknowEnumJsonValue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
        objectMapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        String jsonString = "\"FullTime\"";
        System.out.println("-- Json string to deserialize --");
        System.out.println(jsonString);
        EmployeeType type = objectMapper.readValue(jsonString, EmployeeType.class);
        System.out.println(type);
        System.out.println("-- after deserialization -- \n");


        jsonString = "\"remote\"";
        System.out.println("-- Json string to deserialize --");
        System.out.println(jsonString);
        EmployeeType type2 = objectMapper.readValue(jsonString, EmployeeType.class);
        System.out.println(type2);
        System.out.println("-- after deserialization -- \n");



        ObjectMapper objectMapper2 = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        ObjectReader reader = objectMapper2.readerFor(Status.class);

        Status status = reader.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue("\"notReady\"");
        System.out.println("status name: " + status.toString() + " and status name is : " + status.name() + "\n");  // NOT_READY
    }

    public static void main(String[] args) throws IOException {
//        testUnknowEnumJsonValue();
//        testJsonOps();
//        String[] strings =  "en_US".split("_");
//        System.out.println(strings[0].toLowerCase() + "_" + strings[1]);

    }



    private static void testJsonOps() {
        Person person = new Person("Jon Snow", 22);

        String jsonString = null;
        Person p = null;
        try {
            ObjectMapper mapper = new ObjectMapper();

            // serialize Object - Convert Person Object to JSON String
            jsonString = mapper.writeValueAsString(person);
            System.out.println("Converting Person Object to JSON String : \n" + jsonString);

            // unserialize Object - Convert JSON String back to Person Object
            p = mapper.readValue(jsonString, Person.class);
            System.out.println("\nConverting JSON String to Person Object : \n" + p.toString());

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Person {

    private String name;
    private int age;
    private Student student;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;

        student = new Student(name.replaceAll("\\s+", "_") + "_" +
                String.valueOf(age));
    }

    private Person() {
    }

    @Override
    public String toString() {
        return Arrays.asList(name, String.valueOf(age), student.toString())
                .toString();
    }

    public Student getStudent() {
        return student;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
enum Status {
    EN_US("en_US"),
    DE_DE("de_DE"),
    READY("ready"),
    NOT_READY("notReady"),
    NOT_READY_AT_ALL("notReadyAtAll");

    private final String formatted;

    Status(String formatted) {
        this.formatted = formatted;
    }

    @Override
    public String toString() {
        return formatted;
    }
}

enum EmployeeType {
    FullTime,
    PartTime,
    @JsonEnumDefaultValue
    Contractor;
}

class Student {

    private String id;
    private Set<String> subjects;

    public Student(String id) {
        this.id = id;
        subjects = new HashSet<>(Arrays.asList("Maths", "Science"));
    }

    private Student() {
    }

    @Override
    public String toString() {
        return Arrays.asList(id, subjects.toString()).toString();
    }

    public String getId() {
        return id;
    }

    public Set<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<String> subjects) {
        this.subjects = subjects;
    }

    public void setId(String id) {
        this.id = id;
    }

}
