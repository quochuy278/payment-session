package io.github.quochuy278.paymentsession.shared.domain

import java.util.Currency

data class Money(
    val amount: Long,
    val currency: Currency,
)
