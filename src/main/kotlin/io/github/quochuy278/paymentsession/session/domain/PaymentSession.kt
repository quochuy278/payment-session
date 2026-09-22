package io.github.quochuy278.paymentsession.session.domain

import io.github.quochuy278.paymentsession.shared.domain.Money
import java.time.Instant

class PaymentSession(
    val id: String,
    val checkoutId: String,
    val status: PaymentSessionStatus,
    val psp: String,
    val amount: Money,
    val idempotencyKey: String,
    val referenceId: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    init {
        require(id.isNotBlank()) {
            "Payment session id must not be blank"
        }

        require(checkoutId.isNotBlank()) {
            "Payment session checkoutId must not be blank"
        }

        require(psp.isNotBlank()) {
            "Payment session psp must not be blank"
        }

        require(amount.amount > 0) {
            "Payment session amount must be positive"
        }

        require(idempotencyKey.isNotBlank()) {
            "Payment session idempotencyKey must not be blank"
        }

        require(referenceId == null || referenceId.isNotBlank()) {
            "Payment session referenceId must be null or non-blank"
        }

        require(!updatedAt.isBefore(createdAt)) {
            "Payment session updatedAt must not be before createdAt"
        }
    }
}
