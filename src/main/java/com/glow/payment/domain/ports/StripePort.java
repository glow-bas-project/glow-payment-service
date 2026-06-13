package com.glow.payment.domain.ports;

import java.util.UUID;

public interface StripePort {

    public record StripePaymentIntentResult(
        String paymentIntentId, 
        String clientSecret)
    {}

    StripePaymentIntentResult createPaymentIntent(Integer amount, UUID orderId, UUID customerId);

    String createTransfer(Integer amount, String destinationAccountId, String transferGroup);
}
