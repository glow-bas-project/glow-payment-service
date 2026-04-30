package com.glow.payment.application.model;

import java.util.UUID;

/**
 * Application-layer DTO for Transfer.
 * Used for data transfer between application and API layers.
 */
public class TransferDto {
    public UUID id;
    public String stripeTransferId;
    public Integer amount;
    public String recipientType;
    public String recipientAccountId;
}
