--liquibase formatted sql
--changeset sarh:25_04_2026-1
CREATE TABLE IF NOT EXISTS payments (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    stripe_payment_intent_id VARCHAR(255) NOT NULL UNIQUE,
    amount INTEGER NOT NULL,
    customer_id UUID NOT NULL,
    order_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL
);

--changeset sarh:25_04_2026-2
CREATE TABLE IF NOT EXISTS transfers (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    stripe_transfer_id VARCHAR(255) NOT NULL UNIQUE,
    amount INTEGER NOT NULL,
    recipient_type VARCHAR(50) NOT NULL,
    recipient_account_id VARCHAR(255) NOT NULL,
    payment_id UUID NOT NULL,
    CONSTRAINT fk_transfers_payment FOREIGN KEY (payment_id) REFERENCES payments(id)
);
