package com.haroldadmin.moonshot.core

/**
 * A sealed class to represent UI states associated with a resource.
 */
sealed class Resource<out T> {

    abstract override fun hashCode(): Int
    abstract override fun equals(other: Any?): Boolean

    /**
     * A property to determine whether this Resource has reached a terminal status or not.
     * A Resource is in a terminal status if it is [Success] or [Error]
     */
    abstract val isComplete: Boolean

    /**
     * A data class to represent the scenario where the resource is available without any errors
     *
     * This is a terminal state, and the Resource object is considered to be completed when it is in this state
     */
    data class Success <out T>(
        val data: T,
        val isCached: Boolean = false
    ) : Resource<T>() {

        override val isComplete: Boolean = !isCached

        operator fun invoke(): T {
            return data
        }
    }

    /**
     * A data class to represent the scenario where a resource may or may not be available due to an error
     *
     * This is a terminal state, and the Resource object is considered to be completed when it is in this state
     */
    data class Error <out T, out E> (
        val data: T?,
        val error: E?
    ) : Resource<T>() {

        override val isComplete: Boolean = true
    }

    /**
     * A class to represent the loading state of an object
     *
     * This is a non-terminal state.
     */
    object Loading : Resource<Nothing>() {
        override fun hashCode(): Int {
            return 2
        }

        override fun equals(other: Any?): Boolean {
            return other is Loading
        }

        override val isComplete: Boolean = false
    }

    object Uninitialized : Resource<Nothing>() {
        override fun hashCode(): Int {
            return 1
        }

        override fun equals(other: Any?): Boolean {
            return other is Uninitialized
        }

        override val isComplete: Boolean = false
    }
}

operator fun <T> Resource<T>.invoke(): T? {
    return when {
        this is Resource.Success -> this.data
        this is Resource.Error<T, *> && this.data != null -> this.data
        else -> null
    }
}