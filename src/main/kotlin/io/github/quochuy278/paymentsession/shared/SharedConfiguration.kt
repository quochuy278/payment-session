package io.github.quochuy278.paymentsession.shared

import io.github.quochuy278.paymentsession.shared.utils.IdGenerator
import io.github.quochuy278.paymentsession.shared.utils.generateUuid
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration(proxyBeanMethods = false)
class SharedConfiguration {

    @Bean
    fun clock(): Clock = Clock.systemUTC()

    @Bean
    fun idGenerator(): IdGenerator = IdGenerator(::generateUuid)
}
