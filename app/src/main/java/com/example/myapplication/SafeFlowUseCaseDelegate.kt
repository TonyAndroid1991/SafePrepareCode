package com.example.myapplication

import kotlinx.coroutines.flow.catch

interface SafeFlowUseCaseDelegate {
    val globalErrorManager: GlobalErrorManager

    fun <T, R> FlowUseCase<T, R>.safePrepare(
        input: T,
        onGenericError: (suspend () -> Unit)? = null,
        onUnauthorized: (suspend () -> Unit)? = null,
        onNetworkUnavailable: (suspend () -> Unit)? = null,
        dismissAction: (() -> Unit)? = null
    ) =
        prepare(input).catch {
            RetailLogger.e(this@safePrepare.javaClass.simpleName, it)
            globalErrorManager.emitError(it, onGenericError, onUnauthorized, onNetworkUnavailable, dismissAction)
        }

    /** Method that handle global errors by default without parameter for use case. Use lambdas for special cases */
    fun <R> FlowUseCase<Unit, R>.safePrepare(
        onGenericError: (suspend () -> Unit)? = null,
        onUnauthorized: (suspend () -> Unit)? = null,
        onNetworkUnavailable: (suspend () -> Unit)? = null,
        dismissAction: (() -> Unit)? = null
    ) =
        prepare(Unit).catch {
            RetailLogger.e(this@safePrepare.javaClass.simpleName, it)
            globalErrorManager.emitError(it, onGenericError, onUnauthorized, onNetworkUnavailable, dismissAction)
        }

    /**Hacer esta clase Singleton*/
    //(Singleton::class)
    /**inyectar aqui globalErrorManager*/
    class Default  constructor(override val globalErrorManager: GlobalErrorManager) : SafeFlowUseCaseDelegate
}