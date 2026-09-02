package io.github.quochuy278.paymentsession.checkout.domain

import io.github.quochuy278.paymentsession.shared.domain.Money
import java.time.Instant

class Checkout(
    val id: String,
    val amount: Money,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    init {
        require(id.isNotBlank()) {
            "Checkout id must not be blank"
        }

        require(amount.amount >= 0) {
            "Checkout amount must not be negative"
        }

        require(!updatedAt.isBefore(createdAt)) {
            "Checkout updatedAt must not be before createdAt"
        }
    }
}
