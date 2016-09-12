package com.example.java8;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class PrimitiveStreamExamples {

    public static void main(String[] args) {

        // processing a primitive stream is generally quicker than unboxing an object stream
        // Three primitive streams available:
        // IntStream
        // DoubleStream
        // LongStream

        // First create an object stream
        Stream<Integer> integerObjectStream = Stream.iterate(1,i -> i+1).limit(1000000);
        // now reduce it
        long t1 = System.currentTimeMillis();
        integerObjectStream.parallel().reduce(0, Integer::sum);
        long t2 = System.currentTimeMillis();
        long time = t2 - t1;
        System.out.println("Integer stream with iterate. Time: " + time + "ms");

        // do the same with an IntStream
        IntStream primitiveIntStream = IntStream.rangeClosed(1,1000000);
        t1 = System.currentTimeMillis();
        primitiveIntStream.parallel().reduce(0, Integer::sum);
        t2 = System.currentTimeMillis();
        time = t2 - t1;
        System.out.println("int stream with rangeClosed. Time: " + time + "ms");

        // in fact, convenience methods exist on the primitive stream
        int sum = IntStream.rangeClosed(1,100).sum();

        // for a sum, if the stream is empty, sum() returns 0
        // For other methods, a specialized optional is returned:
        OptionalDouble average = IntStream.rangeClosed(1,100).average();
        // OptionalDouble is simpler than the normal Optional class, so you don't have map or flatMap,
        // but you do have isPresent(), ifPresent(av -> do stuff), orElse, orElseGet, orElseThrow

        // also have a summary object, similar to object streams
        IntSummaryStatistics intSummaryStatistics = IntStream.rangeClosed(1,100).summaryStatistics();

        // can convert from an object stream to primitive stream
        List<Claim> claims = Arrays.asList(new Claim(1l),new Claim(2l));
        LongStream idStream = claims.stream().mapToLong(Claim::getId);

        // can convert from primitive stream back to objects
        Stream<Claim> claimStream = LongStream.rangeClosed(1,100).mapToObj(Claim::new);


    }
}
