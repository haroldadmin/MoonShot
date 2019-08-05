package com.haroldadmin.moonshot.core

data class Consumable <out T> (private val data: T?, private var isConsumed: Boolean = false) {

    @Synchronized
    fun consume(): T? {
        return if (!isConsumed) {
            isConsumed = true
            data
        } else {
            null
        }
    }

    operator fun invoke(): T? {
        return consume()
    }
}