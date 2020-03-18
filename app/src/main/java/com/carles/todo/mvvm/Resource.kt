package com.carles.todo.mvvm

import androidx.lifecycle.LiveData

typealias ResourceLiveData<T> = LiveData<Resource<T>>

sealed class Resource<out T> {
    class Loading<out T> : Resource<T>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure<out T>(val throwable: Throwable) : Resource<T>()
}