package com.example.java8;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Claim {

    public enum PRODUCT_TYPE { MOTOR, HOUSEHOLD, TRAVEL}

    private Long id;
    private PRODUCT_TYPE productType;
    private double totalPayments;
    private Set<Job> jobs = new HashSet<>();

    public Claim() {

    }

    public Claim(Long id) {
        this.id = id;
    }

    public Claim(long id, PRODUCT_TYPE productType, double totalPayments, Job... jobs) {
        this.id = id;
        this.productType = productType;
        this.totalPayments = totalPayments;
        if (jobs != null) {
            this.jobs.addAll(Arrays.asList(jobs));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PRODUCT_TYPE getProductType() {
        return productType;
    }

    public void setProductType(PRODUCT_TYPE productType) {
        this.productType = productType;
    }

    public double getTotalPayments() {
        return totalPayments;
    }

    public void setTotalPayments(double totalPayments) {
        this.totalPayments = totalPayments;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }
}
