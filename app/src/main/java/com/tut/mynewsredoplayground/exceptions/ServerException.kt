package com.tut.mynewsredoplayground.exceptions

import java.lang.Exception

data class ServerException(val errorMessage:String="", val throwable: Throwable?=null):Exception()
data class NoInternetException(val errorMessage:String="No Internet"):Exception()