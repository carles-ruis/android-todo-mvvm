package com.carles.todo.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carles.todo.mvvm.SingleLiveEvent
import com.carles.todo.mvvm.model.Todo
import com.carles.todo.mvvm.model.TodoRepository

class TodoViewModel(
    application: Application,
    private val _todo: Todo,
    private val repository: TodoRepository
) : AndroidViewModel(application) {

    val todo: LiveData<Todo> get() = MutableLiveData(_todo)

    private val _enableConfirmButton = MutableLiveData<Boolean>()
    val enableConfirmButton: LiveData<Boolean> get() = _enableConfirmButton

    private val _openCalendarEvent = SingleLiveEvent<Long>()
    val openCalendarEvent: LiveData<Long> get() = _openCalendarEvent

    private val _navigateBackEvent = SingleLiveEvent<Void>()
    val navigateBackEvent: LiveData<Void> get() = _navigateBackEvent

    init {
        _enableConfirmButton.value = false
    }

    fun changeName(name: String) {
        _todo.name = name
        _enableConfirmButton.value = name.length > 0
    }

    fun openCalendar() {
        _openCalendarEvent.value = _todo.date
    }

    fun changeDate(date: Long) {
        _todo.date = date
    }

    fun confirmAdd() {
        repository.insertTodo(_todo)
        _navigateBackEvent.call()
    }

    fun confirmEdit() {
        repository.updateTodo(_todo)
        _navigateBackEvent.call()
    }

}