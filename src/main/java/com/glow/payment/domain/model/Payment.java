package com.glow.payment.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Domain model for Payment aggregate.
 * Represents a payment transaction in the payment domain.
 */
public class Payment {
    
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String stripePaymentIntentId;
    private String stripeClientSecret;
    private Integer amount;
    private UUID customerId;
    private UUID orderId;
    private PaymentStatus status;
    private List<Transfer> transfers;
    
    public Payment() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.status = PaymentStatus.PENDING;
        this.transfers = new ArrayList<>();
    }
    
    public Payment(String stripePaymentIntentId, Integer amount, UUID customerId, UUID orderId) {
        this();
        this.stripePaymentIntentId = stripePaymentIntentId;
        this.amount = amount;
        this.customerId = customerId;
        this.orderId = orderId;
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
    
    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }
    
    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public String getStripeClientSecret() { 
        return stripeClientSecret; 
    }
    
    public void setStripeClientSecret(String stripeClientSecret) {
        this.stripeClientSecret = stripeClientSecret;
    }

    public Integer getAmount() {
        return amount;
    }
    
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
    public UUID getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }
    
    public UUID getOrderId() {
        return orderId;
    }
    
    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
    
    public PaymentStatus getStatus() {
        return status;
    }
    
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
    
    public List<Transfer> getTransfers() {
        return transfers;
    }
    
    public void setTransfers(List<Transfer> transfers) {
        this.transfers = transfers == null ? new ArrayList<>() : new ArrayList<>(transfers);
    }
    
    // Domain Methods
    public void markSucceeded() {
        this.status = PaymentStatus.SUCCEEDED;
        this.updatedAt = Instant.now();
    }
    
    public void markFailed() {
        this.status = PaymentStatus.FAILED;
        this.updatedAt = Instant.now();
    }
    
    public void markRefunded() {
        this.status = PaymentStatus.REFUNDED;
        this.updatedAt = Instant.now();
    }
    
    public void addTransfer(Transfer transfer) {
        this.transfers.add(transfer);
        transfer.setPayment(this);
    }
}

