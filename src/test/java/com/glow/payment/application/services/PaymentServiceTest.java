package com.glow.payment.application.services;

import com.glow.payment.application.api.model.ConfirmPaymentRequest;
import com.glow.payment.application.api.model.CreatePaymentIntentRequest;
import com.glow.payment.application.api.model.CreateTransferRequest;
import com.glow.payment.application.mappers.PaymentDtoMapper;
import com.glow.payment.application.mappers.TransferDtoMapper;
import com.glow.payment.application.model.PaymentDto;
import com.glow.payment.application.model.TransferDto;
import com.glow.payment.domain.model.Payment;
import com.glow.payment.domain.model.PaymentStatus;
import com.glow.payment.domain.model.Transfer;
import com.glow.payment.domain.repository.PaymentRepository;
import com.glow.payment.domain.repository.TransferRepository;
import com.glow.payment.domain.shared.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    TransferRepository transferRepository;

    @Mock
    PaymentDtoMapper paymentDtoMapper;

    @Mock
    TransferDtoMapper transferDtoMapper;

    @InjectMocks
    PaymentService service;

    @Test
    public void createPaymentIntent_savesAndMapsToDto() {
        // given
        var orderId = UUID.randomUUID();
        var customerId = UUID.randomUUID();
        var userId = "user-123";
        var request = new CreatePaymentIntentRequest();
        request.orderId = orderId;
        request.customerId = customerId;
        request.amount = 10000;

        when(paymentDtoMapper.toDto(any(Payment.class))).thenAnswer(invocation -> {
            Payment p = invocation.getArgument(0);
            var dto = new PaymentDto();
            dto.id = p.getId();
            dto.stripePaymentIntentId = p.getStripePaymentIntentId();
            dto.amount = p.getAmount();
            dto.customerId = p.getCustomerId();
            dto.orderId = p.getOrderId();
            dto.status = p.getStatus().toString();
            return dto;
        });

        // when
        var dto = service.createPaymentIntent(request, userId);

        // then
        assertEquals(10000, dto.amount);
        assertEquals(orderId, dto.orderId);
        assertEquals(customerId, dto.customerId);
        verify(paymentRepository).save(any(Payment.class));
        verify(paymentDtoMapper).toDto(any(Payment.class));
    }

    @Test
    public void createPaymentIntent_throwsWhenOrderIdNull() {
        // given
        var request = new CreatePaymentIntentRequest();
        request.orderId = null;
        request.customerId = UUID.randomUUID();
        request.amount = 10000;

        // when / then
        assertThrows(DomainException.class, () -> service.createPaymentIntent(request, "user-123"));
    }

    @Test
    public void createPaymentIntent_throwsWhenAmountInvalid() {
        // given
        var request = new CreatePaymentIntentRequest();
        request.orderId = UUID.randomUUID();
        request.customerId = UUID.randomUUID();
        request.amount = -100;

        // when / then
        assertThrows(DomainException.class, () -> service.createPaymentIntent(request, "user-123"));
    }

    @Test
    public void confirmPayment_returnsSucceededPayment() {
        // given
        var paymentId = UUID.randomUUID();
        var payment = new Payment("pi_test123", 10000, UUID.randomUUID(), UUID.randomUUID());
        payment.setId(paymentId);

        var request = new ConfirmPaymentRequest();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(paymentDtoMapper.toDto(any(Payment.class))).thenAnswer(invocation -> {
            Payment p = invocation.getArgument(0);
            var dto = new PaymentDto();
            dto.id = p.getId();
            dto.status = p.getStatus().toString();
            return dto;
        });

        // when
        var dto = service.confirmPayment(paymentId, request, "user-123");

        // then
        assertEquals(PaymentStatus.SUCCEEDED.toString(), dto.status);
        verify(paymentRepository).findById(paymentId);
        verify(paymentRepository).update(any(Payment.class));
        verify(paymentDtoMapper).toDto(any(Payment.class));
    }

    @Test
    public void confirmPayment_throwsWhenPaymentNotFound() {
        // given
        var paymentId = UUID.randomUUID();
        var request = new ConfirmPaymentRequest();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(DomainException.class, () -> service.confirmPayment(paymentId, request, "user-123"));
    }

    @Test
    public void getPayment_returnsDtoWhenPresent() {
        // given
        var paymentId = UUID.randomUUID();
        var payment = new Payment("pi_test123", 10000, UUID.randomUUID(), UUID.randomUUID());
        payment.setId(paymentId);

        var expected = new PaymentDto();
        expected.id = paymentId;
        expected.stripePaymentIntentId = "pi_test123";
        expected.amount = 10000;

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(paymentDtoMapper.toDto(payment)).thenReturn(expected);

        // when
        var actual = service.getPayment(paymentId, "user-123");

        // then
        assertEquals(paymentId, actual.id);
        assertEquals("pi_test123", actual.stripePaymentIntentId);
        assertEquals(10000, actual.amount);
    }

    @Test
    public void getPayment_throwsWhenNotFound() {
        // given
        var paymentId = UUID.randomUUID();
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(DomainException.class, () -> service.getPayment(paymentId, "user-123"));
    }

    @Test
    public void getPaymentByStripeId_returnsDtoWhenPresent() {
        // given
        var stripeId = "pi_test123";
        var paymentId = UUID.randomUUID();
        var payment = new Payment(stripeId, 10000, UUID.randomUUID(), UUID.randomUUID());
        payment.setId(paymentId);

        var expected = new PaymentDto();
        expected.id = paymentId;
        expected.stripePaymentIntentId = stripeId;
        expected.amount = 10000;

        when(paymentRepository.findByStripePaymentIntentId(stripeId)).thenReturn(Optional.of(payment));
        when(paymentDtoMapper.toDto(payment)).thenReturn(expected);

        // when
        var actual = service.getPaymentByStripeId(stripeId, "user-123");

        // then
        assertEquals(stripeId, actual.stripePaymentIntentId);
    }

    @Test
    public void getPaymentByStripeId_throwsWhenNotFound() {
        // given
        var stripeId = "pi_missing";
        when(paymentRepository.findByStripePaymentIntentId(stripeId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(DomainException.class, () -> service.getPaymentByStripeId(stripeId, "user-123"));
    }

    @Test
    public void createTransfer_savesAndMapsToDto() {
        // given
        var paymentId = UUID.randomUUID();
        var recipientAccountId = "acct_stripe123";
        var payment = new Payment("pi_test123", 10000, UUID.randomUUID(), UUID.randomUUID());
        payment.setId(paymentId);

        var request = new CreateTransferRequest();
        request.amount = 9000;
        request.recipientType = "COURIER";
        request.recipientAccountId = recipientAccountId;

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(transferDtoMapper.toDto(any(Transfer.class))).thenAnswer(invocation -> {
            Transfer t = invocation.getArgument(0);
            var dto = new TransferDto();
            dto.id = t.getId();
            dto.stripeTransferId = t.getStripeTransferId();
            dto.amount = t.getAmount();
            dto.recipientType = t.getRecipientType();
            dto.recipientAccountId = t.getRecipientAccountId();
            return dto;
        });

        // when
        var dto = service.createTransfer(paymentId, request, "user-123");

        // then
        assertEquals(9000, dto.amount);
        assertEquals("COURIER", dto.recipientType);
        assertEquals(recipientAccountId, dto.recipientAccountId);
        verify(transferRepository).save(any(Transfer.class));
        verify(paymentRepository).update(any(Payment.class));
        verify(transferDtoMapper).toDto(any(Transfer.class));
    }

    @Test
    public void createTransfer_throwsWhenPaymentNotFound() {
        // given
        var paymentId = UUID.randomUUID();
        var request = new CreateTransferRequest();
        request.amount = 9000;
        request.recipientType = "COURIER";
        request.recipientAccountId = "acct_stripe123";

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(DomainException.class, () -> service.createTransfer(paymentId, request, "user-123"));
    }

    @Test
    public void createTransfer_throwsWhenAmountInvalid() {
        // given
        var paymentId = UUID.randomUUID();
        var payment = new Payment("pi_test123", 10000, UUID.randomUUID(), UUID.randomUUID());
        payment.setId(paymentId);

        var request = new CreateTransferRequest();
        request.amount = 0;
        request.recipientType = "COURIER";
        request.recipientAccountId = "acct_stripe123";

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        // when / then
        assertThrows(DomainException.class, () -> service.createTransfer(paymentId, request, "user-123"));
    }
}
