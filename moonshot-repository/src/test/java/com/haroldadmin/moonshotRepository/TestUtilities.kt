package com.haroldadmin.moonshotRepository

import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import java.io.IOException

internal inline fun <T : Any> successfulResponse(crossinline dataProvider: () -> T): NetworkResponse.Success<T> =
    NetworkResponse.Success(dataProvider())

internal inline fun <T : Any> serverErrorResponse(
    crossinline codeProvider: () -> Int = { 404 },
    crossinline errorProvider: () -> T
): NetworkResponse.ServerError<T> = NetworkResponse.ServerError(errorProvider(), codeProvider())

internal fun ioErrorResponse(): NetworkResponse.NetworkError = NetworkResponse.NetworkError(IOException())

internal fun <T> T.toDeferred(): Deferred<T> = CompletableDeferred(this)

@Suppress("UNCHECKED_CAST")
internal inline fun <reified R> Flow<*>.filterIsNotInstance(): Flow<*> = filter { it !is R }
