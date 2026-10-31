package com.sudhir.todoapp

import android.app.Application
import androidx.room.Room
import com.sudhir.todoapp.data.local.TodoDatabase
import com.sudhir.todoapp.data.repository.TodoRepository

class TodoApplication : Application() {
    private val database by lazy {
        Room.databaseBuilder(
            this,
            TodoDatabase::class.java,
            "todo_database"
        ).build()
    }

    val repository by lazy {
        TodoRepository(database.todoDao())
    }
}
