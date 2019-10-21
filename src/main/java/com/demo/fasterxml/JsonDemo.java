package com.demo.fasterxml;

/**
 * Created by tbu on 12/13/2017.
 */

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.pojo.DealerWrapper;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/*



 */
public class JsonDemo {
    private final static Object SINGLETON_OBJECT = new Object();

    public static void main(String[] args) throws IOException {
        unknowEnumJsonValue();
        singleValueAsArray();
        wrapUnWrapJsonRoot();

    }

    /**
     * 1. @JsonRootName annotation is used to indicate name of the POJO that should be serialized.,  it's only responsible for change the class name to customized name specified by @JsonRootName("newname")
     * @throws JsonProcessingException
     */
    private static void wrapUnWrapJsonRoot() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);



        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        Person person = new Person();
        person.setAge(41);
        person.setName("tbu");
        String jsonString = objectMapper.writeValueAsString(person);
        System.out.println(jsonString);

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        person = new Person();
        person.setAge(41);
        person.setName("tbu");
        jsonString = objectMapper.writeValueAsString(person);
        System.out.println(jsonString);


        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        Student student = new Student();
        student.setId("001");
        student.getSubjects().add("Math");
        student.getSubjects().add("Art");
        student.getSubjects().add("Economy");
        jsonString = objectMapper.writeValueAsString(student);
        System.out.println(jsonString);
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        Student student1 = objectMapper.readValue(jsonString, Student.class);
        System.out.println(student);

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        student = new Student();
        student.setId("001");
        student.getSubjects().add("Math");
        student.getSubjects().add("Art");
        student.getSubjects().add("Economy");
        jsonString = objectMapper.writeValueAsString(student);
        System.out.println(jsonString);

        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
        student1 = objectMapper.readValue(jsonString, Student.class);
        System.out.println(student);


    }

    private static void singleValueAsArray() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        String content1 = "{\n" +
                "  \"Dealers\": [\n" +
                "    null\n" +
                "  ]\n" +
                "}";
        DealerWrapper dealerWrapper = objectMapper.readValue(content1, DealerWrapper.class);
        System.out.println(dealerWrapper.getDealers().size());
        System.out.println(dealerWrapper.getDealers().get(0) == null);

        String content2 = "{\n" +
                "    \"Dealers\": [\n" +
                "        {\n" +
                "            \"DealerNo\": 12345,\n" +
                "            \"DealerType\": \"Company\",\n" +
                "            \"DealerName\": \"telelift dealer\",\n" +
                "            \"Address\": \"123 main street\",\n" +
                "            \"City\": \"Norwalk\",\n" +
                "            \"Region\": \"CA\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        dealerWrapper = objectMapper.readValue(content2, DealerWrapper.class);
        System.out.println(dealerWrapper.getDealers().size());
        System.out.println(dealerWrapper.getDealers().get(0) == null);
        String content3 = "{\n" +
                "    \"Dealers\": " +
                "        {\n" +
                "            \"DealerNo\": 12345,\n" +
                "            \"DealerType\": \"Company\",\n" +
                "            \"DealerName\": \"telelift dealer\",\n" +
                "            \"Address\": \"123 main street\",\n" +
                "            \"City\": \"Norwalk\",\n" +
                "            \"Region\": \"CA\"\n" +
                "        }" +
                "  " +
                "}";
        dealerWrapper = objectMapper.readValue(content3, DealerWrapper.class);
        System.out.println(dealerWrapper.getDealers().size());
        System.out.println(dealerWrapper.getDealers().get(0) == null);
    }

    public static void unknowEnumJsonValue() throws IOException {
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


        jsonString = "\"remote\""; // is not defined as enum value
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


}

/**
 * had to support a very heterogeneous API, some of it had root elements, another did not. I could not find a better solution than to configure this property realtime
 */
class NewObjectMapper extends com.fasterxml.jackson.databind.ObjectMapper {
    private void autoconfigureFeatures(JavaType javaType) {
        Annotation rootAnnotation = javaType.getRawClass().getAnnotation(JsonRootName.class);
        this.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, rootAnnotation != null);
    }

    @Override
    protected Object _readMapAndClose(JsonParser jsonParser, JavaType javaType) throws IOException, JsonParseException, JsonMappingException {
        autoconfigureFeatures(javaType);
        return super._readMapAndClose(jsonParser, javaType);
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
    private Set<String> subjects = new HashSet<>();

    public Student(String id) {
        this.id = id;
        subjects = new HashSet<>(Arrays.asList("Maths", "Science"));
    }

    public Student() {
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

@JsonRootName("PersonJsonCustomizedRootName")
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

    public Person() {
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

