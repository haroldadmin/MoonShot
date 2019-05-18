package com.haroldadmin.moonshot.core

/**
 * An extension property which can be used to make `when` expressions
 * exhaustive. Using `safe` makes it necessary to deal with all possible
 * scenarios when using the `when` statement.
 */
val Any.safe get() = Unit