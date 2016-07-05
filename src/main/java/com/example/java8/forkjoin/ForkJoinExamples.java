package com.example.java8.forkjoin;

import java.util.Set;
import java.util.logging.Level;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class ForkJoinExamples {

    public static void main(String[] args) throws InterruptedException {
        long[] input = LongStream.rangeClosed(1,10000).toArray();
//        PerfectNumber perfectNumber = new PerfectNumber(input);
//        Set<Long> perfectNumbers = perfectNumber.compute();
//        for (Long l : perfectNumbers) {
//            System.out.println(l);
//        }

      //  input = new long[20];
        for (int i=0; i<input.length; i++) {
            input[i] = Math.round(Math.random()*1000);
        }
        QuickSort quickSort = new QuickSort(input,Level.INFO);
        quickSort.compute();

//        for (int i=0; i<input.length; i++) {
//            System.out.println(input[i]);
//        }
    }
}
