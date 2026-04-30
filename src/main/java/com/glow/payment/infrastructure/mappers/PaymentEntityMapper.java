package com.glow.payment.infrastructure.mappers;

import com.glow.payment.domain.model.Payment;
import com.glow.payment.infrastructure.repository.entities.PaymentJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface PaymentEntityMapper {
    
    @Mapping(target = "transfers", ignore = true)
    Payment toDomain(PaymentJpaEntity entity);
    
    @Mapping(target = "transfers", ignore = true)
    PaymentJpaEntity toEntity(Payment payment);
}