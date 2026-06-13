package com.glow.payment.application.mappers;

import com.glow.payment.application.model.PaymentDto;
import com.glow.payment.domain.model.Payment;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mapper for converting Payment domain entities to PaymentDto.
 */
@ApplicationScoped
public class PaymentDtoMapper {
    
    public PaymentDto toDto(Payment payment) {
        if (payment == null) {
            return null;
        }
        
        PaymentDto dto = new PaymentDto();
        dto.id = payment.getId();
        dto.stripePaymentIntentId = payment.getStripePaymentIntentId();
        dto.stripeClientSecret = payment.getStripeClientSecret();
        dto.amount = payment.getAmount();
        dto.customerId = payment.getCustomerId();
        dto.orderId = payment.getOrderId();
        dto.status = payment.getStatus().toString();
        
        return dto;
    }
}
