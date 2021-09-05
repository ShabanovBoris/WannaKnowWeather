package com.bosha.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

sealed interface Result<T>

class SuccessResult<T>(
    val data: T
) : Result<T>

class ErrorResult<T>(
    val exception: Exception
) : Result<T>

fun <T> Result<T>?.takeSuccess(): T? {
    return if (this is SuccessResult) this.data
    else null
}

fun <T> Result<T>.takeResult(
    onSuccess: ((T) -> Unit),
    onError: ((Exception) -> Unit) = {}
) {
    when (this) {
        is SuccessResult -> onSuccess.invoke(data)
        is ErrorResult -> onError.invoke(exception)
    }
}

suspend fun <T> Flow<Result<T>>.collectResult(
    onSuccess: ((T) -> Unit),
    onError: ((Exception) -> Unit) = {}
) = collect {
    it.takeResult(
        onSuccess = onSuccess,
        onError = onError
    )
}


