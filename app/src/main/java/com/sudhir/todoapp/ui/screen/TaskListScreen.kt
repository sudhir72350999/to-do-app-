package com.sudhir.todoapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.sudhir.todoapp.data.model.Category
import com.sudhir.todoapp.data.model.Priority
import com.sudhir.todoapp.data.model.TodoItem
import com.sudhir.todoapp.ui.viewmodel.TodoListViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TodoListViewModel,
    onAddTodo: () -> Unit,
    onEditTodo: (TodoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val todoItems by viewModel.todoItems.collectAsState()
    val filterCategory by viewModel.filterCategory.collectAsState()
    val filterPriority by viewModel.filterPriority.collectAsState()

    var showFilterSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("My Tasks") },
                actions = {
                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTodo) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (todoItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tasks found. Add one!")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(todoItems, key = { it.id }) { item ->
                        TodoItemCard(
                            item = item,
                            onToggleCompletion = { viewModel.toggleTodoCompletion(item) },
                            onDelete = { viewModel.deleteTodoItem(item) },
                            onClick = { onEditTodo(item) }
                        )
                    }
                }
            }
        }

        if (showFilterSheet) {
            FilterBottomSheet(
                currentCategory = filterCategory,
                currentPriority = filterPriority,
                onCategorySelected = { viewModel.setCategoryFilter(it) },
                onPrioritySelected = { viewModel.setPriorityFilter(it) },
                onDismiss = { showFilterSheet = false }
            )
        }
    }
}

@Composable
fun TodoItemCard(
    item: TodoItem,
    onToggleCompletion: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    val dueDateStr = dateFormat.format(Date(item.dueDate))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (item.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = { onToggleCompletion() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (item.isCompleted) TextDecoration.LineThrough else null,
                    color = if (item.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CategoryChip(category = item.category)
                    Spacer(modifier = Modifier.width(8.dp))
                    PriorityBadge(priority = item.priority)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = dueDateStr,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryChip(category: Category) {
    val containerColor = when (category) {
        Category.WORK -> MaterialTheme.colorScheme.primaryContainer
        Category.PERSONAL -> MaterialTheme.colorScheme.secondaryContainer
        Category.SHOPPING -> MaterialTheme.colorScheme.tertiaryContainer
        Category.OTHERS -> MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = when (category) {
        Category.WORK -> MaterialTheme.colorScheme.onPrimaryContainer
        Category.PERSONAL -> MaterialTheme.colorScheme.onSecondaryContainer
        Category.SHOPPING -> MaterialTheme.colorScheme.onTertiaryContainer
        Category.OTHERS -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = category.name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun PriorityBadge(priority: Priority) {
    val color = when (priority) {
        Priority.LOW -> MaterialTheme.colorScheme.primary
        Priority.MEDIUM -> Color(0xFFFBC02D) // Vibrant Yellow
        Priority.HIGH -> MaterialTheme.colorScheme.error
    }
    Surface(
        color = color.copy(alpha = 0.1f),
        contentColor = color,
        shape = MaterialTheme.shapes.extraSmall,
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Text(
            text = priority.name,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    currentCategory: Category?,
    currentPriority: Priority?,
    onCategorySelected: (Category?) -> Unit,
    onPrioritySelected: (Priority?) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Filter by Category", style = MaterialTheme.typography.titleMedium)
            Row(modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Category.values().forEach { category ->
                    FilterChip(
                        selected = currentCategory == category,
                        onClick = { onCategorySelected(if (currentCategory == category) null else category) },
                        label = { Text(category.name) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Filter by Priority", style = MaterialTheme.typography.titleMedium)
            Row(modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Priority.values().forEach { priority ->
                    FilterChip(
                        selected = currentPriority == priority,
                        onClick = { onPrioritySelected(if (currentPriority == priority) null else priority) },
                        label = { Text(priority.name) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
