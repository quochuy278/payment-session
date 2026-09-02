package io.github.quochuy278.paymentsession.checkout.api.v1.dto

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.PositiveOrZero

data class CreateCheckoutRequest(
    @field:PositiveOrZero
    val amount: Long,

    @field:Pattern(regexp = "[A-Z]{3}")
    val currency: String,
)
