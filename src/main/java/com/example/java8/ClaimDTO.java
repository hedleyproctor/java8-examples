package com.example.java8;

public class ClaimDTO {
    private Long id;
    private double payments;

    public ClaimDTO(Long id, double payments) {
        this.id = id;
        this.payments = payments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPayments() {
        return payments;
    }

    public void setPayments(double payments) {
        this.payments = payments;
    }
}
