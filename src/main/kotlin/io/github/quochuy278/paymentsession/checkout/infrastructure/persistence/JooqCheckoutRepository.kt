package io.github.quochuy278.paymentsession.checkout.infrastructure.persistence

import io.github.quochuy278.paymentsession.checkout.application.CheckoutRepository
import io.github.quochuy278.paymentsession.checkout.domain.Checkout
import io.github.quochuy278.paymentsession.persistence.jooq.generated.tables.references.CHECKOUT
import io.github.quochuy278.paymentsession.shared.domain.Money
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.ZoneOffset
import java.util.Currency

@Repository
class JooqCheckoutRepository(
    private val dsl: DSLContext,
) : CheckoutRepository {

    override fun insert(checkout: Checkout) {
        dsl.insertInto(CHECKOUT)
            .set(CHECKOUT.ID, checkout.id)
            .set(CHECKOUT.AMOUNT, checkout.amount.amount)
            .set(CHECKOUT.CURRENCY, checkout.amount.currency.currencyCode)
            .set(CHECKOUT.CREATED_AT, checkout.createdAt.atOffset(ZoneOffset.UTC))
            .set(CHECKOUT.UPDATED_AT, checkout.updatedAt.atOffset(ZoneOffset.UTC))
            .execute()
    }

    override fun findById(id: String): Checkout? {
        val record = dsl.selectFrom(CHECKOUT)
            .where(CHECKOUT.ID.eq(id))
            .fetchOne()
            ?: return null

        return Checkout(
            id = checkNotNull(record.id) {
                "Checkout id must not be null"
            },
            amount = Money(
                amount = checkNotNull(record.amount) {
                    "Checkout amount must not be null"
                },
                currency = Currency.getInstance(
                    checkNotNull(record.currency) {
                        "Checkout currency must not be null"
                    },
                ),
            ),
            createdAt = checkNotNull(record.createdAt) {
                "Checkout createdAt must not be null"
            }.toInstant(),
            updatedAt = checkNotNull(record.updatedAt) {
                "Checkout updatedAt must not be null"
            }.toInstant(),
        )
    }
}
