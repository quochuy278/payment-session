package io.github.quochuy278.paymentsession.config

import jakarta.validation.constraints.NotEmpty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "app.cors")
data class CorsProperties(
    @field:NotEmpty
    val allowedOrigins: List<String>,

    val allowCredentials: Boolean,
) {
    init {
        require(!allowCredentials || "*" !in allowedOrigins) {
            "Wildcard CORS origin cannot be combined with credentials"
        }
    }
}