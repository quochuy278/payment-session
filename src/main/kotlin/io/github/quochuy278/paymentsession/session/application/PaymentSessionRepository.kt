package io.github.quochuy278.paymentsession.session.application

import io.github.quochuy278.paymentsession.session.domain.PaymentSession

interface PaymentSessionRepository {
    fun insert(paymentSession: PaymentSession)
}