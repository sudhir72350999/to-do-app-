package com.sudhir.todoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudhir.todoapp.data.model.Category
import com.sudhir.todoapp.data.model.Priority
import com.sudhir.todoapp.data.model.TodoItem
import com.sudhir.todoapp.data.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoListViewModel(private val repository: TodoRepository) : ViewModel() {

    private val _filterCategory = MutableStateFlow<Category?>(null)
    val filterCategory: StateFlow<Category?> = _filterCategory

    private val _filterPriority = MutableStateFlow<Priority?>(null)
    val filterPriority: StateFlow<Priority?> = _filterPriority

    val todoItems: StateFlow<List<TodoItem>> = combine(
        repository.getAllTodoItems(),
        _filterCategory,
        _filterPriority
    ) { items, category, priority ->
        items.filter { item ->
            (category == null || item.category == category) &&
            (priority == null || item.priority == priority)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setCategoryFilter(category: Category?) {
        _filterCategory.value = category
    }

    fun setPriorityFilter(priority: Priority?) {
        _filterPriority.value = priority
    }

    fun toggleTodoCompletion(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.updateTodoItem(todoItem.copy(isCompleted = !todoItem.isCompleted))
        }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.deleteTodoItem(todoItem)
        }
    }
}
