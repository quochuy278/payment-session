package io.github.quochuy278.paymentsession.session.application.exception

class PaymentSessionCurrencyMismatchException(
    checkoutCurrency: String,
    paymentCurrency: String,
) : RuntimeException(
    "Payment currency $paymentCurrency does not match checkout currency $checkoutCurrency",
)