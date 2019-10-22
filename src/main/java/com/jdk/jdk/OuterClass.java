package com.jdk.jdk;

/**
 * What is a Static Class in Java?
 * you might have noticed how we had never made our top level class as static. The reason is plain and simple. Because in Java it can’t be done!
 * Only a class that is nested can be turned into a static class in Java.
 *
 * That being said we must understand that using static classes is simply great for grouping purposes.
 * Because Java allows us to group classes that are useful to each other, we group them just to keep them together under a single keyword static.
 *
 * feature:
 *  A static Class in Java can have a static method:  OuterClassName.InnerClassName.methodname();
 *  Static methods are generally used when you wish to change a static variable.
 *  Static methods and variables are loaded only once, and only one copy of it is created when you run the program.
 *  We can make any variable, method, block or nested class static by preceding the keyword static.
 *  Only a static method can change the value of static variable.
 *  You cannot use a static variable from a non-
 *  The top level class can’t be static.
 *  Nested classes can be both static and non-static.
 *  Both these classes have special ways to call their respective methods.
 *  While a nested static class can be called without instantiating, the nested non-static one needs to be called by creating two objects in a fashion mentioned above. New OuterClass().newInnerClass().methodname();
 *
 *
 *  http://dumbitdude.com/static-class-in-java/
 */
public class OuterClass {

    private static String name = "Mohammad";

    // non-static nested class
    public class NonStaticNestedClass {
        public void say() {
            System.out.println("i am in non static nested class, and HI. ");
        }
    }

    // static nested class
    public static class StaticNestedClass {
        private int a;

        public int getA() {
            return this.a;
        }

        public String getName() {
            return name;
        }
    }
}
class StaticNestedClassTest {

    public static void main(String[] args) {

        // creating instance of static nested class
        OuterClass.StaticNestedClass nested = new OuterClass.StaticNestedClass();

        // creating instance of non-statuc nested class
        OuterClass.NonStaticNestedClass nonStaticNestedClass = new OuterClass().new NonStaticNestedClass();
        nonStaticNestedClass.say();

        //accessing outer class static member
        System.out.println(nested.getName());    // Printing "Mohammad"


    }
}
