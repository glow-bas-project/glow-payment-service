package com.glow.payment.application.api;

import com.glow.payment.application.api.model.*;
import com.glow.payment.application.model.PaymentDto;
import com.glow.payment.application.model.TransferDto;
import com.glow.payment.application.services.PaymentService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.UUID;

@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentController {

    private static final Logger LOG = Logger.getLogger(PaymentController.class);
    private static final String ANONYMOUS_USER = "anonymous";

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }
    
    // ========== CREATE PAYMENT INTENT ==========
    
    @POST
    @Path("/intents")
    public RestResponse<PaymentDto> createPaymentIntent(CreatePaymentIntentRequest request) {
        PaymentDto dto = service.createPaymentIntent(request, ANONYMOUS_USER);
        return RestResponse.status(RestResponse.Status.CREATED, dto);
    }
    
    // ========== CONFIRM PAYMENT ==========
    
    @POST
    @Path("/{paymentId}/confirm")
    public RestResponse<PaymentDto> confirmPayment(@PathParam("paymentId") UUID paymentId, ConfirmPaymentRequest request) {
        PaymentDto dto = service.confirmPayment(paymentId, request, ANONYMOUS_USER);
        return RestResponse.ok(dto);
    }
    
    @GET
    @Path("/{paymentId}")
    public RestResponse<PaymentDto> getPayment(@PathParam("paymentId") UUID paymentId) {
        PaymentDto dto = service.getPayment(paymentId, ANONYMOUS_USER);
        return RestResponse.ok(dto);
    }

    @GET
    @Path("/stripe/{stripePaymentIntentId}")
    public RestResponse<PaymentDto> getPaymentByStripeId(@PathParam("stripePaymentIntentId") String stripePaymentIntentId) {
        PaymentDto dto = service.getPaymentByStripeId(stripePaymentIntentId, ANONYMOUS_USER);
        return RestResponse.ok(dto);
    }
    
    @POST
    @Path("/{paymentId}/transfers")
    public RestResponse<TransferDto> createTransfer(@PathParam("paymentId") UUID paymentId, CreateTransferRequest request) {
        TransferDto dto = service.createTransfer(paymentId, request, ANONYMOUS_USER);
        return RestResponse.status(RestResponse.Status.CREATED, dto);
    }
}
