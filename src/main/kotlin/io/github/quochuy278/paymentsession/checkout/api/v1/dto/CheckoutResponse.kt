package io.github.quochuy278.paymentsession.checkout.api.v1.dto

import io.github.quochuy278.paymentsession.checkout.domain.Checkout
import java.time.Instant

data class CheckoutResponse(
    val id: String,
    val amount: Long,
    val currency: String,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun from(checkout: Checkout) = CheckoutResponse(
            id = checkout.id,
            amount = checkout.amount.amount,
            currency = checkout.amount.currency.currencyCode,
            createdAt = checkout.createdAt,
            updatedAt = checkout.updatedAt,
        )
    }
}
