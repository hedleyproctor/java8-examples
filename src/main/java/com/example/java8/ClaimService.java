package com.example.java8;

import java.util.Optional;

public class ClaimService {

    public Claim find(Long id)  {
        // maybe find the object
        return null;
    }

    public Optional<Claim> findById(Long id) {
        // dummy implementation
        if (id > 10) {
            Claim claim = new Claim();
            claim.setId(id);
            return Optional.of(claim);
        }

        return Optional.empty();
    }

    public Claim.PRODUCT_TYPE getDefaultProductType() {
        return Claim.PRODUCT_TYPE.MOTOR;
    }

    public Optional<AuditLog> findAuditLog(Claim claim) {
        // dummy implementation
        return Optional.of(new AuditLog());
    }
}
