package com.glow.payment.infrastructure.repository.entities;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transfers")
public class TransferJpaEntity {
    
    @Id
    @Column(nullable = false, unique = true)
    private UUID id;
    
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
    
    @Column(name = "stripe_transfer_id", nullable = false, unique = true)
    private String stripeTransferId;
    
    @Column(nullable = false)
    private Integer amount;
    
    @Column(name = "recipient_type", nullable = false)
    private String recipientType;
    
    @Column(name = "recipient_account_id", nullable = false)
    private String recipientAccountId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private PaymentJpaEntity payment;
    
    public TransferJpaEntity() {
    }
    
    public TransferJpaEntity(UUID id, String stripeTransferId, Integer amount, String recipientType, String recipientAccountId, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.stripeTransferId = stripeTransferId;
        this.amount = amount;
        this.recipientType = recipientType;
        this.recipientAccountId = recipientAccountId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public TransferJpaEntity(UUID id, String stripeTransferId, Integer amount, String recipientType, String recipientAccountId, PaymentJpaEntity payment, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.stripeTransferId = stripeTransferId;
        this.amount = amount;
        this.recipientType = recipientType;
        this.recipientAccountId = recipientAccountId;
        this.payment = payment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
    
    public PaymentJpaEntity getPayment() {
        return payment;
    }
    
    public void setPayment(PaymentJpaEntity payment) {
        this.payment = payment;
    }
    
    // Helper methods
    public boolean isForRestaurant() {
        return "RESTAURANT".equalsIgnoreCase(recipientType);
    }
    
    public boolean isForCourier() {
        return "COURIER".equalsIgnoreCase(recipientType);
    }
}
