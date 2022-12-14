package com.example.cryptonomicon

sealed class Resource<T>(val data: T? = null, val text: String? = null) {
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(text: String?, data: T? = null): Resource<T>(data, text)
}
