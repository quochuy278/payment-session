package io.github.quochuy278.paymentsession.checkout.infrastructure.persistence

import io.github.quochuy278.paymentsession.checkout.application.CheckoutRepository
import io.github.quochuy278.paymentsession.checkout.domain.Checkout
import io.github.quochuy278.paymentsession.persistence.jooq.generated.tables.references.CHECKOUT
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.ZoneOffset

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
}
