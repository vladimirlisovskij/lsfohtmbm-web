package tech.lsfohtmbm.utils.coroutines

import kotlin.coroutines.cancellation.CancellationException

fun<T> Result<T>.cancellableGetOrNull(): T? {
    val exception = exceptionOrNull()
    if (exception is CancellationException) {
        throw exception
    } else {
        return getOrNull()
    }
}

fun<T> Result<T>.cancellableGetOrDefault(default: T): T {
    val exception = exceptionOrNull()
    if (exception is CancellationException) {
        throw exception
    } else {
        return getOrDefault(default)
    }
}
