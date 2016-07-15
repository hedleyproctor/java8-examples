package com.example.java8.collect;

import com.example.java8.Claim;

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

    // returns a no-arg function, that when called, returns the container object
    // that will be used to hold the results of your collection
    @Override
    public Supplier<Map> supplier() {
        return () -> new HashMap<>();
    }

    // returns a function that takes two arguments:
    // 1. your container function
    // 2. the next object in the stream
    // The accumulator function contains the code that knows how to add the next object to your collection container.
    @Override
    public BiConsumer<Map,T> accumulator() {
        return (map,claim) -> {
            if (requiredTypes.contains(claim.getProductType()) && map.get(claim.getProductType()) == null) {
                map.put(claim.getProductType(),claim);
            }
        };
    }

    // you can perform a transformation on the container object to convert it to something different before returning
    // it to the user.
    // In this case, we return the identity function, since no transformation is required
    @Override
    public Function<Map, Map> finisher() {
        return Function.identity();
    }

    // combine the results of parallel streams
    @Override
    public BinaryOperator<Map> combiner() {
        return (map1,map2) -> {
            map1.putAll(map2);
            return map1;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.singleton(Characteristics.IDENTITY_FINISH);
    }
}
