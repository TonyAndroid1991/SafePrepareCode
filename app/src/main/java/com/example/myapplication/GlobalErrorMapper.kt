package com.example.myapplication

interface GlobalErrorMapper {
    fun map(throwable: Throwable): GlobalErrorType
}
