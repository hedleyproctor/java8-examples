package com.example.java8.collect;

import com.example.java8.Claim;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class UniqueCollectorTest {

    @Test
    public void test() {
        System.out.println("Generating claims as input data...");
        List<Claim> inputData = new ArrayList<>();
        IntStream.rangeClosed(1,20_000_000).forEach(i -> {
            Claim claim = new Claim();
            claim.setId((long) (Math.random()*100));
            inputData.add(claim);
        });
        System.out.println("Done generating input data. Size: " + inputData.size());

        System.out.println("Doing non-parallel evaluation");
        evaluate(inputData.stream());

        System.out.println("Now parallel");
        evaluate(inputData.parallelStream());

        // on my machine takes 231 and 110 milliseconds respectively, for 20,000,000 claims
    }

    private void evaluate(Stream<Claim> claimStream) {
        long startTime = System.currentTimeMillis();
        // replace the call toList() with your collector...
        List<Claim> uniqueClaims = claimStream.collect(toList());
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        System.out.println("Got unique list. Size: " + uniqueClaims.size());
        System.out.println("Evaluation time (milliseconds): " + time);
    }
}
