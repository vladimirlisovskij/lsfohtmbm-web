package tech.lsfohtmbm.utils.coroutines

import kotlin.coroutines.cancellation.CancellationException

@Suppress("TooGenericExceptionCaught")
inline fun <T> cancellableRunCatching(block: () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (th: Throwable) {
        Result.failure(th)
    }
}
