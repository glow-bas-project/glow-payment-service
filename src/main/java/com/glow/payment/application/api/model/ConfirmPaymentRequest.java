package com.glow.payment.application.api.model;

/**
 * Request DTO for confirming a payment.
 * Received from Stripe webhook after successful payment.
 */
public class ConfirmPaymentRequest {
    public String stripePaymentIntentId;
}
