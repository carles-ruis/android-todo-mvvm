package com.carles.todo.mvvm.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carles.todo.mvvm.LiveEvent
import com.carles.todo.mvvm.ResourceLiveData
import com.carles.todo.mvvm.model.Todo
import com.carles.todo.mvvm.model.TodoLocation
import com.carles.todo.mvvm.model.TodoRepository
import com.google.android.gms.location.FusedLocationProviderClient
import java.util.*

class TodoListViewModel(
    application: Application,
    private val repository: TodoRepository,
    private val locationClient: FusedLocationProviderClient
) : AndroidViewModel(application) {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _todoListLiveData: ResourceLiveData<List<Todo>>
    val todoListLiveData: ResourceLiveData<List<Todo>> get() = _todoListLiveData

    private val _navigateToAddTodo = LiveEvent<Todo>()
    val navigateToAddTodo: LiveData<Todo> get() = _navigateToAddTodo

    init {
        _todoListLiveData = repository.getTodos()
    }

    fun addTodo(locationAllowed:Boolean) {
        _loading.value = true
        val date = Date().time

        if (!locationAllowed) {
            navigateToAddTodo(date, getDefaultLocation())
            return
        }

        locationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                navigateToAddTodo(date, location)
            } else {
                navigateToAddTodo(date, getDefaultLocation())
            }
        }.addOnFailureListener {
            navigateToAddTodo(date, getDefaultLocation())
        }
    }

    private fun navigateToAddTodo(date: Long, location: Location) {
        _loading.value = false
        _navigateToAddTodo.value = Todo(
            name = "",
            date = date,
            location = TodoLocation(location.latitude, location.longitude)
        )
    }

    private fun getDefaultLocation() = Location("dummy_provider")

    fun deleteTodo(todo: Todo) {
        repository.deleteTodo(todo)
    }

}