package com.glow.payment.infrastructure.services;

import com.glow.payment.domain.model.Payment;
import com.glow.payment.domain.model.Transfer;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class TransferRepositoryServiceIntegrationTest {

    @Inject
    TransferRepositoryService transferRepository;

    @Inject
    PaymentRepositoryService paymentRepository;

    @Test
    @Transactional
    public void saveAndFindById_roundTrips() {
        // given
        var payment = new Payment("pi_transfer_test", 10000, UUID.randomUUID(), UUID.randomUUID());
        paymentRepository.save(payment);

        var transfer = new Transfer("tr_integration_test", 9000, "COURIER", "acct_stripe123");
        transfer.setPayment(payment);

        // when
        transferRepository.save(transfer);
        var found = transferRepository.findById(transfer.getId());

        // then
        assertTrue(found.isPresent());
        assertEquals("tr_integration_test", found.get().getStripeTransferId());
        assertEquals(9000, found.get().getAmount());
        assertEquals("COURIER", found.get().getRecipientType());
        assertEquals("acct_stripe123", found.get().getRecipientAccountId());
    }

    @Test
    @Transactional
    public void findByStripeTransferId_returnsPersistedTransfer() {
        // given
        var paymentStripeId = "pi_" + UUID.randomUUID().toString().substring(0, 24);
        var payment = new Payment(paymentStripeId, 10000, UUID.randomUUID(), UUID.randomUUID());
        paymentRepository.save(payment);

        var stripeId = "tr_" + UUID.randomUUID().toString().substring(0, 24);
        var transfer = new Transfer(stripeId, 5000, "RESTAURANT", "acct_restaurant");
        transfer.setPayment(payment);
        transferRepository.save(transfer);

        // when
        var found = transferRepository.findByStripeTransferId(stripeId);

        // then
        assertTrue(found.isPresent());
        assertEquals(5000, found.get().getAmount());
        assertEquals("RESTAURANT", found.get().getRecipientType());
    }

    @Test
    @Transactional
    public void findByStripeTransferId_returnsEmptyWhenNotFound() {
        // given
        var stripeId = "tr_nonexistent_" + UUID.randomUUID().toString();

        // when
        var found = transferRepository.findByStripeTransferId(stripeId);

        // then
        assertTrue(found.isEmpty());
    }

    @Test
    @Transactional
    public void findByPaymentId_returnsTransfersForPayment() {
        // given
        var payment1 = new Payment("pi_payment1", 10000, UUID.randomUUID(), UUID.randomUUID());
        var payment2 = new Payment("pi_payment2", 5000, UUID.randomUUID(), UUID.randomUUID());
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        var transfer1 = new Transfer("tr_payment1_1", 9000, "COURIER", "acct_courier1");
        transfer1.setPayment(payment1);
        var transfer2 = new Transfer("tr_payment1_2", 8000, "RESTAURANT", "acct_restaurant");
        transfer2.setPayment(payment1);
        var transfer3 = new Transfer("tr_payment2_1", 4000, "COURIER", "acct_courier2");
        transfer3.setPayment(payment2);

        transferRepository.save(transfer1);
        transferRepository.save(transfer2);
        transferRepository.save(transfer3);

        // when
        var transfers = transferRepository.findByPaymentId(payment1.getId());

        // then
        // Verify that we got transfers for payment1 by checking the specific stripe IDs
        assertTrue(transfers.stream()
            .anyMatch(t -> t.getStripeTransferId().equals("tr_payment1_1") && t.getAmount().equals(9000)));
        assertTrue(transfers.stream()
            .anyMatch(t -> t.getStripeTransferId().equals("tr_payment1_2") && t.getAmount().equals(8000)));
        // Verify we did NOT get transfer3 (from payment2)
        assertTrue(transfers.stream()
            .noneMatch(t -> t.getStripeTransferId().equals("tr_payment2_1")));
    }

    @Test
    @Transactional
    public void findByRecipientType_returnsTransfersWithType() {
        // given
        var paymentStripeId = "pi_" + UUID.randomUUID().toString().substring(0, 24);
        var payment = new Payment(paymentStripeId, 10000, UUID.randomUUID(), UUID.randomUUID());
        paymentRepository.save(payment);

        var courierTransfer = new Transfer("tr_courier", 5000, "COURIER", "acct_courier");
        courierTransfer.setPayment(payment);
        var restaurantTransfer = new Transfer("tr_restaurant", 4000, "RESTAURANT", "acct_restaurant");
        restaurantTransfer.setPayment(payment);
        var courierTransfer2 = new Transfer("tr_courier2", 3000, "COURIER", "acct_courier2");
        courierTransfer2.setPayment(payment);

        transferRepository.save(courierTransfer);
        transferRepository.save(restaurantTransfer);
        transferRepository.save(courierTransfer2);

        // when
        var courierTransfers = transferRepository.findByRecipientType("COURIER");
        var restaurantTransfers = transferRepository.findByRecipientType("RESTAURANT");

        // then
        assertTrue(courierTransfers.stream().allMatch(t -> "COURIER".equals(t.getRecipientType())));
        assertTrue(courierTransfers.stream()
            .anyMatch(t -> t.getStripeTransferId().equals("tr_courier")));
        assertTrue(courierTransfers.stream()
            .anyMatch(t -> t.getStripeTransferId().equals("tr_courier2")));

        assertTrue(restaurantTransfers.stream()
            .anyMatch(t -> "RESTAURANT".equals(t.getRecipientType()) && 
                          t.getStripeTransferId().equals("tr_restaurant")));
    }

    @Test
    @Transactional
    public void update_modifiesExistingTransfer() {
        // given
        var paymentStripeId = "pi_" + UUID.randomUUID().toString().substring(0, 24);
        var payment = new Payment(paymentStripeId, 10000, UUID.randomUUID(), UUID.randomUUID());
        paymentRepository.save(payment);

        var transfer = new Transfer("tr_update_test", 5000, "COURIER", "acct_courier");
        transfer.setPayment(payment);
        transferRepository.save(transfer);
        var transferId = transfer.getId();

        // when
        transfer.setAmount(5500);
        transferRepository.update(transfer);
        var found = transferRepository.findById(transferId);

        // then
        assertTrue(found.isPresent());
        assertEquals(5500, found.get().getAmount());
    }

    @Test
    @Transactional
    public void deleteById_removesTransfer() {
        // given
        var paymentStripeId = "pi_" + UUID.randomUUID().toString().substring(0, 24);
        var payment = new Payment(paymentStripeId, 10000, UUID.randomUUID(), UUID.randomUUID());
        paymentRepository.save(payment);

        var transfer = new Transfer("tr_delete_test", 5000, "COURIER", "acct_courier");
        transfer.setPayment(payment);
        transferRepository.save(transfer);
        var transferId = transfer.getId();

        // when
        var deleted = transferRepository.deleteById(transferId);
        var after = transferRepository.findById(transferId);

        // then
        assertTrue(deleted);
        assertTrue(after.isEmpty());
    }

    @Test
    @Transactional
    public void deleteById_returnsFalseWhenNotFound() {
        // given
        var transferId = UUID.randomUUID();

        // when
        var deleted = transferRepository.deleteById(transferId);

        // then
        assertFalse(deleted);
    }
}
