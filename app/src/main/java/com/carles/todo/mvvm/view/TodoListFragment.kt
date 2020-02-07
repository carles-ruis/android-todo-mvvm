package com.carles.todo.mvvm.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import com.carles.todo.mvvm.R
import com.carles.todo.mvvm.Resource
import com.carles.todo.mvvm.model.Todo
import com.carles.todo.mvvm.viewmodel.TodoListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_todo_list.*
import kotlinx.android.synthetic.main.view_progress.*
import org.koin.android.viewmodel.ext.android.viewModel

class TodoListFragment : Fragment() {

    companion object {
        private const val REQUEST_PERMISSION_FOR_ADD_LOCATION = 100
    }

    private lateinit var adapter: TodoAdapter
    private val viewModel by viewModel<TodoListViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_todo_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
    }

    private fun initViews() {
        adapter = TodoAdapter(::navigateToEditTodo, ::showDeleteConfirmationDialog)
        todolist_recyclerview.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        todolist_recyclerview.adapter = adapter
        todolist_fab.setOnClickListener { checkPermissionsAndAddTodo() }
    }

    private fun checkPermissionsAndAddTodo() {
        val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        activity?.let {
            if (ContextCompat.checkSelfPermission(it, locationPermission) == PackageManager.PERMISSION_GRANTED) {
                viewModel.addTodo(true)
            } else {
                requestPermissions(arrayOf(locationPermission), REQUEST_PERMISSION_FOR_ADD_LOCATION)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_FOR_ADD_LOCATION) {
            viewModel.addTodo(grantResults[0] == PackageManager.PERMISSION_GRANTED)
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun observeViewModel() {
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            progress.visibility = if (it) VISIBLE else GONE
        })
        viewModel.todoListLiveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> showLoading()
                is Resource.Failure -> showError(result.throwable.message)
                is Resource.Success -> showItems(result.data)
            }
        })
        viewModel.navigateToAddTodo.observe(viewLifecycleOwner, Observer { todo ->
            Navigation.findNavController(view!!)
                .navigate(TodoListFragmentDirections.actionTodoListFragmentToAddTodoFragment(todo))
        })
    }

    private fun showLoading() {
        progress.visibility = VISIBLE
    }

    private fun hideLoading() {
        progress.visibility = GONE
    }

    private fun showError(message: String?) {
        hideLoading()
        message?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
    }

    private fun showItems(items: List<Todo>?) {
        hideLoading()
        items?.let { adapter.items = it.toMutableList() }
    }

    private fun navigateToEditTodo(todo: Todo) {
        Navigation.findNavController(view!!).navigate(TodoListFragmentDirections.actionTodoListFragmentToEditTodoFragment(todo))
    }

    private fun showDeleteConfirmationDialog(todo: Todo) {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.main_delete_title)
            .setMessage(R.string.main_delete_message)
            .setPositiveButton(R.string.main_delete_confirm) { _, _ -> deleteTodo(todo) }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .show()
    }

    private fun deleteTodo(todo: Todo) {
        viewModel.deleteTodo(todo)
    }

}
