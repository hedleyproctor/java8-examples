package com.example.java8.spliterator;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class PaymentBatchSpliterator implements Spliterator<Payment> {

    private List<Payment> paymentList;
    private int current;
    private int last;  // inclusive

    public PaymentBatchSpliterator(List<Payment> payments) {
        this.paymentList = payments;
        last = paymentList.size() - 1;
    }

    public PaymentBatchSpliterator(List<Payment> payments, int start, int last) {
        this.paymentList = payments;
        this.current = start;
        this.last = last;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Payment> action) {
        if (current <= last) {
            action.accept(paymentList.get(current));
            current++;
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<Payment> trySplit() {
        if ((last - current) < 100) {
            return null;
        }

        // first stab at finding a split position
        int splitPosition = current + (last - current) / 2;
        // if the categories are the same, we can't split here, as we are in the middle of a batch
        String categoryBeforeSplit = paymentList.get(splitPosition-1).getCategory();
        String categoryAfterSplit = paymentList.get(splitPosition).getCategory();

        // keep moving forward until we reach a split between categories
        while (categoryBeforeSplit.equals(categoryAfterSplit)) {
            splitPosition++;
            categoryBeforeSplit = categoryAfterSplit;
            categoryAfterSplit = paymentList.get(splitPosition).getCategory();
        }

        // safe to create a new spliterator
        PaymentBatchSpliterator secondHalf = new PaymentBatchSpliterator(paymentList,splitPosition,last);
        // reset our own last value
        last = splitPosition - 1;

        return secondHalf;
    }

    @Override
    public long estimateSize() {
        return last - current;
    }

    @Override
    public int characteristics() {
        return 0;
    }
}
