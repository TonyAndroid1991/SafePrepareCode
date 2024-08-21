package com.example.myapplication

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

// region Side-effects

@OptIn(ExperimentalContracts::class)
inline fun <T, E> Either<T, E>.onSuccess(action: (T) -> Unit): Either<T, E> {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }

    if (this is Either.Success) action(data)
    return this
}

@OptIn(ExperimentalContracts::class)
inline fun <T, E> Either<T, E>.onFailure(action: (E) -> Unit): Either<T, E> {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }

    if (this is Either.Failure) action(error)
    return this
}

// endregion

// region Transform

@OptIn(ExperimentalContracts::class)
inline fun <T1, E1, T2, E2> Either<T1, E1>.map(
    onSuccess: (T1) -> T2,
    onFailure: (E1) -> E2,
): Either<T2, E2> {

    contract {
        callsInPlace(onSuccess, InvocationKind.AT_MOST_ONCE)
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }

    return when (this) {
        is Either.Success -> Either.Success(onSuccess(data))
        is Either.Failure -> Either.Failure(onFailure(error))
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <T1, E1, R : Either<*, *>> Either<T1, E1>.transform(
    onSuccess: (T1) -> R,
    onFailure: (E1) -> R,
): R {

    contract {
        callsInPlace(onSuccess, InvocationKind.AT_MOST_ONCE)
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }

    return when (this) {
        is Either.Success -> onSuccess(data)
        is Either.Failure -> onFailure(error)
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <T1, E, T2> Either<T1, E>.mapSuccess(action: (T1) -> T2): Either<T2, E> {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }

    return when (this) {
        is Either.Success -> Either.Success(action(data))
        is Either.Failure -> Either.Failure(error)
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <T, E1, E2> Either<T, E1>.mapFailure(action: (E1) -> E2): Either<T, E2> {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }

    return when (this) {
        is Either.Success -> Either.Success(data)
        is Either.Failure -> Either.Failure(action(error))
    }
}

// region Concat

@OptIn(ExperimentalContracts::class)
inline fun <T1, E, T2> Either<T1, E>.then(onSuccess: (T1) -> Either<T2, E>): Either<T2, E> {
    contract { callsInPlace(onSuccess, InvocationKind.AT_MOST_ONCE) }

    return when (this) {
        is Either.Success -> onSuccess(data)
        is Either.Failure -> Either.Failure(error)
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <T1, E1, T2, E2> Either<T1, E1>.then(
    onSuccess: (T1) -> Either<T2, E2>,
    onFailure: (E1) -> Either<T2, E2>,
): Either<T2, E2> {

    contract {
        callsInPlace(onSuccess, InvocationKind.AT_MOST_ONCE)
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }

    return when (this) {
        is Either.Success -> onSuccess(data)
        is Either.Failure -> onFailure(error)
    }
}

/** Combine two [Either] if both are success or return a [Either.Failure]. */
fun <T1, T2, E> Either<T1, E>.zipWith(other: () -> Either<T2, E>) =
    when (this) {
        is Either.Failure -> eitherFailure(error)
        is Either.Success ->
            when (val second = other()) {
                is Either.Failure -> eitherFailure(second.error)
                is Either.Success -> eitherSuccess(data to second.data)
            }
    }

// endregion

// region Retrieve

fun <T, E> Either<T, E>.getOrNull() = if (this is Either.Success) data else null

fun <T, E> Either<T, E>.getOrThrow() = if (this is Either.Success) data else error("Response must be Success")

fun <T, E> Either<T, E>.getOrElse(value: T) = if (this is Either.Success) data else value

fun <T, E> Either<T, E>.getErrorOrNull() = if (this is Either.Failure) error else null

@OptIn(ExperimentalContracts::class)
inline fun <T, E> Either<T, E>.getOrElse(action: () -> T): T {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }

    return if (this is Either.Success) data else action()
}

// endregion

// region Checks

inline val Either<*, *>.isSuccess
    get() = this is Either.Success

inline val Either<*, *>.isFailure
    get() = this is Either.Failure

// endregion
