package com.carles.todo.mvvm.data

import androidx.lifecycle.LiveData
import com.carles.todo.mvvm.AppExecutors
import com.carles.todo.mvvm.model.Todo

class TodoRepository(private val dao: TodoDao, private val executors: AppExecutors) {


    fun getTodos(): LiveData<List<Todo>> {
        return dao.getTodos()
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