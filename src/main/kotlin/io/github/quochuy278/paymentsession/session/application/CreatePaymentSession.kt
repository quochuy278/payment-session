package io.github.quochuy278.paymentsession.session.application

import io.github.quochuy278.paymentsession.checkout.application.CheckoutRepository
import io.github.quochuy278.paymentsession.session.application.exception.CheckoutNotFoundException
import io.github.quochuy278.paymentsession.session.application.exception.PaymentSessionAmountExceedsCheckoutException
import io.github.quochuy278.paymentsession.session.application.exception.PaymentSessionCurrencyMismatchException
import io.github.quochuy278.paymentsession.session.domain.PaymentSession
import io.github.quochuy278.paymentsession.session.domain.PaymentSessionStatus
import io.github.quochuy278.paymentsession.shared.domain.Money
import io.github.quochuy278.paymentsession.shared.utils.IdGenerator
import org.springframework.transaction.annotation.Transactional
import java.time.Clock

class CreatePaymentSession(
    private val checkoutRepository: CheckoutRepository,
    private val paymentSessionRepository: PaymentSessionRepository,
    private val idGenerator: IdGenerator,
    private val clock: Clock,
) {
    @Transactional
    fun execute(
        checkoutId: String,
        psp: String,
        amount: Money,
        idempotencyKey: String,
    ): PaymentSession {
        val checkout = checkoutRepository.findById(checkoutId)
            ?: throw CheckoutNotFoundException(checkoutId)

        if (amount.currency != checkout.amount.currency) {
            throw PaymentSessionCurrencyMismatchException(
                checkoutCurrency = checkout.amount.currency.currencyCode,
                paymentCurrency = amount.currency.currencyCode,
            )
        }

        if (amount.amount > checkout.amount.amount) {
            throw PaymentSessionAmountExceedsCheckoutException(
                checkoutAmount = checkout.amount.amount,
                paymentAmount = amount.amount,
            )
        }

        val now = clock.instant()

        val paymentSession = PaymentSession(
            id = idGenerator.generate(),
            checkoutId = checkout.id,
            status = PaymentSessionStatus.RESERVED,
            psp = psp,
            amount = amount,
            idempotencyKey = idempotencyKey,
            referenceId = null,
            createdAt = now,
            updatedAt = now,
        )

        paymentSessionRepository.insert(paymentSession)

        return paymentSession
    }
}
