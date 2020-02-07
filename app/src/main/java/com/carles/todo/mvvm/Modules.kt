package com.carles.todo.mvvm

import androidx.room.Room
import com.carles.todo.mvvm.model.Todo
import com.carles.todo.mvvm.model.TodoDatabase
import com.carles.todo.mvvm.model.TodoRepository
import com.carles.todo.mvvm.viewmodel.TodoListViewModel
import com.carles.todo.mvvm.viewmodel.TodoViewModel
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.Executors

val appModule = module {

    single {
        Room.databaseBuilder(androidContext(), TodoDatabase::class.java, "todo_database").build()
    }
    single { (get() as TodoDatabase).dao() }
    single { AppExecutors(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3), MainThreadExecutor()) }
    single { TodoRepository(get(), get()) }
    single { LocationServices.getFusedLocationProviderClient(androidApplication()) }

    viewModel { TodoListViewModel(androidApplication(), get(), get()) }
    viewModel { (todo: Todo) -> TodoViewModel(androidApplication(), todo, get()) }
}