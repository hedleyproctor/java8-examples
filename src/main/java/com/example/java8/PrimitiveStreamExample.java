package com.example.java8;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PrimitiveStreamExample {

    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();
        Stream.iterate(1,i -> i+1).limit(1000000).parallel().reduce(0, Integer::sum);
        long t2 = System.currentTimeMillis();
        long time = t2 - t1;
        System.out.println("Integer stream with iterate. Time: " + time + "ms");

        t1 = System.currentTimeMillis();
        IntStream.rangeClosed(1,1000000).parallel().reduce(0, Integer::sum);
        t2 = System.currentTimeMillis();
        time = t2 - t1;
        System.out.println("int stream with rangeClosed. Time: " + time + "ms");

    }
}
