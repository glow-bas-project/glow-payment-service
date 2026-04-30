package com.glow.payment.infrastructure.mappers;

import com.glow.payment.domain.model.Transfer;
import com.glow.payment.infrastructure.repository.entities.TransferJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface TransferEntityMapper {
    
    @Mapping(target = "payment", ignore = true)
    Transfer toDomain(TransferJpaEntity entity);
    
    @Mapping(target = "payment", ignore = true)
    TransferJpaEntity toEntity(Transfer transfer);
}
