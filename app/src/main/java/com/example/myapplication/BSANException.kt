package com.example.myapplication

abstract class BSANException(message: String, val url: String? = null): Exception(message)
