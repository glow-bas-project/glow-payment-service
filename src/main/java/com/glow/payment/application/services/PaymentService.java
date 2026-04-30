package com.glow.payment.application.services;

import com.glow.payment.application.api.model.*;
import com.glow.payment.application.mappers.PaymentDtoMapper;
import com.glow.payment.application.mappers.TransferDtoMapper;
import com.glow.payment.application.model.PaymentDto;
import com.glow.payment.application.model.TransferDto;
import com.glow.payment.domain.model.Payment;
import com.glow.payment.domain.model.Transfer;
import com.glow.payment.domain.repository.PaymentRepository;
import com.glow.payment.domain.repository.TransferRepository;
import com.glow.payment.domain.shared.DomainException;
import com.glow.payment.domain.shared.DomainPrecondition;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * PaymentService orchestrates payment operations.
 * Handles business logic for creating payment intents, confirming payments,
 * and managing transfers between service users and the platform.
 */
@ApplicationScoped
public class PaymentService {

    private static final Logger LOG = Logger.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final TransferRepository transferRepository;
    private final PaymentDtoMapper paymentDtoMapper;
    private final TransferDtoMapper transferDtoMapper;

    public PaymentService(PaymentRepository paymentRepository, TransferRepository transferRepository,
                          PaymentDtoMapper paymentDtoMapper, TransferDtoMapper transferDtoMapper) {
        this.paymentRepository = paymentRepository;
        this.transferRepository = transferRepository;
        this.paymentDtoMapper = paymentDtoMapper;
        this.transferDtoMapper = transferDtoMapper;
    }

    /**
     * Creates a new payment intent for an order.
     */
    public PaymentDto createPaymentIntent(CreatePaymentIntentRequest request, String userId) {
        LOG.infof("Creating payment intent for order %s, customerId %s, amount %d",
                request.orderId, request.customerId, request.amount);

        // Validate request
        DomainPrecondition.notNull(request.orderId, "orderId is required");
        DomainPrecondition.notNull(request.customerId, "customerId is required");
        DomainPrecondition.notNull(request.amount, "amount is required");
        DomainPrecondition.assertTrue(request.amount > 0, "amount must be greater than 0");

        // TODO: Validate that customerId matches authenticated userId for authorization
        // This will be implemented when integrating with OrderService validation

        // TODO: Call Stripe API to create PaymentIntent
        // For now, generate a mock Stripe intent ID
        String stripePaymentIntentId = "pi_" + UUID.randomUUID().toString().substring(0, 24);

        // Create Payment entity
        Payment payment = new Payment(stripePaymentIntentId, request.amount, request.customerId, request.orderId);
        paymentRepository.save(payment);

        LOG.infof("Payment intent created: %s", payment.getId());
        return paymentDtoMapper.toDto(payment);
    }

    /**
     * Confirms a payment by marking it as succeeded.
     */
    public PaymentDto confirmPayment(UUID paymentId, ConfirmPaymentRequest request, String userId) {
        LOG.infof("Confirming payment %s with Stripe confirmation", paymentId);

        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            throw new DomainException("Payment not found: " + paymentId);
        }

        Payment payment = optionalPayment.get();

        // TODO: Validate ownership - ensure authenticated user has access to this payment
        // TODO: Validate ownership - ensure authenticated user has access to this payment
        // For now, trust the webhook signature is valid

        payment.markSucceeded();
        payment.setUpdatedAt(Instant.now());
        paymentRepository.update(payment);

        LOG.infof("Payment %s marked as succeeded", paymentId);
        return paymentDtoMapper.toDto(payment);
    }

    /**
     * Retrieves a payment by its ID.
     */
    public PaymentDto getPayment(UUID paymentId, String userId) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);

        if (optionalPayment.isEmpty()) {
            throw new DomainException("Payment not found: " + paymentId);
        }

        // TODO: Validate ownership - ensure authenticated user has access to this payment
        return paymentDtoMapper.toDto(optionalPayment.get());
    }

    /**
     * Retrieves a payment by its Stripe Payment Intent ID.
     */
    public PaymentDto getPaymentByStripeId(String stripePaymentIntentId, String userId) {
        Optional<Payment> optionalPayment = paymentRepository.findByStripePaymentIntentId(stripePaymentIntentId);

        if (optionalPayment.isEmpty()) {
            throw new DomainException("Payment not found: " + stripePaymentIntentId);
        }

        // TODO: Validate ownership - ensure authenticated user has access to this payment
        return paymentDtoMapper.toDto(optionalPayment.get());
    }

    /**
     * Creates a transfer (payout) from a payment to a recipient.
     */
    public TransferDto createTransfer(UUID paymentId, CreateTransferRequest request, String userId) {
        LOG.infof("Creating transfer for payment %s to %s", paymentId, request.recipientType);

        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            throw new DomainException("Payment not found: " + paymentId);
        }

        // Validate request
        DomainPrecondition.notNull(request.amount, "amount is required");
        DomainPrecondition.assertTrue(request.amount > 0, "amount must be greater than 0");
        DomainPrecondition.notEmpty(request.recipientAccountId, "recipientAccountId is required");

        Payment payment = optionalPayment.get();

        // TODO: Call Stripe API to create Transfer
        String stripeTransferId = "tr_" + UUID.randomUUID().toString().substring(0, 24);

        // Create Transfer entity
        Transfer transfer = new Transfer(stripeTransferId, request.amount, request.recipientType, request.recipientAccountId);
        transfer.setPayment(payment);
        transferRepository.save(transfer);

        // Add to payment's transfers list
        payment.getTransfers().add(transfer);
        payment.setUpdatedAt(Instant.now());
        paymentRepository.update(payment);

        LOG.infof("Transfer created: %s", transfer.getId());
        return transferDtoMapper.toDto(transfer);
    }
}
