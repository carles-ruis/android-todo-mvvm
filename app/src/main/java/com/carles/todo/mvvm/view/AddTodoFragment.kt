package com.carles.todo.mvvm.view

import androidx.fragment.app.Fragment
import com.carles.todo.mvvm.R

class AddTodoFragment : TodoFragment() {
    override fun getTitleTextRes() = R.string.todo_add_title
    override fun getConfirmButtonTextRes() = R.string.todo_add_button
    override fun onConfirmButtonClicked() {
        viewModel.confirmAdd()
    }
}