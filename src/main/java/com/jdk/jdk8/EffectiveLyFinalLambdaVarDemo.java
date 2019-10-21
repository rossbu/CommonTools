package com.jdk.jdk8;

import java.util.ArrayList;
import java.util.List;

public class EffectiveLyFinalLambdaVarDemo {
    public static void main(String[] args) {
        String country = null;
        final String[] countries = {null};

        List<String> cities = new ArrayList<>();
        cities.add("Delhi");
        cities.add("New York");
        cities.add("Beijing");
        cities.add("1kjh1231");

        cities.forEach( elem -> {
            if(elem.equals("Beijing")){
//                country = "China";  // try to modify country value in 'foreach' lambda expression
                countries[0] = "China";
            }
        });
        System.out.println(countries); //Should be China
    }
}
