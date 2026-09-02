package io.github.quochuy278.paymentsession.checkout.application

import io.github.quochuy278.paymentsession.checkout.domain.Checkout

interface CheckoutRepository {
    fun insert(checkout: Checkout)
}
