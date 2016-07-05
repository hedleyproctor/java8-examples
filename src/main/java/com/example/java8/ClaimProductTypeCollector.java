package com.example.java8;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ClaimProductTypeCollector<T extends Claim> implements Collector<T,Map,Map> {

    private Set<Claim.PRODUCT_TYPE> requiredTypes = new HashSet<>();

    public Set<Claim.PRODUCT_TYPE> getRequiredTypes() {
        return requiredTypes;
    }

    @Override
    public Supplier<Map> supplier() {
        return () -> new HashMap<>();
    }

    @Override
    public BiConsumer<Map,T> accumulator() {
        return (map,claim) -> {
            if (map.get(claim.getProductType()) == null) {
                map.put(claim.getProductType(),claim);
            }
        };
    }


    @Override
    public Function<Map, Map> finisher() {
        return Function.identity();
    }

    @Override
    public BinaryOperator<Map> combiner() {
        return null;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.singleton(Characteristics.IDENTITY_FINISH);
    }
}
