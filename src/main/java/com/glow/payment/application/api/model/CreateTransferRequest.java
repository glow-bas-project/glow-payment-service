package com.glow.payment.application.api.model;

/**
 * Request DTO for creating a transfer (payout).
 * Received from Order Service after a successful payment.
 */
public class CreateTransferRequest {
    public Integer amount;
    public String recipientType;  // "RESTAURANT" or "COURIER"
    public String recipientAccountId;
}
