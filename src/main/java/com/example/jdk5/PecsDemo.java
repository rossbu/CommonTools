package com.example.jdk5;

import com.example.pojo.Person;

import java.util.*;

/**
 * Created by tbu on 4/5/2018.
 * <p>
 * <p>
 * https://docs.oracle.com/javase/tutorial/java/generics/lowerBounded.html
 */
public class PecsDemo {
    PecsDemo() {
    }

    public static void main(String[] args) {
        PecsDemo demo = new PecsDemo();

        // test super
        List<Object> superObjects = new ArrayList<Object>();
        superObjects.add("str");
        superObjects.add(new Person());
        superObjects.add(1f); // 1f is Number
        demo.testSuper(superObjects);
        demo.testSuper2(superObjects);


        // test extends
        List<? extends Integer> extendsObjects = new ArrayList<>();
        demo.testExtends(extendsObjects);



        // copy
        ArrayList arrayList = new ArrayList();
        arrayList.add("arl_element_1");
        arrayList.add("arl_element_4");

        // Create a Vector and populate it with elements
        Vector vector = new Vector();
        vector.add("vec_element_1");
        vector.add("vec_element_6");
        vector.add("vec_element_7");
        Collections.copy(vector, arrayList);

    }


    /**
     * <? super Integer> means that it is an unknown type, but can be Integer or its super type, like Integer, Number, Object, so the lowered-bounded object will be Integer, which can only be added
     * But Just because you can only add integer types to the list using the "list" reference, doesn't mean that there are only Integers in the list.
     */
    void testSuper(List<? super Integer> list) {
        System.out.println("-------------------------testSuper1----------------------------------------------");
        Integer ii = 0;
        list.add(ii);
        for (Object obj : list) {
            System.out.println(obj);
        }
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

    /**
     * extends means you can only add null to the collections , nothing else;
     *
     * @param listOfObject
     */
    void testExtends(List<? extends Number> listOfObject) {
        System.out.println("-------------------------testExtends----------------------------------------------");
        listOfObject.add(null);
//        listOfObject.add(1);
        System.out.println(listOfObject.size());
    }

    /**
     * List<?>  ==== List<? extends Object>
     *
     * Question is :  if this List is type of Object . why we can't add to the list?
     */
    void testExtends2(List<?> list) {
        list.add(null);
    }


    /**
     * let's write a method to copy one collection to another collection
     */
     static <T> void copy(Collection<? super T> dest, Collection<? extends T> src) {
         for (int i = 0; i < src.size(); i++ ){
         }
     }
}
