package com.example.myapplication

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**aqui es inyectado defaultErrorMapper*/
class GlobalErrorManager(private val defaultErrorMapper: GlobalErrorMapper) {

    private val _event = MutableSharedFlow<GlobalErrorHandlerModel>()
    val event: SharedFlow<GlobalErrorHandlerModel> = _event.asSharedFlow()

    private val _errorClosedEvent = MutableSharedFlow<Unit>()
    val errorClosedEvent: SharedFlow<GlobalErrorHandlerModel> = _event.asSharedFlow()

    suspend fun emitError(
        throwable: Throwable,
        onGenericError: (suspend () -> Unit)? = null,
        onUnauthorized: (suspend () -> Unit)? = null,
        onNetworkUnavailable: (suspend () -> Unit)? = null,
        dismissAction: (() -> Unit)? = null,
    ) {
        _errorClosedEvent.emit(Unit)
        emitErrorType(
            defaultErrorMapper.map(throwable),
            onGenericError = onGenericError,
            onUnauthorized = onUnauthorized,
            onNetworkUnavailable = onNetworkUnavailable,
            dismissAction = dismissAction,
        )
    }

    suspend fun emitErrorType(
        globalErrorType: GlobalErrorType,
        onGenericError: (suspend () -> Unit)? = null,
        onUnauthorized: (suspend () -> Unit)? = null,
        onNetworkUnavailable: (suspend () -> Unit)? = null,
        dismissAction: (() -> Unit)? = null,
    ) {
        _errorClosedEvent.emit(Unit)
        _event.emit(
            GlobalErrorHandlerModel(
                globalErrorType = globalErrorType,
                onGenericError = onGenericError,
                onUnauthorized = onUnauthorized,
                onNetworkUnavailable = onNetworkUnavailable,
                dismissAction = dismissAction,
            )
        )
    }
}