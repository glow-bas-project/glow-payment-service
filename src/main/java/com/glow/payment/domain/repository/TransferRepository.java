package com.glow.payment.domain.repository;

import com.glow.payment.domain.model.Transfer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Domain repository interface for Transfer aggregate.
 * Defines the contract for persistence operations on Transfers.
 */
public interface TransferRepository {
    
    void save(Transfer transfer);
    
    void update(Transfer transfer);
    
    Optional<Transfer> findById(UUID id);
    
    Optional<Transfer> findByStripeTransferId(String stripeTransferId);
    
    List<Transfer> findByPaymentId(UUID paymentId);
    
    List<Transfer> findByRecipientType(String recipientType);
    
    boolean deleteById(UUID id);
}
