package com.example.myapplication

import kotlinx.coroutines.CancellationException

class GlobalErrorMapperImpl : GlobalErrorMapper {

    override fun map(throwable: Throwable) =
        when (throwable) {
            is UnauthorizedException -> GlobalErrorType.UNAUTHORIZED
            is NetworkException -> GlobalErrorType.NETWORK_UNAVAILABLE
            is IllegalStateException,
            is CancellationException -> GlobalErrorType.SILENT
            else -> GlobalErrorType.GENERIC_ERROR
        }
}