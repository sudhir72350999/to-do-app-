package com.sudhir.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.sudhir.todoapp.data.repository.TodoRepository
import com.sudhir.todoapp.ui.navigation.AddEditTodoRoute
import com.sudhir.todoapp.ui.navigation.TaskListRoute
import com.sudhir.todoapp.ui.screen.AddEditTodoScreen
import com.sudhir.todoapp.ui.screen.TaskListScreen
import com.sudhir.todoapp.ui.theme.TodoAppTheme
import com.sudhir.todoapp.ui.viewmodel.AddEditTodoViewModel
import com.sudhir.todoapp.ui.viewmodel.TodoListViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = (application as TodoApplication).repository
        setContent {
            TodoAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TodoNavHost(repository = repository)
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class TodoViewModelFactory(
    private val repository: TodoRepository,
    private val todoId: Int? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TodoListViewModel::class.java) -> {
                TodoListViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddEditTodoViewModel::class.java) -> {
                AddEditTodoViewModel(repository, todoId) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun TodoNavHost(
    repository: TodoRepository,
    modifier: Modifier = Modifier
) {
    val backStack = remember { mutableStateListOf<Any>(TaskListRoute) }
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val scope = rememberCoroutineScope()

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { 
            if (navigator.canNavigateBack()) {
                scope.launch { navigator.navigateBack() }
            } else {
                if (backStack.size > 1) {
                    backStack.removeLast()
                }
            }
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        )
    ) { key ->
        when (key) {
            is TaskListRoute -> {
                NavEntry(key) {
                    val todoListViewModel: TodoListViewModel = viewModel(
                        factory = TodoViewModelFactory(repository)
                    )

                    NavigableListDetailPaneScaffold(
                        navigator = navigator,
                        listPane = {
                            AnimatedPane {
                                TaskListScreen(
                                    viewModel = todoListViewModel,
                                    onAddTodo = {
                                        backStack.add(AddEditTodoRoute())
                                    },
                                    onEditTodo = { todo ->
                                        scope.launch {
                                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, todo.id)
                                        }
                                    }
                                )
                            }
                        },
                        detailPane = {
                            AnimatedPane {
                                val selectedTodoId = navigator.currentDestination?.contentKey as? Int
                                if (selectedTodoId != null) {
                                    val addEditViewModel: AddEditTodoViewModel = viewModel(
                                        key = selectedTodoId.toString(),
                                        factory = TodoViewModelFactory(repository, selectedTodoId)
                                    )
                                    AddEditTodoScreen(
                                        viewModel = addEditViewModel,
                                        onBack = { 
                                            scope.launch { navigator.navigateBack() }
                                        }
                                    )
                                } else {
                                    Surface {
                                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                            Text("Select a task to view details")
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }
            is AddEditTodoRoute -> {
                NavEntry(key) {
                    val addEditViewModel: AddEditTodoViewModel = viewModel(
                        key = key.todoId?.toString() ?: "new",
                        factory = TodoViewModelFactory(repository, key.todoId)
                    )
                    AddEditTodoScreen(
                        viewModel = addEditViewModel,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
            }
            else -> NavEntry(Unit) { Text("Unknown Route") }
        }
    }
}
