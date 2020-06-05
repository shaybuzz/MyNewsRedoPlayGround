package com.tut.mynewsredoplayground.utils

sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Loading<out T>(val partialData: T? = null) : Result<T>()
    data class Failure<out T>(val throwable: Throwable? = null, val message:String? = null) : Result<T>()

    val extractData: T?
        get() = when (this) {
            is Success -> data
            is Loading -> partialData
            is Failure -> null
        }
}