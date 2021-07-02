package com.learnreactiveprogramming.functional;

import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class FunctionalExample {
    public static void main(String[] args) {
        final var filteredNames = List.of("alex", "ben", "chloe", "adam", "adam")
                .stream()
                .filter(greaterThan(3))
                .distinct()
                .map(String::toUpperCase)
                .sorted()
                .collect(toList());
        System.out.println(filteredNames);
    }

    private static Predicate<? super String> greaterThan(int size) {
        return name -> name.length() > size;
    }
}
