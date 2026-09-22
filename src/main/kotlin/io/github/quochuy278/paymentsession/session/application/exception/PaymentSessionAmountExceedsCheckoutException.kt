package io.github.quochuy278.paymentsession.session.application.exception

class PaymentSessionAmountExceedsCheckoutException(
    checkoutAmount: Long,
    paymentAmount: Long,
) : RuntimeException(
    "Payment amount $paymentAmount exceeds checkout amount $checkoutAmount",
)