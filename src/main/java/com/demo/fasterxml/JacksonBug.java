package com.demo.fasterxml;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;


class Parent {

    @JsonIgnoreProperties("parent")
    public Child child;

}

class Child {

    public Parent parent;

    public String childType;

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
            property = "childType"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(name = "A", value = SubChildA.class),
            @JsonSubTypes.Type(name = "B", value = SubChildB.class),
    })
    public SubChild subChild;

}

interface SubChild {
}

class SubChildA implements SubChild {
}


class SubChildB implements SubChild {
}

public class JacksonBug {

    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        Parent p = objectMapper.readValue("{\"child\":{\"childType\":\"A\", \"subChild\": {} }}", Parent.class);
        if (!(p.child.subChild instanceof SubChildA)) {
            throw new Exception("Expected SubChildA, got " + p.child.subChild.getClass().getName());
        }

    }
}