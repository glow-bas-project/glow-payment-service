package com.glow.payment.infrastructure.repository;

import com.glow.payment.domain.model.PaymentStatus;
import com.glow.payment.infrastructure.repository.entities.PaymentJpaEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PaymentJpaRepository implements PanacheRepositoryBase<PaymentJpaEntity, UUID> {
    
    public Optional<PaymentJpaEntity> findByStripePaymentIntentId(String stripePaymentIntentId) {
        return find("stripePaymentIntentId", stripePaymentIntentId).firstResultOptional();
    }
    
    public List<PaymentJpaEntity> findByOrderId(UUID orderId) {
        return find("orderId", orderId).list();
    }
    
    public List<PaymentJpaEntity> findByCustomerId(UUID customerId) {
        return find("customerId", customerId).list();
    }
    
    public List<PaymentJpaEntity> findByStatus(PaymentStatus status) {
        return find("status", status).list();
    }
}
