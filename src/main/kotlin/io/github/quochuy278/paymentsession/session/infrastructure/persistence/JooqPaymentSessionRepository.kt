package io.github.quochuy278.paymentsession.session.infrastructure.persistence

import io.github.quochuy278.paymentsession.persistence.jooq.generated.tables.references.PAYMENT_SESSION
import io.github.quochuy278.paymentsession.session.application.PaymentSessionRepository
import io.github.quochuy278.paymentsession.session.domain.PaymentSession
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.ZoneOffset

@Repository
class JooqPaymentSessionRepository(
    private val dsl: DSLContext,
) : PaymentSessionRepository {

    override fun insert(paymentSession: PaymentSession) {
        dsl.insertInto(PAYMENT_SESSION)
            .set(PAYMENT_SESSION.ID, paymentSession.id)
            .set(PAYMENT_SESSION.CHECKOUT_ID, paymentSession.checkoutId)
            .set(PAYMENT_SESSION.STATUS, paymentSession.status.name)
            .set(PAYMENT_SESSION.PSP, paymentSession.psp)
            .set(PAYMENT_SESSION.AMOUNT, paymentSession.amount.amount)
            .set(
                PAYMENT_SESSION.CURRENCY,
                paymentSession.amount.currency.currencyCode,
            )
            .set(
                PAYMENT_SESSION.IDEMPOTENCY_KEY,
                paymentSession.idempotencyKey,
            )
            .set(PAYMENT_SESSION.REFERENCE_ID, paymentSession.referenceId)
            .set(
                PAYMENT_SESSION.CREATED_AT,
                paymentSession.createdAt.atOffset(ZoneOffset.UTC),
            )
            .set(
                PAYMENT_SESSION.UPDATED_AT,
                paymentSession.updatedAt.atOffset(ZoneOffset.UTC),
            )
            .execute()
    }
}
