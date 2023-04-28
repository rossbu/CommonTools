package com.demo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
/*
Static fields aren't serialized either,

Why are transient fields needed in Java?

    The transient keyword gives you some control over the serialization process and allows you to exclude some object properties from this process.

when to use transient
     I try not to allow fields whose values can be derived from others to be serialized, so I mark them transient.
     If you have a field called interest whose value can be calculated from other fields (principal, rate & time), there is no need to serialize it.

what is the transient keyword and its purpose?
    By default, all of object's variables get converted into a persistent state.
    In some cases, you may want to avoid persisting some variables because you don't have the need to persist those variables. So you can declare those variables as transient.
    If the variable is declared as transient, then it will not be persisted. That is the main purpose of the transient keyword.
 */
public class SerializableTest {
    public static void main(String args[]) throws Exception {
        NameStore nameStore = new NameStore("Steve", "Middle","Jobs");
        ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream("nameStore"));
        // writing to object
        o.writeObject(nameStore);
        o.close();

        // reading from object
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("nameStore"));
        NameStore nameStore1 = (NameStore)in.readObject();
        System.out.println(nameStore1);
    }
    static class NameStore implements Serializable {
        private final String firstName;
        private final transient String middleName;
        private final String lastName;

        public NameStore (String fName, String mName, String lName){
            this.firstName = fName;
            this.middleName = mName;
            this.lastName = lName;
        }

        public String toString(){
            StringBuffer sb = new StringBuffer(40);
            sb.append("First Name : ");
            sb.append(this.firstName);
            sb.append("Middle Name : ");        // this would not be serialized
            sb.append(this.middleName);
            sb.append("Last Name : ");
            sb.append(this.lastName);
            return sb.toString();
        }
    }
}
