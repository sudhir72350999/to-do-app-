package com.sudhir.todoapp.data.repository

import com.sudhir.todoapp.data.local.TodoDao
import com.sudhir.todoapp.data.model.TodoItem
import kotlinx.coroutines.flow.Flow

class TodoRepository(private val todoDao: TodoDao) {
    fun getAllTodoItems(): Flow<List<TodoItem>> = todoDao.getAllTodoItems()

    fun getTodoItemById(id: Int): Flow<TodoItem?> = todoDao.getTodoItemById(id)

    suspend fun insertTodoItem(todoItem: TodoItem) = todoDao.insertTodoItem(todoItem)

    suspend fun updateTodoItem(todoItem: TodoItem) = todoDao.updateTodoItem(todoItem)

    suspend fun deleteTodoItem(todoItem: TodoItem) = todoDao.deleteTodoItem(todoItem)
}
