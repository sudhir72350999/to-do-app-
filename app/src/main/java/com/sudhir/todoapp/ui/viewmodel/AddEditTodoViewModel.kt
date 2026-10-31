package com.sudhir.todoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudhir.todoapp.data.model.Category
import com.sudhir.todoapp.data.model.Priority
import com.sudhir.todoapp.data.model.TodoItem
import com.sudhir.todoapp.data.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AddEditTodoViewModel(
    private val repository: TodoRepository,
    private val todoId: Int?
) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _category = MutableStateFlow(Category.OTHERS)
    val category: StateFlow<Category> = _category

    private val _priority = MutableStateFlow(Priority.MEDIUM)
    val priority: StateFlow<Priority> = _priority

    private val _dueDate = MutableStateFlow(System.currentTimeMillis())
    val dueDate: StateFlow<Long> = _dueDate

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved

    init {
        if (todoId != null) {
            viewModelScope.launch {
                repository.getTodoItemById(todoId).firstOrNull()?.let { todo ->
                    _title.value = todo.title
                    _description.value = todo.description
                    _category.value = todo.category
                    _priority.value = todo.priority
                    _dueDate.value = todo.dueDate
                }
            }
        }
    }

    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }

    fun onDescriptionChange(newDescription: String) {
        _description.value = newDescription
    }

    fun onCategoryChange(newCategory: Category) {
        _category.value = newCategory
    }

    fun onPriorityChange(newPriority: Priority) {
        _priority.value = newPriority
    }

    fun onDueDateChange(newDueDate: Long) {
        _dueDate.value = newDueDate
    }

    fun saveTodo() {
        viewModelScope.launch {
            val todoItem = TodoItem(
                id = todoId ?: 0,
                title = _title.value,
                description = _description.value,
                category = _category.value,
                priority = _priority.value,
                dueDate = _dueDate.value
            )
            if (todoId == null) {
                repository.insertTodoItem(todoItem)
            } else {
                repository.updateTodoItem(todoItem)
            }
            _isSaved.value = true
        }
    }
}
