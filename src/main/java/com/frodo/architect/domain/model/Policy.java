package com.frodo.architect.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.UUID;

/**
 * A DDD entity. It is mutable. It got state, lifecycle and can change. It is a class.
 */
public class Policy {

    private UUID policyId;            // System identity for Policy
    private String policyNumber;      // Business identity for Policy
    private Property property;
    private UUID policyHolderId;      // Reference to another Aggregate Root (Customer)
    private Money premium;
    private LocalDate fromDate;
    private LocalDate toDate;
    private PolicyStatus policyStatus;

    // private constructor to ensure all instances of policies goes via public factory
    private Policy() {}

    public static Policy create(Property property, UUID policyHolderId, Money premium, LocalDate fromDate, LocalDate toDate) {
        Policy policy = new Policy();

        // The domain encapsulates its own identity generation rules upon creation
        policy.policyId = UUID.randomUUID();
        policy.policyNumber = generateBusinessPolicyNumber();

        // Map structural components
        policy.property = property; // Your rich Value Object
        policy.policyHolderId = policyHolderId;
        policy.premium = calculatedPremium();
        policy.fromDate = fromDate;
        policy.toDate = toDate;
        policy.policyStatus = PolicyStatus.DRAFT;

        return policy;
    }

    private static Money calculatedPremium() {
        // always 100000 for now
        return new Money(new BigDecimal("100000"), Currency.getInstance("SEK"));
    }

    private static String generateBusinessPolicyNumber() {
        return null;
    }

    public static Policy reconstitute(UUID policyId, String policyNumber, UUID propertyId, UUID policyHolderId, Money premium, LocalDate fromDate, LocalDate toDate,  PolicyStatus policyStatus) {
        Policy policy = new Policy();
        policy.policyId = policyId;
        policy.policyNumber = policyNumber;
        policy.policyHolderId = policyHolderId;
        policy.premium = premium;
        policy.fromDate = fromDate;
        policy.toDate = toDate;
        policy.policyStatus = policyStatus;  // ← restored from DB, not hardcoded
        return policy;
    }

    public void activate () {
        if (this.policyStatus != PolicyStatus.DRAFT) {
            throw new IllegalStateException("Only a DRAFT policy can be activated.");
        }
        this.policyStatus = PolicyStatus.ACTIVE;
    }

    public void cancel () {
        if (this.policyStatus != PolicyStatus.ACTIVE) {
            throw new IllegalStateException("Only a ACTIVE policy can be cancelled");
        }

        this.policyStatus = PolicyStatus.CANCELLED;
    }

    public void expire (LocalDate today) {
        if (this.policyStatus != PolicyStatus.ACTIVE) {
            throw new IllegalStateException("Only a ACTIVE policy can be expired");
        }

        if (today.isBefore(this.toDate)) {
            throw new IllegalArgumentException("Cannot expire a policy before its official end date.");
        }

        this.policyStatus = PolicyStatus.EXPIRED;

        // Hexagonal bonus: Register a domain event to notify other subdomains
        // this.registerEvent(new PolicyExpiredEvent(this.id));
    }

    public UUID getPolicyId() {
        return policyId;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public UUID getPolicyHolderId() {
        return policyHolderId;
    }

    public Money getPremium() {
        return premium;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public PolicyStatus getPolicyStatus() {
        return policyStatus;
    }
}
