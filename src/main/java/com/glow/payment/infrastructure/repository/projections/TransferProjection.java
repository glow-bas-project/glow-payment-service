package com.glow.payment.infrastructure.repository.projections;

import java.time.Instant;
import java.util.UUID;

/**
 * Read-only projection of Transfer for query responses.
 * Used to fetch transfer data without loading all relationships.
 */
public class TransferProjection {
    
    public UUID id;
    public Instant createdAt;
    public Instant updatedAt;
    public String stripeTransferId;
    public Integer amount;
    public String recipientType;
    public String recipientAccountId;
    public UUID paymentId;
    
    public TransferProjection() {
    }
    
    public TransferProjection(UUID id, Instant createdAt, Instant updatedAt, String stripeTransferId,
                              Integer amount, String recipientType, String recipientAccountId, UUID paymentId) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.stripeTransferId = stripeTransferId;
        this.amount = amount;
        this.recipientType = recipientType;
        this.recipientAccountId = recipientAccountId;
        this.paymentId = paymentId;
    }
}
