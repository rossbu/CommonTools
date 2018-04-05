package com.example.jdk8;


import java.util.*;
import java.util.stream.Collectors;

/*
Java Stream is a sequence of elements that support the sum operations.
In streams, the elements are reckoned on demand from different data sources such as Collections,
Arrays or I/O resources and thus the elements are never stored.

Streams allow the chaining of multiple operations and thus, developers can apply filtering, mapping, matching, searching,
sorting or reducing operations on streams. Unlike collections which use the external iteration fashion, streams are internally iterated. *
 */
public class FilterNullValueDemo {

    public static void main(String[] args) {

        List<String> cList = new ArrayList<String>();
        cList.add("United States of America");
        cList.add("Ecuador");
        cList.add("Denmark");
        cList.add(null);
        cList.add("Seychelles");
        cList.add("Germany");
        cList.add(null);

        System.out.println("<!-----Original list with null values-----!>");
        System.out.println(cList + "\n");

        List<?> mixedTypeList = Arrays.asList("a","b","",1,2,3,true,false,null);
        Set<?> filteredResult = mixedTypeList.stream().filter(obj -> obj != null).collect(Collectors.toSet());
        System.out.println("hmm: " + filteredResult);


        // EXAMPLE #1 = Filter Null Values from a Stream Using 'Lambda Expressions'
        List<String> result = cList.stream().filter(str -> str != null && str.length() > 0).collect(Collectors.toList());
        System.out.println("<!-----Result after null values filtered-----!>");
        System.out.println(result + "\n");

        // EXAMPLE #2 = Filter Null Values from a Stream Using 'Method Reference'
        List<String> nonNullResult = cList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        System.out.println("<!-----Result after null values filtered using nonNull-----!>");
        System.out.println(nonNullResult + "\n");

        // EXAMPLE #3 = Filter Null Values after Map intermediate operation
        List<String> mapNullResult = cList.stream().map(s -> s).filter(str -> str != null && str.length() > 0).collect(Collectors.toList());
        System.out.println("<!-----Result after null values filtered using Map intermediate operation-----!>");
        System.out.println(mapNullResult);
    }
}