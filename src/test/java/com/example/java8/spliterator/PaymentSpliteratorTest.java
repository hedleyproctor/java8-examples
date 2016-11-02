package com.example.java8.spliterator;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.StreamSupport;

import static org.testng.Assert.assertEquals;

public class PaymentSpliteratorTest {

    @Test
    public void paymentBatchTotals() {
        // construct some test data
        List<Payment> payments = createSampleData();

        // this one won't work in parallel
        //Map<String,Double> averageTotalsPerBatchAndCategory = payments.stream().collect(new PaymentBatchTotaller());

        Map<String,Double> averageTotalsPerBatchAndCategory =
                StreamSupport.stream(new PaymentBatchSpliterator(payments),true).collect(new PaymentBatchTotaller());

        Set<Map.Entry<String,Double>> entrySet = averageTotalsPerBatchAndCategory.entrySet();
        assertEquals(entrySet.size(),3);
        for (Map.Entry<String,Double> total : averageTotalsPerBatchAndCategory.entrySet()) {
            if (total.getKey().equals("A")) {
                assertEquals(60d,total.getValue());
            } else if (total.getKey().equals("B")) {
                assertEquals(200d,total.getValue());
            } else {
                assertEquals(80d,total.getValue());
            }
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
