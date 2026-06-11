package com.glow.payment.application.model;

import java.util.UUID;

/**
 * Application-layer DTO for Payment.
 * Used for data transfer between application and API layers.
 */
public class PaymentDto {
    public UUID id;
    public String stripePaymentIntentId;
    public String stripeClientSecret;
    public Integer amount;
    public UUID customerId;
    public UUID orderId;
    public String status;
}
