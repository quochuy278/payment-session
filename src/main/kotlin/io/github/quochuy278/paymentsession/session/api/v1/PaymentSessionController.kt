package io.github.quochuy278.paymentsession.session.api.v1

import io.github.quochuy278.paymentsession.api.ApiController
import org.springframework.web.bind.annotation.RequestMapping

@ApiController
@RequestMapping(
    path = ["/payment-sessions"],
    version = "1",
)
class PaymentSessionController