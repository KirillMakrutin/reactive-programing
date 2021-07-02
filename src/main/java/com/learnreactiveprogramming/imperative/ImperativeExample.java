package com.learnreactiveprogramming.imperative;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImperativeExample {
    public static void main(String[] args) {
        final var names = List.of("alex", "ben", "chloe", "adam", "adam");
        final var filteredNames = namesGreaterThanSize(names, 3);
        Collections.sort(filteredNames);
        System.out.println(filteredNames);
    }

    private static List<String> namesGreaterThanSize(List<String> names, int size) {
        var filteredNames = new ArrayList<String>();
        for (String name : names) {
            if (name.length() > size && !filteredNames.contains(name.toUpperCase()))
                filteredNames.add(name.toUpperCase());
        }

        return filteredNames;
    }
}
