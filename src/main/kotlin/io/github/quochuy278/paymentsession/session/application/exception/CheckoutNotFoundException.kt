package io.github.quochuy278.paymentsession.session.application.exception

class CheckoutNotFoundException(
    checkoutId: String,
) : RuntimeException("Checkout $checkoutId was not found")