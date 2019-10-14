package com.example.jdk5;

import com.example.pojo.Person;

import java.util.*;
import java.util.function.Function;

/**
 * Created by tbu on 4/5/2018.
    <pre>

        PECS:
            (short for "Producer extends and Consumer super") can be explained by : Get and Put Principle  , means,  you can only GET from Producer and You have to GIVE/PUT to the consumer

        PECS design pattern:
            1. use an extends wildcard when you only GET values out of a structure
            2. use a super wildcard when you only PUT values into a structure
            3. Don’t use a wildcard when you both GET and PUT.

        List<?>
        List<?>  ==== List<? extends Object>  implicitly ， when you should use this List<?> which seems useless because you can't PUT anything in the box, the only place you may use it as Type Argument, for example:
            private static void printList(List<?> list) {
                System.out.println(list);
        }
        and below code is basically useless because you can't PUT anything in there and it's empty one
        List<?> stuff = new ArrayList<>();
        testing code is in below example.


         <T> and <?>
         <T>：泛型标识符，用于泛型定义（类、接口、方法等）时，可以想象成形参。
         <?>：通配符，用于泛型实例化时，可以想象成实参。

         List < ? extends T> 是个大容器，里面的T就是家长，一旦这个‘家容器’被实例话建立一个家了，这个时候T就是这个家的大家长，别人都不可以进来了，你可以让这个大家庭里的人出来看看GET
         List < ? super T>   是个大容器，里面的T就是家里得最小辈，剩下都比他大的爸爸，爷爷奶奶，太爷爷等。这些老家伙只进不出，你可以给我东西(add)，但是你不能要我东西看(get) ,所以这些大爷们都是顾客consumer只进不出

         Example:
         The perfect example of this is the signature for Collections.copy():

            public static <T> void copy(List<? super T> dest,List<? extends T> src) -- you GET from src and PUT into dest.

         Notice how the src list declaration uses extends to allow me to pass any List from a family of related List types and still guarantee it will produce values of type T or subclasses of T. But you cannot add to the src list
         The dest list declaration uses super to allow me to pass any List from a family of related List types and still guarantee I can write a value of a specific type T to that list. But it cannot be guaranteed to read the values of specific type T if I read from the list.


 </pre>


 */
public class PecsDemo {
    PecsDemo() {
    }

    public static void main(String[] args) {
        PecsDemo demo = new PecsDemo();

        uselessWildCardExample();
        demo.testExtends();
        demo.testSuper();

    }

    private static void uselessWildCardExample() {
        List<?> stuff = new ArrayList<>();  // useless if you use this way
        List<? extends Object> stuff2 = new ArrayList<>(); //  useless as same as above.
//         stuff.add("abc");
//         stuff.add(new Object());
//         stuff.add(3);
        printList(Arrays.asList("test", "?", "as", "argument", "type"));

        Function<? extends Object, ? super Integer> function1 = a -> a.hashCode();
        // Object abc = function1.apply("abc");// COMPILATION ERROR
    }


    void testExtends() {
        List<? extends Programmer> programmerList = null;
        programmerList = Arrays.asList(
                new Programmer("tbu"),
                new JavaProgrammer("wen"),
                new JavaArchitectProgrammer("john")
        );
        Programmer programmer = programmerList.get(0);
        Worker worker = programmerList.get(1);
        Person person = programmerList.get(2);
        // so you can 'initialize' programmer list with it's subclasses, But after that you WON'T be able to add anything. so only one change, don't miss it.
    }


    void testSuper() {
        List<? super Programmer> programmerSuperList = null;
        programmerSuperList = Arrays.asList(
                new Programmer("tbu"),
                new JavaProgrammer("wen"),
                new JavaArchitectProgrammer("john")
        );
        Person person = new Person("aperson");
        Worker worker = new Worker("aworker");
        Programmer programmer = new Programmer("tbu");
        JavaProgrammer javaProgrammer = new JavaProgrammer("wen");
        JavaArchitectProgrammer javaArchitectProgrammer = new JavaArchitectProgrammer("John");
        programmerSuperList = new ArrayList<Worker>(); // at this point the 'lower Type' of list is defined as worker and from now on you can't get anything out of the box.
        programmerSuperList.add(programmer);
        programmerSuperList.add(javaProgrammer);
        programmerSuperList.add(javaArchitectProgrammer);

//        Programmer p = programmerSuperList.get(1); // error, compiler won't let you do it. because you don't know who is really in the container.
        Person a = (Person) programmerSuperList.get(0); // // youc an cast type to get an object or Person, but we don't know what kind of Object it is.
        System.out.println("test super : get it out of List superisingly as Object : " + a.toString());
        // so you can 'initialize' programmer list with it's subclasses, But after that you WON'T be able to add anything. so only one change, don't miss it.
    }


    /**
     * <? super Integer> means that it is an unknown type, but can be Integer or its super type, like Integer, Number, Object, so the lowered-bounded object will be Integer, which can only be added
     * But Just because you can only add integer types to the list using the "list" reference, doesn't mean that there are only Integers in the list.  see main() method which calls this method.
     */
    void testSuper(List<? super Integer> list) {
        System.out.println("-------------------------testSuper1----------------------------------------------");
        Integer ii = 0;
        list.add(ii);
        for (Object obj : list) {
            System.out.println(obj);
        }
//        list.add(new Float("1.0"));  throw errors , can't be added by compiler ,  Float can' be added, lower-bounded is integer.
//        System.out.println(list.get(0));
    }

    /**
     * <? super Number> means that it is an unknown type, but can be Number or its super type, like Number, Object, Serializable, so the lowered-bounded object will be Integer, Double, Float can be added.
     * But Just because you can only add lower-bounded types to the list, doesn't mean that there are only those types in the list.
     */
    void testSuper2(List<? super Number> list) {
        System.out.println("-------------------------testSuper2----------------------------------------------");
        list.add(null);
        list.add(new Integer(1));
        list.add(new Float(2.0));
        list.add(new Double(3.22));
        for (Object obj : list) {
            System.out.println(obj);
        }
        System.out.println(list.get(0));
    }

    // you may use only for argument to take any and print them.
    private static void printList(List<? extends Object> list) {
        list.get(0);
//        list.add("sth"); fails
    }

    static class Person {
        String name;
        int age;

        public Person(String name) {
            this.name = name;
        }

        public Person() {
        }
    }

    static class Worker extends Person {
        public Worker(String name) {
            super(name);
        }

        public Worker() {
        }
    }

    static class Programmer extends Worker {
        String language;
        boolean isSenior;
        boolean isArchitect;
        String ide;

        public Programmer(String name) {
            super.name= name;
        }

        @Override
        public String toString() {
            return "Programmer{" + "name='" + name + '\'' + '}';
        }
    }

    static class JavaProgrammer extends Programmer {
        public JavaProgrammer(String name) {
            super(name);
        }
    }

    static class JavaArchitectProgrammer extends JavaProgrammer {
        public JavaArchitectProgrammer(String name) {
            super(name);
        }
    }
}