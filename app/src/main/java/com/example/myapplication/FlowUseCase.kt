package com.example.myapplication

import androidx.annotation.CheckResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

abstract class FlowUseCase<T, R>(protected open val dispatcherProvider: DispatcherProvider) {

    protected open fun dispatcher(): CoroutineContext = dispatcherProvider.io()

    @Deprecated(
        replaceWith = ReplaceWith("com.santander.one.domain.error.SafeFlowUseCaseDelegate"),
        message = "use safePrepare instead so you handle global errors"
    )
    @CheckResult
    fun prepare(input: T) = prepareFlow(input).flowOn(dispatcher())

    /**
     * GenericFlowUseCase Return a [Flow] that will be executed in the specified [CoroutineContext] ([Dispatchers.IO] by default).
     *
     * There's no need to call to [flowOn] in subclasses.
     */
    protected abstract fun prepareFlow(input: T): Flow<R>
}

@NoCoverage
@Deprecated("It will be removed when legacy get migrated", replaceWith = ReplaceWith("FlowUseCase"))
abstract class GenericFlowUseCase<T>(protected open val dispatcherProvider: DispatcherProvider) {

    protected open fun dispatcher(): CoroutineContext = dispatcherProvider.io()

    @CheckResult
    fun <R> prepare(input: T, klass: Class<R>) = prepareFlow(input, klass).flowOn(dispatcher())

    @CheckResult
    inline fun <reified R> prepare(input: T) = prepare(input, R::class.java)

    /**
     * Return a [Flow] that will be executed in the specified [CoroutineContext] ([Dispatchers.IO] by default).
     *
     * There's no need to call to [flowOn] in subclasses.
     */
    abstract fun <R> prepareFlow(input: T, klass: Class<R>): Flow<R?>
}
