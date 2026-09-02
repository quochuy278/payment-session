CREATE TABLE payment.payment_session
(
    id               TEXT         NOT NULL,
    checkout_id      TEXT         NOT NULL,
    status           VARCHAR(32)  NOT NULL,
    psp              TEXT         NOT NULL,
    amount           BIGINT       NOT NULL,
    currency         VARCHAR(3)   NOT NULL,
    idempotency_key  TEXT         NOT NULL,
    reference_id     TEXT,
    surcharge_amount BIGINT       NOT NULL DEFAULT 0,
    tip_amount       BIGINT       NOT NULL DEFAULT 0,
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT payment_session_pkey
        PRIMARY KEY (id),

    CONSTRAINT payment_session_checkout_fk
        FOREIGN KEY (checkout_id)
        REFERENCES payment.checkout (id)
        ON DELETE RESTRICT,

    CONSTRAINT payment_session_amount_positive
        CHECK (amount > 0),

    CONSTRAINT payment_session_surcharge_non_negative
        CHECK (surcharge_amount >= 0),

    CONSTRAINT payment_session_tip_non_negative
        CHECK (tip_amount >= 0),

    CONSTRAINT payment_session_currency_format
        CHECK (currency ~ '^[A-Z]{3}$'),

    CONSTRAINT payment_session_status_not_blank
        CHECK (btrim(status) <> ''),

    CONSTRAINT payment_session_psp_not_blank
        CHECK (btrim(psp) <> ''),

    CONSTRAINT payment_session_idempotency_key_not_blank
        CHECK (btrim(idempotency_key) <> ''),

    CONSTRAINT payment_session_reference_not_blank
        CHECK (reference_id IS NULL OR btrim(reference_id) <> ''),

    CONSTRAINT payment_session_checkout_idempotency_key_unique
        UNIQUE (checkout_id, idempotency_key),

    CONSTRAINT payment_session_psp_reference_unique
        UNIQUE (psp, reference_id)
);

COMMENT ON COLUMN payment.payment_session.amount
    IS 'Checkout amount reserved by this payment session, in minor units';

COMMENT ON COLUMN payment.payment_session.surcharge_amount
    IS 'Additional surcharge charged by the payment session, in minor units';

COMMENT ON COLUMN payment.payment_session.tip_amount
    IS 'Tip charged by the payment session, in minor units';

COMMENT ON COLUMN payment.payment_session.reference_id
    IS 'Transaction reference assigned by the PSP';