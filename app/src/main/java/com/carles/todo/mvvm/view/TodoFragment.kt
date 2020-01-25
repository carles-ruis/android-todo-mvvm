package com.carles.todo.mvvm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.carles.todo.mvvm.R
import com.carles.todo.mvvm.viewmodel.TodoViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_todo.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.ext.getOrCreateScope

abstract class TodoFragment : Fragment() {

    abstract fun getTitleTextRes(): Int
    abstract fun getConfirmButtonTextRes(): Int
    abstract fun onConfirmButtonClicked()

    protected val viewModel by viewModel<TodoViewModel> { parametersOf(arguments!!.getParcelable(EXTRA_TODO)!!) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_todo, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
    }

    private fun initViews() {
        todo_toolbar.setTitle(getTitleTextRes())
        (activity as AppCompatActivity).setSupportActionBar(todo_toolbar)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        todo_toolbar.setNavigationOnClickListener { onBackClicked() }
        todo_name_edittext.doAfterTextChanged { name -> viewModel.changeName(name.toString()) }
        todo_duedate_edittext.setOnClickListener { viewModel.openCalendar() }
        todo_confirm_button.setOnClickListener { onConfirmButtonClicked() }
        todo_confirm_button.setText(getConfirmButtonTextRes())
    }

    private fun observeViewModel() {
        viewModel.enableConfirmButton.observe(viewLifecycleOwner, Observer {
            todo_confirm_button.isEnabled = it
        })
        viewModel.todo.observe(viewLifecycleOwner, Observer { todo ->
            todo_name_edittext.setText(todo.name)
            todo_duedate_edittext.setText(todo.date.toFormattedDateString())
            todo_location_edittext.setText(getString(R.string.main_todo_location_formatted, todo.latitude, todo.longitude))
        })
        viewModel.openCalendarEvent.observe(viewLifecycleOwner, Observer { date ->
            showCalendarWithDate(date)
        })
        viewModel.navigateBackEvent.observe(viewLifecycleOwner, Observer {
            navigateBack()
        })
    }

    private fun onBackClicked() {
        MaterialAlertDialogBuilder(context).setCancelable(true)
            .setMessage(R.string.todo_back_message)
            .setPositiveButton(R.string.todo_exit_button) { _, _ -> navigateBack() }
            .setNegativeButton(R.string.todo_stay_button) { _, _ -> }
            .show()
    }

    private fun showCalendarWithDate(date: Long) {
        val picker = MaterialDatePicker.Builder.datePicker().setSelection(date).setTitleText(R.string.todo_calendar_title).build()
        picker.addOnPositiveButtonClickListener { selectedDate ->
            viewModel.changeDate(selectedDate)
        }
        picker.show(childFragmentManager, MaterialDatePicker<Long>::javaClass.name)
    }

    private fun navigateBack() {
        Navigation.findNavController(view!!).navigateUp()
    }

}