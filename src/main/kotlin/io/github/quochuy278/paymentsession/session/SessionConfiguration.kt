package io.github.quochuy278.paymentsession.session

import io.github.quochuy278.paymentsession.checkout.application.CheckoutRepository
import io.github.quochuy278.paymentsession.session.application.CreatePaymentSession
import io.github.quochuy278.paymentsession.session.application.PaymentSessionRepository
import io.github.quochuy278.paymentsession.shared.utils.IdGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration(proxyBeanMethods = false)
class SessionConfiguration {

    @Bean
    fun createPaymentSession(
        checkoutRepository: CheckoutRepository,
        paymentSessionRepository: PaymentSessionRepository,
        idGenerator: IdGenerator,
        clock: Clock,
    ): CreatePaymentSession = CreatePaymentSession(
        checkoutRepository = checkoutRepository,
        paymentSessionRepository = paymentSessionRepository,
        idGenerator = idGenerator,
        clock = clock,
    )
}
