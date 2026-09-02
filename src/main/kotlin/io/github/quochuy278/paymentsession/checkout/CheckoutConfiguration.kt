package io.github.quochuy278.paymentsession.checkout

import io.github.quochuy278.paymentsession.checkout.application.CheckoutRepository
import io.github.quochuy278.paymentsession.checkout.application.CreateCheckout
import io.github.quochuy278.paymentsession.shared.utils.IdGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration(proxyBeanMethods = false)
class CheckoutConfiguration {

    @Bean
    fun createCheckout(
        checkoutRepository: CheckoutRepository,
        idGenerator: IdGenerator,
        clock: Clock,
    ): CreateCheckout = CreateCheckout(
        checkoutRepository = checkoutRepository,
        idGenerator = idGenerator,
        clock = clock,
    )
}
