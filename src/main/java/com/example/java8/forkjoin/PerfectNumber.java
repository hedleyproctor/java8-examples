package com.example.java8.forkjoin;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class PerfectNumber extends RecursiveTask<Set<Long>> {
    private final int DIVIDE_THRESHOLD = 1000;
    private long[] input;
    private int start;
    private int end;

    // constructor for top level task creation
    public PerfectNumber(long[] input) {
        this.input = input;
        start = 0;
        end = input.length;
    }

    // constructor for recursive task creation
    public PerfectNumber(long[] input, int start, int end) {
        this.input = input;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Set<Long> compute() {
        int length = end - start;
        if (length > DIVIDE_THRESHOLD) {
            return computeInParallel(length);
        }
        return computeSequentially();
    }

    private Set<Long> computeInParallel(int length) {
        PerfectNumber leftTask = new PerfectNumber(input,start,start+length/2);
        leftTask.fork(); // fork into separate thread
        PerfectNumber rightTask = new PerfectNumber(input,start+length/2,end);
        Set<Long> rightResult = rightTask.compute(); // our thread
        Set<Long> leftResult = leftTask.join(); // block until left task completes
        leftResult.addAll(rightResult);
        return leftResult;
    }

    private Set<Long> computeSequentially() {
        Set<Long> results = new HashSet<>();
        for (int i = start; i<end; i++) {
            if (isPerfect(input[i])) {
                results.add(input[i]);
            }
        }
        return results;
    }

    private boolean isPerfect(long number) {
        int sum = 0;
        for(int i=1;i<number;i++)
        {
            if(number % i == 0) {
                sum = sum + i;
            }
        }
        if(sum == number) {
            return true;
        }
        return false;
    }
}
