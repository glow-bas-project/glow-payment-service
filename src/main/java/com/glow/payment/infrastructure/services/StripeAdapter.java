package com.glow.payment.infrastructure.services;

import com.glow.payment.domain.ports.StripePort;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Transfer;
import com.stripe.net.RequestOptions;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class StripeAdapter implements StripePort {

    private static final Logger LOG = Logger.getLogger(StripeAdapter.class);

    @ConfigProperty(name = "glow.payment.stripe.secret-key")
    String secretKey;

    @ConfigProperty(name = "glow.payment.stripe.base-url")
    String baseUrl;

    @PostConstruct
    void init() {
        Stripe.apiKey = secretKey;
        Stripe.overrideApiBase(baseUrl);
        LOG.infof("Stripe adapter initialised – base URL: %s", baseUrl);
    }

    @Override
    public StripePaymentIntentResult createPaymentIntent(Integer amount, UUID orderId, UUID customerId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("amount", amount);
            params.put("currency", "dkk");
            params.put("metadata", Map.of(
                    "orderId", orderId.toString(),
                    "customerId", customerId.toString()));

            PaymentIntent intent = PaymentIntent.create(params);
            LOG.infof("Stripe PaymentIntent created: %s", intent.getId());
            return new StripePaymentIntentResult(intent.getId(), intent.getClientSecret());
        } catch (StripeException e) {
            throw new com.glow.payment.domain.shared.DomainException(
                    "Failed to create Stripe PaymentIntent: " + e.getMessage());
        }
    }

    @Override
    public String createTransfer(Integer amount, String destinationAccountId, String transferGroup) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("amount", amount);
            params.put("currency", "dkk");
            params.put("destination", destinationAccountId);
            params.put("transfer_group", transferGroup);

            Transfer transfer = Transfer.create(params);
            LOG.infof("Stripe Transfer created: %s", transfer.getId());
            return transfer.getId();
        } catch (StripeException e) {
            throw new com.glow.payment.domain.shared.DomainException(
                    "Failed to create Stripe Transfer: " + e.getMessage());
        }
    }
}