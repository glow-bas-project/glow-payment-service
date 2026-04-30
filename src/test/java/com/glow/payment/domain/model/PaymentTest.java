package com.glow.payment.domain.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    @Test
    public void constructor_createsPaymentWithPendingStatus() {
        // given
        String stripePaymentIntentId = "pi_test123";
        Integer amount = 2999;
        UUID customerId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        // when
        Payment payment = new Payment(stripePaymentIntentId, amount, customerId, orderId);

        // then
        assertNotNull(payment.getId());
        assertEquals(stripePaymentIntentId, payment.getStripePaymentIntentId());
        assertEquals(amount, payment.getAmount());
        assertEquals(customerId, payment.getCustomerId());
        assertEquals(orderId, payment.getOrderId());
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
        assertNotNull(payment.getCreatedAt());
        assertNotNull(payment.getUpdatedAt());
    }

    @Test
    public void markSucceeded_updatesStatusToSucceeded() {
        // given
        Payment payment = new Payment("pi_test123", 2999, UUID.randomUUID(), UUID.randomUUID());

        // when
        payment.markSucceeded();

        // then
        assertEquals(PaymentStatus.SUCCEEDED, payment.getStatus());
    }

    @Test
    public void markFailed_updatesStatusToFailed() {
        // given
        Payment payment = new Payment("pi_test123", 2999, UUID.randomUUID(), UUID.randomUUID());

        // when
        payment.markFailed();

        // then
        assertEquals(PaymentStatus.FAILED, payment.getStatus());
    }

    @Test
    public void markRefunded_updatesStatusToRefunded() {
        // given
        Payment payment = new Payment("pi_test123", 2999, UUID.randomUUID(), UUID.randomUUID());

        // when
        payment.markRefunded();

        // then
        assertEquals(PaymentStatus.REFUNDED, payment.getStatus());
    }

    @Test
    public void addTransfer_addsTransferToList() {
        // given
        Payment payment = new Payment("pi_test123", 2999, UUID.randomUUID(), UUID.randomUUID());
        Transfer transfer = new Transfer("tr_test456", 2000, "RESTAURANT", "acct_rest123");

        // when
        payment.addTransfer(transfer);

        // then
        assertEquals(1, payment.getTransfers().size());
        assertTrue(payment.getTransfers().contains(transfer));
    }

    @Test
    public void addTransfer_multipleTransfers() {
        // given
        Payment payment = new Payment("pi_test123", 2999, UUID.randomUUID(), UUID.randomUUID());
        Transfer transfer1 = new Transfer("tr_test456", 2000, "RESTAURANT", "acct_rest123");
        Transfer transfer2 = new Transfer("tr_test789", 999, "COURIER", "acct_courier456");

        // when
        payment.addTransfer(transfer1);
        payment.addTransfer(transfer2);

        // then
        assertEquals(2, payment.getTransfers().size());
        assertTrue(payment.getTransfers().contains(transfer1));
        assertTrue(payment.getTransfers().contains(transfer2));
    }
}
