package com.example.java8;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class StreamExamples {

    private Set<Claim> createClaimData() {
        Set<Claim> claims = new HashSet<>();
        claims.add(new Claim(1l,Claim.PRODUCT_TYPE.MOTOR,254.34, new Job(Job.Type.MOTOR_REPAIR), new Job(Job.Type.SOLICITOR)));
        claims.add(new Claim(2l,Claim.PRODUCT_TYPE.MOTOR,1600.12, new Job(Job.Type.LOSS_ADJUSTER), new Job(Job.Type.MOTOR_REPAIR)));
        claims.add(new Claim(3l,Claim.PRODUCT_TYPE.MOTOR,750, new Job(Job.Type.SOLICITOR)));

        claims.add(new Claim(4l,Claim.PRODUCT_TYPE.HOUSEHOLD,425));
        claims.add(new Claim(5l,Claim.PRODUCT_TYPE.HOUSEHOLD,7000));

        return claims;
    }


    private Set<Dish> createMenuData() {
        Set<Dish> menu = new HashSet<>();
        menu.add(new Dish("Steak",false,900, Dish.Type.MEAT, new SideOrder(SideOrder.Type.ROAST_POTATOES,450), new SideOrder(SideOrder.Type.CHIPS,550), new SideOrder(SideOrder.Type.VEGETABLES,250)));
        menu.add(new Dish("Chicken",false,730, Dish.Type.MEAT, new SideOrder(SideOrder.Type.CHIPS,550), new SideOrder(SideOrder.Type.VEGETABLES,250)));
        menu.add(new Dish("Salmon",false,680, Dish.Type.FISH, new SideOrder(SideOrder.Type.SALAD,270), new SideOrder(SideOrder.Type.VEGETABLES,250)));
        menu.add(new Dish("Trout",false,750, Dish.Type.FISH, new SideOrder(SideOrder.Type.SALAD,200)));
        menu.add(new Dish("Lentil curry",true,700, Dish.Type.DELICIOUS_VEG, new SideOrder(SideOrder.Type.SALAD,270), new SideOrder(SideOrder.Type.VEGETABLES,250)));
        menu.add(new Dish("Bean burger",true,770, Dish.Type.DELICIOUS_VEG, new SideOrder(SideOrder.Type.CHIPS,700), new SideOrder(SideOrder.Type.VEGETABLES,250)));
        return menu;
    }

    public static void main(String[] args) {
        StreamExamples streamExamples = new StreamExamples();
        streamExamples.streamExamples();
    }

    public void streamExamples() {
        Set<Claim> claims = createClaimData();
        Set<Dish> menu = createMenuData();


        // 1. filter - intermediate operation - returns a stream
        // motor claims only
        Stream<Claim> motorClaims = claims.stream().filter(claim -> claim.getProductType().equals(Claim.PRODUCT_TYPE.MOTOR));
        // payments > 1000
        Stream<Claim> paymentsOver1000 = claims.stream().filter(claim -> claim.getTotalPayments() > 1000);
        // Example: claims with two or more jobs?

        // 2. map - intermediate operation - maps stream to different type
        Stream<Long> claimIds = claims.stream().map(claim -> claim.getId());
        // can write this using a method reference
        Stream<Long> claimIds2 = claims.stream().map(Claim::getId);
        // can chain / pipeline multiple stream operations
        Stream<Long> motorClaimIds = claims.stream()
                .filter(claim -> claim.getProductType().equals(Claim.PRODUCT_TYPE.MOTOR))
                .map(Claim::getId);
        // create new objects
        Stream<ClaimDTO> claimDTOs = claims.stream().map(claim -> new ClaimDTO(claim.getId(),claim.getTotalPayments()));
        // Example: Names of all dishes with more than 720 calories?

        // 3. flatmap - collapses a stream of collections into individual objects
        // get all jobs from all claims
        Stream<Job> jobs = claims.stream().map(Claim::getJobs).flatMap(Set::stream);
        // Example: side orders for dishes over 750 calories?

        // 4. collect - terminal operation - collect your results into a collection or single result
        // to a set
        Set<Claim> motorClaimSet = claims.stream().
                                    filter(claim -> claim.getProductType().equals(Claim.PRODUCT_TYPE.MOTOR)).
                                    collect(Collectors.toSet());
        // to a list
        List<Claim> motorClaimList = claims.stream().
                                    filter(claim -> claim.getProductType().equals(Claim.PRODUCT_TYPE.MOTOR)).
                                    collect(Collectors.toList());
        // to a map (grouping by unique key)
        Map<Long,Claim> motorClaimMap =  claims.stream().
                                        filter(claim -> claim.getProductType().equals(Claim.PRODUCT_TYPE.MOTOR)).
                                        collect(Collectors.toMap(Claim::getId, Function.<Claim>identity()));
        // Example: Map<String,Integer> of dish names and calories (for main dish)

        // group by non-unique key
        Map<Claim.PRODUCT_TYPE,List<Claim>> claimsByType = claims.stream().collect(groupingBy(Claim::getProductType));
        // multi-level grouping
        // group by product type, and then by claims more than £1000 and less
        Map<Claim.PRODUCT_TYPE,Map<String,List<Claim>>> claimsByTypeAndPayment = claims.stream().collect(
                                            groupingBy(Claim::getProductType,
                                                    groupingBy(claim -> {
                                                        if (claim.getTotalPayments() > 1000) {
                                                            return "HIGH";
                                                        }
                                                        else {
                                                            return "LOW";
                                                        }
                                                    })
        ));
        // side orders, grouped by type, with a list of the calories for each type
        Map<SideOrder.Type,List<Integer>> sideOrderCalories = menu.stream().map(Dish::getSideOrders).flatMap(Set::stream)
                .collect(groupingBy(SideOrder::getType, mapping(SideOrder::getCalories, toList())));


        // summing
        double totalCalories = menu.stream().collect(summingDouble(Dish::getCalories));
        double totalPayments = claims.stream().collect(summingDouble(Claim::getTotalPayments));

        // most expensive claim
        Optional<Claim> optionalClaim = claims.stream().max(Comparator.comparingDouble(Claim::getTotalPayments));


        // http://stackoverflow.com/questions/22577197/java-8-streams-collect-vs-reduce



        Map<Claim.PRODUCT_TYPE,List<Claim>> claimsByTypeOrderedByPaymentTotals = claims.stream().collect(groupingBy(Claim::getProductType));

        // consider claims of different types, each with one or more jobs, of different types, such as loss adjuster, motor repair, solicitor etc.
        // How do you find the distinct set of jobs that are displayed by a collection of claims?
        // or how do you find if all jobs / or jobs you specify are displayed by that collection
        Set<Job.Type> jobTypes = claims.stream().map(claim -> claim.getJobs()).flatMap(Set::stream).map(job -> job.getType()).collect(Collectors.toSet());

        // return one claim for each product type e.g. motor and household
        ClaimProductTypeCollector<Claim> claimProductTypeCollector = new ClaimProductTypeCollector();
        claimProductTypeCollector.getRequiredTypes().add(Claim.PRODUCT_TYPE.MOTOR);
        claimProductTypeCollector.getRequiredTypes().add(Claim.PRODUCT_TYPE.HOUSEHOLD);
        Map oneClaimPerProductType = claims.stream().collect(claimProductTypeCollector);



        // get total spend across all claims

        // get total spend on motor claims

        // get first motor claim

        System.out.println("Done");
    }
}
