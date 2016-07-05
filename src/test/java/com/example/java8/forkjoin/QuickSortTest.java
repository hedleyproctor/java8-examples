package com.example.java8.forkjoin;

import org.testng.annotations.Test;

import java.util.stream.LongStream;

import static org.testng.Assert.assertTrue;

public class QuickSortTest {

    @Test
    public void quickSortTest() {
        long[] input = LongStream.rangeClosed(1, 10000).toArray();
        for (int i=0; i<input.length; i++) {
            input[i] = Math.round(Math.random()*1000);
        }
        QuickSort quickSort = new QuickSort(input);
        quickSort.compute();

        for (int i=0; i<input.length-1; i++) {
           assertTrue(input[i] <= input[i+1]);
        }
    }
}
