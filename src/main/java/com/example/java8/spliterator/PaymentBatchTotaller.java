package com.example.java8.spliterator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class PaymentBatchTotaller implements Collector<Payment,PaymentBatchTotaller.Accumulator,Map<String,Double>> {

    public class Total {
        public double amount;
        public int numberOfBatches;
    }

    public class Accumulator {
        Map<String,Total> totalsByCategory = new HashMap<>();
        String currentPaymentCategory;
    }

    @Override
    public Supplier<Accumulator> supplier() {
        return Accumulator::new;
    }

    @Override
    public BiConsumer<Accumulator,Payment> accumulator() {
        return (accumulator,payment) -> {
            // store this amount
            Total batchTotalForThisCategory = accumulator.totalsByCategory.get(payment.getCategory());
            if (batchTotalForThisCategory == null) {
                batchTotalForThisCategory = new Total();
                accumulator.totalsByCategory.put(payment.getCategory(),batchTotalForThisCategory);
            }
            batchTotalForThisCategory.amount += payment.getAmount();

            // if this was start of a new batch, increment the counter
            if (!payment.getCategory().equals(accumulator.currentPaymentCategory)) {
                batchTotalForThisCategory.numberOfBatches += 1;
                accumulator.currentPaymentCategory = payment.getCategory();
            }
        };
    }

    @Override
    public BinaryOperator<Accumulator> combiner() {
        return (accumulator1,accumulator2) -> {
            for (String category : accumulator1.totalsByCategory.keySet()) {
                Total total2 = accumulator2.totalsByCategory.get(category);
                if (total2 == null) {
                    accumulator2.totalsByCategory.put(category,accumulator1.totalsByCategory.get(category));
                } else {
                    Total total1 = accumulator1.totalsByCategory.get(category);
                    total2.amount += total1.amount;
                    total2.numberOfBatches += total1.numberOfBatches;
                }
            }
            return accumulator2;
        };
    }

    @Override
    public Function<Accumulator, Map<String, Double>> finisher() {
        return (accumulator) -> {
            Map<String,Double> results = new HashMap<>();
            for (Map.Entry<String,Total> entry : accumulator.totalsByCategory.entrySet()) {
                String category = entry.getKey();
                Total total = entry.getValue();
                double averageForBatchInThisCategory = total.amount / total.numberOfBatches;
                results.put(category,averageForBatchInThisCategory);
            }
            return results;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.EMPTY_SET;
    }
}
