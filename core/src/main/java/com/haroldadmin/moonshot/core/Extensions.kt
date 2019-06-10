package com.haroldadmin.moonshot.core

/**
 * An extension property which can be used to make `when` expressions
 * exhaustive. Using `safe` makes it necessary to deal with all possible
 * scenarios when using the `when` statement.
 */
val Any.safe get() = Unit

fun <A, B, C> tripleOf(a: A, b: B, c: C) = Triple(a, b, c)

fun <A, B> pairOf(a: A, b: B) = Pair(a, b)