package com.glow.payment.application.api.model;

import java.util.UUID;

/**
 * Request DTO for creating a payment intent.
 * Received from Order Service when a customer initiates checkout.
 */
public class CreatePaymentIntentRequest {
    public UUID orderId;
    public UUID customerId;
    public Integer amount;
}
