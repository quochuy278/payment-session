package io.github.quochuy278.paymentsession.checkout.application

import io.github.quochuy278.paymentsession.checkout.domain.Checkout
import io.github.quochuy278.paymentsession.shared.domain.Money
import io.github.quochuy278.paymentsession.shared.utils.IdGenerator
import java.time.Clock

class CreateCheckout(
    private val checkoutRepository: CheckoutRepository,
    private val idGenerator: IdGenerator,
    private val clock: Clock,
) {
    fun execute(amount: Money): Checkout {
        val now = clock.instant()

        val checkout = Checkout(
            id = idGenerator.generate(),
            amount = amount,
            createdAt = now,
            updatedAt = now,
        )

        checkoutRepository.insert(checkout)

        return checkout
    }
}
