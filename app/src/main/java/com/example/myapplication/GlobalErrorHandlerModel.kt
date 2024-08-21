package com.example.myapplication

data class GlobalErrorHandlerModel(
    val globalErrorType: GlobalErrorType,
    val onGenericError: (suspend () -> Unit)? = null,
    val onUnauthorized: (suspend () -> Unit)? = null,
    val onNetworkUnavailable: (suspend () -> Unit)? = null,
    val dismissAction: (() -> Unit)? = null
)