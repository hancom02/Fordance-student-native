package com.fordance.student.core.common

sealed interface ResultWrapper<out T> {
    data class Success<out T>(val data: T) : ResultWrapper<T>
    data class Error(val message: String, val throwable: Throwable? = null) : ResultWrapper<Nothing>
    data object Loading : ResultWrapper<Nothing>
}

inline fun <T, R> ResultWrapper<T>.map(transform: (T) -> R): ResultWrapper<R> {
    return when (this) {
        is ResultWrapper.Success -> ResultWrapper.Success(transform(data))
        is ResultWrapper.Error -> ResultWrapper.Error(message, throwable)
        is ResultWrapper.Loading -> ResultWrapper.Loading
    }
}
