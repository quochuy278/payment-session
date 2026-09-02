package io.github.quochuy278.paymentsession.shared.id

fun interface IdGenerator {
    fun generate(): String
}
