package com.carles.todo.mvvm.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(Todo::class), version = 1, exportSchema = false)
abstract class TodoDatabase() : RoomDatabase() {

    abstract fun dao(): TodoDao
}