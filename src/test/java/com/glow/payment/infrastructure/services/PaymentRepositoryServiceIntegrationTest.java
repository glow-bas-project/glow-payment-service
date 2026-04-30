package com.glow.payment.infrastructure.services;

import com.glow.payment.domain.model.Payment;
import com.glow.payment.domain.model.PaymentStatus;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class PaymentRepositoryServiceIntegrationTest {

    @Inject
    PaymentRepositoryService repository;

    @Test
    @Transactional
    public void saveAndFindById_roundTrips() {
        // given
        var customerId = UUID.randomUUID();
        var orderId = UUID.randomUUID();
        var payment = new Payment("pi_integration_test", 10000, customerId, orderId);

        // when
        repository.save(payment);
        var found = repository.findById(payment.getId());

        // then
        assertTrue(found.isPresent());
        assertEquals("pi_integration_test", found.get().getStripePaymentIntentId());
        assertEquals(10000, found.get().getAmount());
        assertEquals(customerId, found.get().getCustomerId());
        assertEquals(orderId, found.get().getOrderId());
        assertEquals(PaymentStatus.PENDING, found.get().getStatus());
    }

    @Test
    @Transactional
    public void findByStripePaymentIntentId_returnsPersistedPayment() {
        // given
        var stripeId = "pi_" + UUID.randomUUID().toString().substring(0, 24);
        var payment = new Payment(stripeId, 5000, UUID.randomUUID(), UUID.randomUUID());
        repository.save(payment);

        // when
        var found = repository.findByStripePaymentIntentId(stripeId);

        // then
        assertTrue(found.isPresent());
        assertEquals(5000, found.get().getAmount());
    }

    @Test
    @Transactional
    public void findByStripePaymentIntentId_returnsEmptyWhenNotFound() {
        // given
        var stripeId = "pi_nonexistent_" + UUID.randomUUID().toString();

        // when
        var found = repository.findByStripePaymentIntentId(stripeId);

        // then
        assertTrue(found.isEmpty());
    }

    @Test
    @Transactional
    public void findByOrderId_returnsPaymentsForOrder() {
        // given
        var orderId = UUID.randomUUID();
        var customerId1 = UUID.randomUUID();
        var customerId2 = UUID.randomUUID();

        var payment1 = new Payment("pi_order1_1", 5000, customerId1, orderId);
        var payment2 = new Payment("pi_order1_2", 3000, customerId2, orderId);
        var payment3 = new Payment("pi_other_order", 2000, customerId1, UUID.randomUUID());

        repository.save(payment1);
        repository.save(payment2);
        repository.save(payment3);

        // when
        var payments = repository.findByOrderId(orderId);

        // then
        assertEquals(2, payments.size());
        assertTrue(payments.stream().allMatch(p -> p.getOrderId().equals(orderId)));
    }

    @Test
    @Transactional
    public void findByCustomerId_returnsPaymentsForCustomer() {
        // given
        var customerId = UUID.randomUUID();
        var orderId1 = UUID.randomUUID();
        var orderId2 = UUID.randomUUID();

        var payment1 = new Payment("pi_customer1_1", 5000, customerId, orderId1);
        var payment2 = new Payment("pi_customer1_2", 3000, customerId, orderId2);
        var payment3 = new Payment("pi_other_customer", 2000, UUID.randomUUID(), orderId1);

        repository.save(payment1);
        repository.save(payment2);
        repository.save(payment3);

        // when
        var payments = repository.findByCustomerId(customerId);

        // then
        assertEquals(2, payments.size());
        assertTrue(payments.stream().allMatch(p -> p.getCustomerId().equals(customerId)));
    }

    @Test
    @Transactional
    public void findByStatus_returnsPaymentsWithStatus() {
        // given
        var orderId = UUID.randomUUID();
        var customerId1 = UUID.randomUUID();
        var customerId2 = UUID.randomUUID();

        var pendingPayment = new Payment("pi_pending", 5000, customerId1, orderId);
        var succeededPayment = new Payment("pi_succeeded", 3000, customerId2, orderId);
        succeededPayment.markSucceeded();

        repository.save(pendingPayment);
        repository.save(succeededPayment);

        // when
        var succeeded = repository.findByStatus(PaymentStatus.SUCCEEDED);
        var pending = repository.findByStatus(PaymentStatus.PENDING);

        // then
        assertTrue(succeeded.stream()
            .anyMatch(p -> p.getStripePaymentIntentId().equals("pi_succeeded") && 
                          p.getStatus().equals(PaymentStatus.SUCCEEDED)));

        assertTrue(pending.stream()
            .anyMatch(p -> p.getStripePaymentIntentId().equals("pi_pending") && 
                          p.getStatus().equals(PaymentStatus.PENDING)));
    }

    @Test
    @Transactional
    public void update_modifiesExistingPayment() {
        // given
        var payment = new Payment("pi_update_test", 5000, UUID.randomUUID(), UUID.randomUUID());
        repository.save(payment);
        var paymentId = payment.getId();

        // when
        payment.markSucceeded();
        repository.update(payment);
        var found = repository.findById(paymentId);

        // then
        assertTrue(found.isPresent());
        assertEquals(PaymentStatus.SUCCEEDED, found.get().getStatus());
    }

    @Test
    @Transactional
    public void deleteById_removesPayment() {
        // given
        var payment = new Payment("pi_delete_test", 5000, UUID.randomUUID(), UUID.randomUUID());
        repository.save(payment);
        var paymentId = payment.getId();

        // when
        var deleted = repository.deleteById(paymentId);
        var after = repository.findById(paymentId);

        // then
        assertTrue(deleted);
        assertTrue(after.isEmpty());
    }

    @Test
    @Transactional
    public void deleteById_returnsFalseWhenNotFound() {
        // given
        var paymentId = UUID.randomUUID();

        // when
        var deleted = repository.deleteById(paymentId);

        // then
        assertFalse(deleted);
    }
}
