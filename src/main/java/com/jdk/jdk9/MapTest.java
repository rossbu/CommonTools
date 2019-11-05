package com.jdk.jdk9;

import java.util.AbstractMap;
import java.util.Map;

public class MapTest {
    public static void main(String[] args) {

    }

    public void createMapWithMapOf() {
        Map<String, String> emptyMap = Map.of();
        Map<String, String> singletonMap = Map.of("key1", "value");
        Map<String, String> map = Map.of("key1","value1", "key2", "value2");
    }
    public void createMapWithMapEntries() {
        Map<String, String> map = Map.ofEntries(
                new AbstractMap.SimpleEntry<String, String>("name", "John"),
                new AbstractMap.SimpleEntry<String, String>("city", "budapest"),
                new AbstractMap.SimpleEntry<String, String>("zip", "000000"),
                new AbstractMap.SimpleEntry<String, String>("home", "1231231231")
        );
    }
}
