package com.sudhir.todoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_items")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val category: Category,
    val priority: Priority,
    val dueDate: Long,
    val isCompleted: Boolean = false
)
