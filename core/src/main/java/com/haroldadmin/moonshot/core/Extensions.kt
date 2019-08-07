package com.haroldadmin.moonshot.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.reduce

/**
 * An extension property which can be used to make `when` expressions
 * exhaustive. Using `safe` makes it necessary to deal with all possible
 * scenarios when using the `when` statement.
 */
val Any.safe get() = Unit

fun <A, B, C> tripleOf(a: A, b: B, c: C) = Triple(a, b, c)

fun <A, B> pairOf(a: A, b: B) = Pair(a, b)

fun <T: Any?> T.asConsumable(): Consumable<T> {
    return Consumable(this)
}

@ExperimentalCoroutinesApi
suspend fun <T> Flow<T>.last(): T {
    return this.reduce { _, value -> value }
}