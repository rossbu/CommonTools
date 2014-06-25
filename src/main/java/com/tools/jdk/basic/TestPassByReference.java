package com.tools.jdk.basic;


/**
 * Output of the program :
 * Most methods passed arguments when they are called. An argument may be a constant or a variable.
 * For example in the expression: Math.sqrt(x); The variable x is passed here.
 * <p/>
 * Pass by Reference means the passing the address itself rather than passing the value and pass by value means passing a copy of the value as an argument.
 * <p/>
 * This is simple enough, however there is an important but simple principle at work here. If a variable is passed, the method receives a copy of the variable's value. The value of the original variable cannot be changed within the method. This seems reasonable because the method only has a copy of the value; it does not have access to the original variable. This process is called pass by value.
 * <p/>
 * However, if the variable passed is an object, then the effect is different. We often say things like, "this method returns an object ...", or "this method is passed an object as an argument ..." But this is not quite true, more precisely, we should say, something like "this method returns a reference to an object ..." or "this method is passed a reference to an object as an argument ..."
 * <p/>
 * Generally, objects are never passed to methods or returned by methods. It is always "a reference to an object" that is passed or returned. In general, pass by value refers to passing a constant or a variable holding a primitive data type to a method, and pass by reference refers to passing an object variable to a method. In both cases a copy of the variable is passed to the method. It is a copy of the "value" for a primitive data type variable; it is a copy of the "reference" for an object variable. So, a method receiving an object variable as an argument receives a copy of the reference to the original object.
 * <p/>
 * Here's the clincher: If the method uses that reference to make changes to the object,
 * then the original object is changed. This is reasonable because both the original reference and the copy of the reference "refer to" to same thing ” the original object.
 * There is one exception: strings. Since String objects are immutable in Java,
 * a method that is passed a reference to a String object cannot change the original object.
 * <p/>
 * To understand pass by reference lets see the sample program below:
 * 25
 * Java is fun!
 * Hello, world
 * <p/>
 * 9
 * fun
 * Hello, Java world
 * <p/>
 * 25
 * Java is fun!
 * Hello, Java world
 */
public class TestPassByReference {
    public static void main(String[] args) {
        // declare and initialize variables and objects
        int i = 25;
        String s = "Java is fun!";
        StringBuffer sb = new StringBuffer("Hello, world");

        // print variable i and objects s and sb
        System.out.println(i);     // print it (1)
        System.out.println(s);    // print it (2)
        System.out.println(sb);  // print it (3)

        // attempt to change i, s, and sb using methods
        iMethod(i);
        sMethod(s);
        sbMethod(sb);

        // print variable i and objects s and sb (again)
        System.out.println(i);    // print it (7)
        System.out.println(s);   // print it (8)
        System.out.println(sb); // print it (9)

    }

    public static void iMethod(int iTest) {
        iTest = 9;                          // change it
        System.out.println(iTest); // print it (4)
        return;
    }

    public static void sMethod(String sTest) {
        sTest = sTest.substring(8, 11); // change it
        System.out.println(sTest);        // print it (5)
        return;
    }

    public static void sbMethod(StringBuffer sbTest) {
        sbTest = sbTest.insert(7, "Java "); // change it
        System.out.println(sbTest);            // print it (6)
        return;
    }
}