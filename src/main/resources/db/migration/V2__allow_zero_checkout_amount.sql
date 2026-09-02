ALTER TABLE payment.checkout
    DROP CONSTRAINT checkout_amount_positive,
    ADD CONSTRAINT checkout_amount_non_negative
        CHECK (amount >= 0);