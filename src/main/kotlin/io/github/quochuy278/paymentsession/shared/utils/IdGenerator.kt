package io.github.quochuy278.paymentsession.shared.utils

import java.util.UUID

fun interface IdGenerator {
    fun generate(): String
}

fun generateUuid(): String = UUID.randomUUID().toString()
