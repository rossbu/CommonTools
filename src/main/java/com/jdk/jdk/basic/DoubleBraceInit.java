package com.jdk.jdk.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


/**
 *
 1. The first brace creates a new Anonymous Inner Class.


 2. The second set of brace creates an instance initializers like static block in Class.
     {
         put("2", "TWO");
     }

 */

public class DoubleBraceInit {

    public static void main(String[] args) {
        ArrayList<String> strings = new ArrayList<String>() {{
            add("bu");
            add("tianshi");
        }};


        HashMap<String,String> map = new HashMap<String,String>(){
            {
                put("1", "ONE");
            }
            {
                put("2", "TWO");
            }
            {
                put("3", "THREE");
            }
        };
        Set<String> keySet = map.keySet();
        for (String string : keySet) {
            System.out.println(string+" ->"+map.get(string));
        }
    }
}
