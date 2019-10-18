package com.demo.enumeration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;


/**
 Exception in thread "main" com.fasterxml.jackson.databind.exc.InvalidFormatException: Cannot deserialize value of type `example.Localetype` from String "en_US": value not one of declared Enum instance names: [EN_US, DE_DE]
 */
public class EnumValueDemo {

    public static void main(String... args) throws IOException, ClassNotFoundException {
        String json = "{\"languagelocale\" : \"en_US\"}";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);// true or false , both return EN_US
        Userinfo user = mapper.readValue(json, Userinfo.class);
        System.out.println("result : " + user.getLanguagelocale().valueof());
        System.out.println(Localetype.class.getEnumConstants()[0].ordinal());

        Class clz = Class.forName(Localetype.class.getName());

        Object o = Enum.valueOf(clz, "EN_US");
        System.out.println("Enum.valueOf test : " + o);


        // to get INSTANCE EN_US by the value "en_US"
        Localetype fromvalue = Localetype.fromValue("en_US");
        System.out.println("fromValue test : " + fromvalue);


        Localetype fromvalue2 = Localetype.fromValue("EN_US"); // throw Exception in thread "main" java.lang.IllegalArgumentException: EN_US
        System.out.println("fromValue2 test : " + fromvalue2);
    }
}

// given a generated  class
class Userinfo{
    Localetype languagelocale;
    public Localetype getLanguagelocale() {
        return languagelocale;
    }

    public void setLanguagelocale(Localetype value) {
        this.languagelocale = value;
    }
}

/// a generated class  by xjc or other tools like jaxb2 maven plugin. unmodifiable.
enum Localetype {

    EN_US("en_US"),
    DE_DE("de_DE");
//    public static final Localtype EN_US= new Localtype("en_US");
//    public static final Localtype DE_DE = new Localtype("de_DE");

    private final String value;
    public String valueof() {
        return value;
    }

    Localetype(String v) {
        value = v;
    }

    static Localetype fromValue(String value){
        return Arrays.stream(Localetype.values()).filter(e -> e.valueof().equals(value)).findFirst().get();
    }
    // return INSTANCE EN_US by value en_US
    public static Localetype fromValuejdk5(String v) {
        for (Localetype c: Localetype.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
    @Override
    public String toString() {
        return this.valueof();   // with valueof it returns en_US , and the deserialization works.
//        return name();  //  name() EN_US
    }
}

enum Monster {
    ZOMBIE(Userinfo.class, "zombie"),
    ORK(Ork.class, "ork"),
    WOLF(Wolf.class, "wolf");
    private final Class<? extends Object> entityClass;
    private final String                  entityId;
    Monster(Class<? extends Object> entityClass, String entityId) {
        this.entityClass = entityClass;
        this.entityId = "monster:" + entityId;
    }
    public Class<? extends Object> getEntityClass() { return this.entityClass; }
    public String getEntityId() { return this.entityId; }
    public Object create() {
        try { return entityClass.newInstance(); }
        catch (InstantiationException | IllegalAccessException e) { throw new InternalError(e); }
    }
}

class Ork extends Object {
    String name ;
    String age;
}

class Wolf extends Object {
    boolean isBadAnimal;
    int firePower;
}
