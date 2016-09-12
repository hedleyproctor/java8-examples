package com.example.java8.collect;

import com.example.java8.Claim;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/** Does work, but if used in parallel, will get 100 claims for each thread,
 * before merging these to a single map of 100 claims, so rather inefficient.
 * Compare with ConcurrentUniqueCollector.
 *
 * @param <T>
 */
public class NaiveUniqueCollector<T extends Claim> implements Collector<T,Map,List> {
    @Override
    public Supplier<Map> supplier() {
        return () -> new HashMap<>();
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
        return Collections.EMPTY_SET;
    }
}
