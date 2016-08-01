package com.example.java8;

import com.example.java8.collect.ClaimProductTypeCollector;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class StreamExamples {

    private static Set<Claim> claims;
    private static Set<Dish> menu;

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
        claims = streamExamples.createClaimData();
        menu = streamExamples.createMenuData();
        streamExamples.reduce();
    }

    /** filter - intermediate operation - returns a stream
      */
    public void filter() {
        // motor claims only
        Stream<Claim> motorClaims = claims.stream().filter(claim -> claim.getProductType().equals(Claim.PRODUCT_TYPE.MOTOR));
        // payments > 1000
        Stream<Claim> paymentsOver1000 = claims.stream().filter(claim -> claim.getTotalPayments() > 1000);
        // Example: claims with two or more jobs?
        Stream<Claim> twoOrMore = claims.stream().filter(claim -> claim.getJobs().size() >= 2);
    }

    public void map() {
        // map - intermediate operation - maps stream to different type
        Stream<Long> claimIds = claims.stream().map(claim -> claim.getId());
        // can write this using a method reference
        Stream<Long> claimIds2 = claims.stream().map(Claim::getId);
        // can chain / pipeline multiple stream operations
        Stream<Long> motorClaimIds = claims.stream()
                .filter(claim -> claim.getProductType().equals(Claim.PRODUCT_TYPE.MOTOR))
                .map(Claim::getId);
        // create new objects
        Stream<ClaimDTO> claimDTOs = claims.stream().map(claim -> new ClaimDTO(claim.getId(), claim.getTotalPayments()));
        // Example: Names of all dishes with more than 720 calories?
        Stream<String> dishNames = menu.stream().filter(dish -> dish.getCalories() > 720).map(Dish::getName);
    }

    public void flatmap() {
        // flatmap - collapses a stream of collections into individual objects
        // get all jobs from all claims
        Stream<Job> jobs = claims.stream().map(Claim::getJobs).flatMap(Set::stream);
        // Example: side orders for dishes over 750 calories?
        Stream<SideOrder> sideOrdersOver750 = menu.stream().filter(dish -> dish.getCalories() > 750).map(Dish::getSideOrders).flatMap(Set::stream);
    }

    public void collect() {
        // collect - terminal operation - collect your results into a collection or single result
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
        Map<String,Integer> dishNameAndCalories = menu.stream().collect(Collectors.toMap(Dish::getName, Dish::getCalories));

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

        // partitioning - divide stream into two groups
        Map<Boolean,List<Dish>> veggieAndNonVeggie = menu.stream().collect(partitioningBy(Dish::isVegetarian));

        // summing
        int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));
        double totalPayments = claims.stream().collect(summingDouble(Claim::getTotalPayments));
        // average
        double averagePayment = claims.stream().collect(averagingDouble(Claim::getTotalPayments));

        // if you want multiple sums etc, better off using a summing collector, so you don't have to iterate the stream
        // multiple times:
        DoubleSummaryStatistics paymentStats = claims.stream().collect(summarizingDouble(Claim::getTotalPayments));
        totalPayments = paymentStats.getSum();
        averagePayment = paymentStats.getAverage();

        // joining
        String claimIdListAsCommaSeparatedString = claims.stream().map(claim -> claim.getId().toString()).collect(joining(","));

        // custom collector: return one claim for each product type e.g. motor and household
        ClaimProductTypeCollector<Claim> claimProductTypeCollector = new ClaimProductTypeCollector();
        claimProductTypeCollector.getRequiredTypes().add(Claim.PRODUCT_TYPE.MOTOR);
        claimProductTypeCollector.getRequiredTypes().add(Claim.PRODUCT_TYPE.HOUSEHOLD);
        Map oneClaimPerProductType = claims.stream().collect(claimProductTypeCollector);
    }

    /** reduce is a "functional" reduction. i.e. not using a mutable container
     *
     */
    public void reduce() {
        int totalCalories = menu.stream().map(Dish::getCalories).reduce(0,(calories1,calories2) -> calories1 + calories2);
        // note that the two argument reduce method reduces a stream to a single value of the same type as the stream.
        // Hence whenever you want a result of a different type to your current stream, you must map first.
        // This is the "map-reduce" pattern made famous by Google.

        // also have a single parameter version of the method:
        Optional<Integer> totalCalories2 = menu.stream().map(Dish::getCalories).reduce((calories1,calories2) -> calories1 + calories2);

        // finally there is a three argument method that combines both the map and reduce operations.
        // Note how similar it looks to a collect:
        int totalCalories3 = menu.stream().reduce(0,(calories,dish) -> calories + dish.getCalories(), null);
    }

}
