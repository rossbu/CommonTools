package com.demo.enumeration;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class EnumConstantDemo {

    public static void main(String... args) throws Exception {


        Localetype[] values = getEnumValues(Localetype.class);
        System.out.println(Arrays.toString(values));

        //alternatively
        Method method = Localetype.class.getDeclaredMethod("values");
        Object obj = method.invoke(null);
        System.out.println(Arrays.toString((Object[]) obj));
    }

    private static <E extends Enum> E[] getEnumValues(Class<E> enumClass)
            throws NoSuchFieldException, IllegalAccessException {
        Field f = enumClass.getDeclaredField("$VALUES");
        f.setAccessible(true);
        System.out.println(Modifier.toString(f.getModifiers()));
        System.out.println(f);
        Object o = f.get(null);
        return (E[]) o;
    }

    private static enum MyEnum {
        A, B, C
    }

    private static enum Localetype {

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

        public static Localetype fromValue(String v) {
            for (Localetype c : Localetype.values()) {
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

    private static enum Localetype2 {
        EN_US("en_US", "default"),
        DE_DE("de_DE", "notdefault");
        //    public static final Localtype EN_US= new Localtype("en_US","default");
        //    public static final Localtype DE_DE = new Localtype("de_DE","notdefault");
        private final String defaulttest;
        private final String notdefault;

        Localetype2(String testdefault, String notdefault) {
            this.notdefault = testdefault;
            defaulttest = notdefault;
        }
    }

    static enum WithMultiFields {
        CAR("Long Trip", 4), BIKE("Short Trip", 2);
        String tripType;
        int wheelCount;

        WithMultiFields(String tripType, int wheelCount) {
            this.tripType = tripType;
            this.wheelCount = wheelCount;
        }
    }
    static enum WithIntField {
        ONE(1), TWO(2);
        int i;
        WithIntField(int i) {
            this.i = i;
        }
        @Override
        public String toString() {
            return "WithIntField{" +
                    "i=" + i +
                    "; name="+name()+"}";
        }
    }

    static enum EnumWithPOJO {

        RED("Rose", new Color((short) 255, (short) 0, (short) 0)), GREEN("Leaf", new Color((short) 0, (short) 255, (short) 0)), BLUE("Sky", new Color((short) 0, (short) 0, (short) 255));
        String example;
        Color description;

        EnumWithPOJO(String example, Color description) {
            this.example = example;
            this.description = description;
        }
    }

    static class Color {
        short red;
        short green;
        short blue;

        public Color(short red, short green, short blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }
    }
}
