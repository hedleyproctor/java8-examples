package com.example.java8.forkjoin;

import java.util.concurrent.RecursiveAction;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuickSort extends RecursiveAction {
    private final int DIVIDE_THRESHOLD = 1000;
    private long[] input;
    private int start;
    private int end;

    private static final Logger LOG = Logger.getAnonymousLogger();

    public QuickSort(long[] input) {
        this(input,null);
    }

    // constructor for top level task creation
    public QuickSort(long[] input, Level logLevel) {
        this.input = input;
        start = 0;
        end = input.length-1;
        if (logLevel != null) {
            Handler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(logLevel);
            LOG.addHandler(consoleHandler);
            LOG.setLevel(logLevel);
        }
    }

    // constructor for recursive task creation
    public QuickSort(long[] input, int start, int end) {
        this.input = input;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        int length = end - start;
        if (length > DIVIDE_THRESHOLD) {
            computeInParallel(length);
        }
        quicksort(input, 0, input.length-1);
    }

    private void computeInParallel(int length) {
        LOG.info("Starting parallel computation. Start: " + start + " End: " + end);
        QuickSort leftTask = new QuickSort(input,start,start+length/2);
        leftTask.fork(); // fork into separate thread
        QuickSort rightTask = new QuickSort(input,start+length/2,end);
        rightTask.compute(); // our thread
        leftTask.join(); // block until left task completes
        LOG.info("Parallel computation finished");
    }

    private void quicksort(long[] input, int start, int end) {
        if (start < end) {
            int splitIndex = partition(input,start,end);
            quicksort(input, start, splitIndex-1);
            quicksort(input, splitIndex+1, end);
        }
    }

    private int partition(long[] input, int start, int end) {
        long pivotValue = input[end];
        LOG.fine("Initial pivot value: " + pivotValue);
        int pivotIndex = start;
        // need to iterate over the values in the array and compare each one with the pivot value
        // we want numbers less than the pivot to the left of it, and numbers more than the pivot on its right
        // remember the pivot index always starts at zero / the left, hence:
        // if the number is more than the pivot value, it can stay where it is, since the pivot will already be
        // on its left.
        // if the number is less than the pivot value, we must swap the number with the number at the pivot index
        // since that is the first number that is more than the pivot value, then increment the pivot index
        for (int i=start; i<end; i++) {
            LOG.fine("i=" + i + " value: " + input[i]);
            if (input[i] < pivotValue) {
                swap(input,i,pivotIndex);
                pivotIndex++;
            }
        }
        // finally stick the pivot value in where it belongs
        swap(input,pivotIndex,end);
        return pivotIndex;
    }

    private void swap(long[] input, int x, int y) {
        LOG.fine("Less than pivot value. Need to swap.");
        long temp = input[x];
        LOG.fine("index[" + x + "] being set to: " + input[y]);
        input[x] = input[y];
        LOG.fine("index[" + y + "] being set to " + temp);
        input[y] = temp;
    }

}
