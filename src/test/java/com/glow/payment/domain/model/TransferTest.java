package com.glow.payment.domain.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TransferTest {

    @Test
    public void constructor_createsTransferWithAllFields() {
        // given
        String stripeTransferId = "tr_test123";
        Integer amount = 2000;
        String recipientType = "RESTAURANT";
        String recipientAccountId = "acct_rest123";

        // when
        Transfer transfer = new Transfer(stripeTransferId, amount, recipientType, recipientAccountId);

        // then
        assertNotNull(transfer.getId());
        assertEquals(stripeTransferId, transfer.getStripeTransferId());
        assertEquals(amount, transfer.getAmount());
        assertEquals(recipientType, transfer.getRecipientType());
        assertEquals(recipientAccountId, transfer.getRecipientAccountId());
        assertNotNull(transfer.getCreatedAt());
        assertNotNull(transfer.getUpdatedAt());
    }

    @Test
    public void isForRestaurant_returnsTrue() {
        // given
        Transfer transfer = new Transfer("tr_test123", 2000, "RESTAURANT", "acct_rest123");

        // when
        boolean result = transfer.isForRestaurant();

        // then
        assertTrue(result);
    }

    @Test
    public void isForRestaurant_returnsFalseForCourier() {
        // given
        Transfer transfer = new Transfer("tr_test123", 900, "COURIER", "acct_courier456");

        // when
        boolean result = transfer.isForRestaurant();

        // then
        assertFalse(result);
    }

    @Test
    public void isForCourier_returnsTrue() {
        // given
        Transfer transfer = new Transfer("tr_test123", 900, "COURIER", "acct_courier456");

        // when
        boolean result = transfer.isForCourier();

        // then
        assertTrue(result);
    }

    @Test
    public void isForCourier_returnsFalseForRestaurant() {
        // given
        Transfer transfer = new Transfer("tr_test123", 2000, "RESTAURANT", "acct_rest123");

        // when
        boolean result = transfer.isForCourier();

        // then
        assertFalse(result);
    }
}
