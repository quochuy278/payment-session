package io.github.quochuy278.paymentsession.checkout.api.v1

import io.github.quochuy278.paymentsession.api.ApiController
import org.springframework.web.bind.annotation.RequestMapping

@ApiController
@RequestMapping(
    path = ["/checkouts"],
    version = "1",
)
class CheckoutController {
}
