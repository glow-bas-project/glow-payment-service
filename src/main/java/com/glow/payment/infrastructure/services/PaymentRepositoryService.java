package com.glow.payment.infrastructure.services;

import com.glow.payment.domain.model.Payment;
import com.glow.payment.domain.model.PaymentStatus;
import com.glow.payment.domain.repository.PaymentRepository;
import com.glow.payment.infrastructure.mappers.PaymentEntityMapper;
import com.glow.payment.infrastructure.repository.PaymentJpaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Infrastructure-layer service implementing the domain PaymentRepository interface.
 * Handles persistence operations with MapStruct mapping between domain and JPA entities.
 */
@ApplicationScoped
public class PaymentRepositoryService implements PaymentRepository {
    
    private final PaymentJpaRepository jpaRepository;
    private final PaymentEntityMapper mapper;
    
    public PaymentRepositoryService(PaymentJpaRepository jpaRepository, PaymentEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    @Transactional
    public void save(Payment payment) {
        jpaRepository.persist(mapper.toEntity(payment));
    }
    
    @Override
    @Transactional
    public void update(Payment payment) {
        jpaRepository.getEntityManager().merge(mapper.toEntity(payment));
    }
    
    @Override
    @Transactional
    public Optional<Payment> findById(UUID id) {
        return jpaRepository.findByIdOptional(id)
            .map(mapper::toDomain);
    }
    
    @Override
    @Transactional
    public Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId) {
        return jpaRepository.findByStripePaymentIntentId(stripePaymentIntentId)
            .map(mapper::toDomain);
    }
    
    @Override
    @Transactional
    public List<Payment> findByOrderId(UUID orderId) {
        return jpaRepository.findByOrderId(orderId).stream()
            .map(mapper::toDomain)
            .toList();
    }
    
    @Override
    @Transactional
    public List<Payment> findByCustomerId(UUID customerId) {
        return jpaRepository.findByCustomerId(customerId).stream()
            .map(mapper::toDomain)
            .toList();
    }
    
    @Override
    @Transactional
    public List<Payment> findByStatus(PaymentStatus status) {
        return jpaRepository.findByStatus(status).stream()
            .map(mapper::toDomain)
            .toList();
    }
    
    @Override
    @Transactional
    public boolean deleteById(UUID id) {
        return jpaRepository.deleteById(id);
    }
}
