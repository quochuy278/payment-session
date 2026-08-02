package io.github.quochuy278.paymentsession

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class PaymentSessionApplication

fun main(args: Array<String>) {
	runApplication<PaymentSessionApplication>(*args)
}
