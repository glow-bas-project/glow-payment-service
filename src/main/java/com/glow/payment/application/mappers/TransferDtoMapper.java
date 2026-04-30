package com.glow.payment.application.mappers;

import com.glow.payment.application.model.TransferDto;
import com.glow.payment.domain.model.Transfer;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mapper for converting Transfer domain entities to TransferDto.
 */
@ApplicationScoped
public class TransferDtoMapper {
    
    public TransferDto toDto(Transfer transfer) {
        if (transfer == null) {
            return null;
        }
        
        TransferDto dto = new TransferDto();
        dto.id = transfer.getId();
        dto.stripeTransferId = transfer.getStripeTransferId();
        dto.amount = transfer.getAmount();
        dto.recipientType = transfer.getRecipientType();
        dto.recipientAccountId = transfer.getRecipientAccountId();
        
        return dto;
    }
}
