package com.carles.todo.mvvm.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.carles.todo.mvvm.R

class MainActivity : AppCompatActivity() {

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
