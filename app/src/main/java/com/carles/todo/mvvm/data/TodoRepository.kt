package com.carles.todo.mvvm.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.carles.todo.mvvm.AppExecutors
import com.carles.todo.mvvm.Resource
import com.carles.todo.mvvm.model.Todo

class TodoRepository(private val dao: TodoDao, private val executors: AppExecutors) {

    fun getTodos(): LiveData<Resource<List<Todo>>> {
        val result = MediatorLiveData<Resource<List<Todo>>>()
        result.value = Resource.Loading()

        val dbSource = dao.getTodos()
        result.addSource(dbSource) { todos ->
            result.removeSource(dbSource)
            result.value = Resource.Success(todos)
        }

        return result
    }

    fun insertTodo(todo: Todo) {
        executors.diskIO.execute { dao.insertTodo(todo) }
    }

    fun updateTodo(todo: Todo) {
        executors.diskIO.execute { dao.updateTodo(todo) }
    }

    fun deleteTodo(todo: Todo) {
        executors.diskIO.execute { dao.deleteTodo(todo) }
    }
}