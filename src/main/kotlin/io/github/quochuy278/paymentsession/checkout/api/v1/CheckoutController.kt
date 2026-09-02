package io.github.quochuy278.paymentsession.checkout.api.v1

import io.github.quochuy278.paymentsession.api.ApiController
import io.github.quochuy278.paymentsession.checkout.api.v1.dto.CheckoutResponse
import io.github.quochuy278.paymentsession.checkout.api.v1.dto.CreateCheckoutRequest
import io.github.quochuy278.paymentsession.checkout.application.CreateCheckout
import io.github.quochuy278.paymentsession.shared.domain.Money
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.Currency

@ApiController
@RequestMapping(
    path = ["/checkouts"],
    version = "1",
)
class CheckoutController(
    private val createCheckout: CreateCheckout,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody request: CreateCheckoutRequest,
    ): CheckoutResponse {
        val checkout = createCheckout.execute(
            Money(
                amount = request.amount,
                currency = Currency.getInstance(request.currency),
            ),
        )

        return CheckoutResponse.from(checkout)
    }
}
