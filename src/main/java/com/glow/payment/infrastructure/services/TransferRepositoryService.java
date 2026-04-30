package com.glow.payment.infrastructure.services;

import com.glow.payment.domain.model.Transfer;
import com.glow.payment.domain.repository.TransferRepository;
import com.glow.payment.infrastructure.mappers.TransferEntityMapper;
import com.glow.payment.infrastructure.repository.PaymentJpaRepository;
import com.glow.payment.infrastructure.repository.TransferJpaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Infrastructure-layer service implementing the domain TransferRepository interface.
 * Handles persistence operations with MapStruct mapping between domain and JPA entities.
 */
@ApplicationScoped
public class TransferRepositoryService implements TransferRepository {
    
    private final TransferJpaRepository jpaRepository;
    private final PaymentJpaRepository paymentJpaRepository;
    private final TransferEntityMapper mapper;
    
    public TransferRepositoryService(TransferJpaRepository jpaRepository, PaymentJpaRepository paymentJpaRepository, TransferEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.paymentJpaRepository = paymentJpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    @Transactional
    public void save(Transfer transfer) {
        var entity = mapper.toEntity(transfer);
        var managedPayment = paymentJpaRepository.findById(transfer.getPayment().getId());
        entity.setPayment(managedPayment);
        jpaRepository.persist(entity);
    }
    
    @Override
    @Transactional
    public void update(Transfer transfer) {
        var entity = mapper.toEntity(transfer);
        var managedPayment = paymentJpaRepository.findById(transfer.getPayment().getId());
        entity.setPayment(managedPayment);
        jpaRepository.getEntityManager().merge(entity);
    }
    
    @Override
    @Transactional
    public Optional<Transfer> findById(UUID id) {
        return jpaRepository.findByIdOptional(id)
            .map(mapper::toDomain);
    }
    
    @Override
    @Transactional
    public Optional<Transfer> findByStripeTransferId(String stripeTransferId) {
        return jpaRepository.findByStripeTransferId(stripeTransferId)
            .map(mapper::toDomain);
    }
    
    @Override
    @Transactional
    public List<Transfer> findByPaymentId(UUID paymentId) {
        return jpaRepository.findByPaymentId(paymentId).stream()
            .map(mapper::toDomain)
            .toList();
    }
    
    @Override
    @Transactional
    public List<Transfer> findByRecipientType(String recipientType) {
        return jpaRepository.findByRecipientType(recipientType).stream()
            .map(mapper::toDomain)
            .toList();
    }
    
    @Override
    @Transactional
    public boolean deleteById(UUID id) {
        return jpaRepository.deleteById(id);
    }
}
