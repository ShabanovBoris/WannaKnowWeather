package com.bosha.domain.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.takeWhile

sealed interface Result<T>

class SuccessResult<T>(
    val data: T
) : Result<T>

class ErrorResult<T>(
    val exception: Exception
) : Result<T>

fun <T> Result<T>.takeSuccess(): T? {
    return if (this is SuccessResult) this.data
    else null
}

fun <T> Result<T>.isSuccess(): Boolean {
    return this is SuccessResult
}

/**
 * Take only SuccessResult or do lambda on ErrorResult
 */
fun <T> Flow<Result<T>>.takeSuccess(onError: ((Exception) -> Unit) = {}): Flow<SuccessResult<T>> =
    takeWhile {
        if (it.isSuccess()) {
            return@takeWhile true
        } else {
            onError.invoke((it as ErrorResult<T>).exception)
            return@takeWhile false
        }
    }.map { SuccessResult(it.takeSuccess()!!) }


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


