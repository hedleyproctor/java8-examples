package com.example.java8.collect;

import com.example.java8.Claim;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ConcurrentUniqueCollector<T extends Claim> implements Collector<T,Map,List> {
    @Override
    public Supplier<Map> supplier() {
        System.out.println("Creating new ConcurrentHashMap");
        return () -> new ConcurrentHashMap<>();
    }

    @Override
    public BiConsumer<Map, T> accumulator() {
        return (map,claim) -> {
            if (!map.containsKey(claim.getId())) {
                map.put(claim.getId(),claim);
            }
        };
    }

    @Override
    public BinaryOperator<Map> combiner() {
        return (map1,map2) -> {
            map1.putAll(map2);
            return map1;
        };
    }

    @Override
    public Function<Map, List> finisher() {
        return map -> new ArrayList(map.values());
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.singleton(Characteristics.CONCURRENT);
    }
}
