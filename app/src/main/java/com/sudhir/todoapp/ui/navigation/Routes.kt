package com.sudhir.todoapp.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object TaskListRoute : NavKey

@Serializable
data class AddEditTodoRoute(val todoId: Int? = null) : NavKey
