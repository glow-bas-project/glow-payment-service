package com.glow.payment.infrastructure.repository;

import com.glow.payment.infrastructure.repository.entities.TransferJpaEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TransferJpaRepository implements PanacheRepositoryBase<TransferJpaEntity, UUID> {
    
    public Optional<TransferJpaEntity> findByStripeTransferId(String stripeTransferId) {
        return find("stripeTransferId", stripeTransferId).firstResultOptional();
    }
    
    public List<TransferJpaEntity> findByPaymentId(UUID paymentId) {
        return find("payment.id", paymentId).list();
    }
    
    public List<TransferJpaEntity> findByRecipientType(String recipientType) {
        return find("recipientType", recipientType).list();
    }
}
