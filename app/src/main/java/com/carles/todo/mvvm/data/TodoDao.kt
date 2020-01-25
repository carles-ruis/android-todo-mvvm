package com.carles.todo.mvvm.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.carles.todo.mvvm.model.Todo

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo")
    // LiveData objects are returned asynchronously outside the main thread
    fun getTodos() : LiveData<List<Todo>>

    @Insert
    fun insertTodo(todo: Todo)

    @Update
    fun updateTodo(todo: Todo)

    @Delete
    fun deleteTodo(todo: Todo)

}