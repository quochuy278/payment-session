CREATE TABLE payment.checkout
(
    id         TEXT        PRIMARY KEY,
    amount     BIGINT      NOT NULL,
    currency   VARCHAR(3)  NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT checkout_amount_positive
        CHECK (amount > 0),

    CONSTRAINT checkout_currency_format
        CHECK (currency ~ '^[A-Z]{3}$')
);

COMMENT ON COLUMN payment.checkout.amount
    IS 'Monetary amount represented in minor units';