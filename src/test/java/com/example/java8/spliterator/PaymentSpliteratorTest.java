package com.example.java8.spliterator;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaymentSpliteratorTest {

    @Test
    public void paymentBatchTotals() {
        // construct some test data
        List<Payment> payments = createSampleData();

        // this one won't work in parallel
        Map<String,Double> averageTotalsPerBatchAndCategory = payments.stream().collect(new PaymentBatchTotaller());


        for (Map.Entry<String,Double> total : averageTotalsPerBatchAndCategory.entrySet()) {
            System.out.println("Category: " + total.getKey() + " Average batch total: " + total.getValue());
        }

    }

    private List<Payment> createSampleData() {
        List<Payment> paymentList = new ArrayList<>();
        for (int i=0; i<1000; i++) {
            paymentList.add(new Payment(10,"A"));
            paymentList.add(new Payment(20,"A"));
            paymentList.add(new Payment(30,"A"));
            // total = 60

            paymentList.add(new Payment(20,"B"));
            paymentList.add(new Payment(30,"B"));
            paymentList.add(new Payment(40,"B"));
            paymentList.add(new Payment(50,"B"));
            paymentList.add(new Payment(60,"B"));
            // total = 200

            paymentList.add(new Payment(30,"C"));
            paymentList.add(new Payment(30,"C"));
            paymentList.add(new Payment(20,"C"));
            // total = 80
        }
        return paymentList;
    }
}
