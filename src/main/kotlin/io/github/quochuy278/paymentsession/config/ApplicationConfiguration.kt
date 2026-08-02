package io.github.quochuy278.paymentsession.config

import io.github.quochuy278.paymentsession.api.ApiController
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.HandlerTypePredicate
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class ApplicationConfiguration(
    private val corsProperties: CorsProperties,
) : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("/api/**")
            .allowedOrigins(*corsProperties.allowedOrigins.toTypedArray())
            .allowedMethods(
                "GET",
                "HEAD",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
            )
            .allowedHeaders("Accept", "Content-Type")
            .allowCredentials(corsProperties.allowCredentials)
            .maxAge(3600)
    }

    override fun configureApiVersioning(
        configurer: ApiVersionConfigurer,
    ) {
        configurer.usePathSegment(1)
    }

    override fun configurePathMatch(
        configurer: PathMatchConfigurer,
    ) {
        configurer.addPathPrefix(
            "/api/{version}",
            HandlerTypePredicate.forAnnotation(ApiController::class.java),
        )
    }
}