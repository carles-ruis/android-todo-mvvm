package com.carles.todo.mvvm.view

import com.carles.todo.mvvm.R

class EditTodoFragment : TodoFragment() {

    override fun getTitleTextRes() = R.string.todo_edit_title
    override fun getConfirmButtonTextRes() = R.string.todo_edit_button
    override fun onConfirmButtonClicked() {
        viewModel.confirmEdit()
    }
}