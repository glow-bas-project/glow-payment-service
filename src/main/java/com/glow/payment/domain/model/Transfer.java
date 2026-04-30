package com.glow.payment.domain.model;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain model for Transfer aggregate.
 * Represents a payout/transfer transaction from a payment.
 */
public class Transfer {
    
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String stripeTransferId;
    private Integer amount;
    private String recipientType;
    private String recipientAccountId;
    private Payment payment;
    
    public Transfer() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
    
    public Transfer(String stripeTransferId, Integer amount, String recipientType, String recipientAccountId) {
        this();
        this.stripeTransferId = stripeTransferId;
        this.amount = amount;
        this.recipientType = recipientType;
        this.recipientAccountId = recipientAccountId;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getStripeTransferId() {
        return stripeTransferId;
    }
    
    public void setStripeTransferId(String stripeTransferId) {
        this.stripeTransferId = stripeTransferId;
    }
    
    public Integer getAmount() {
        return amount;
    }
    
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
    public String getRecipientType() {
        return recipientType;
    }
    
    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }
    
    public String getRecipientAccountId() {
        return recipientAccountId;
    }
    
    public void setRecipientAccountId(String recipientAccountId) {
        this.recipientAccountId = recipientAccountId;
    }
    
    public Payment getPayment() {
        return payment;
    }
    
    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    
    // Helper Methods
    public boolean isForRestaurant() {
        return "RESTAURANT".equalsIgnoreCase(recipientType);
    }
    
    public boolean isForCourier() {
        return "COURIER".equalsIgnoreCase(recipientType);
    }
}

