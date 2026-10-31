# Project Plan

Create a Todo List app using Kotlin Multiplatform (KMP). The app should allow users to create, read, update, and delete (CRUD) tasks. It should have a clean, modern UI following Material Design 3 guidelines. Key features include task categorization (e.g., Work, Personal), due dates, and priority levels. The app should target Android and potentially other platforms supported by KMP (like iOS), focusing on shared logic for data management.

## Project Brief

# Project Brief: Todo List App

### Features
1.  **Task Management (CRUD):** Seamlessly create, read, update, and delete tasks with a user-friendly interface.
2.  **Categorization & Priority:** Organize your life by grouping tasks into categories (e.g., Work, Personal) and assigning priority levels to focus on what's urgent.
3.  **Smart Deadlines:** Assign due dates to tasks to ensure you stay on schedule and receive timely reminders.

### High-Level Technical Stack
*   **Language:** Kotlin (leveraging Kotlin Multiplatform for shared data management logic).
*   **UI Framework:** Jetpack Compose for a modern, declarative UI following Material Design 3 guidelines.
*   **Navigation:** Jetpack Navigation 3 (State-driven) to handle complex navigation flows efficiently.
*   **Adaptive Strategy:** Compose Material Adaptive library to provide a premium experience across different screen sizes and form factors.
*   **Concurrency:** Kotlin Coroutines for fluid, non-blocking asynchronous operations.

## Implementation Steps

### Task_1_Data_Layer: Define the TodoItem entity, Category enum, and Priority enum. Set up the Room database, DAO, and a Repository to handle CRUD operations.
- **Status:** COMPLETED
- **Updates:** Implemented TodoItem entity, Category and Priority enums. Set up Room database with TodoDao and TodoDatabase. Implemented TodoRepository providing flows for task list and single tasks. Updated SDK versions to 37 and verified build.
- **Acceptance Criteria:**
  - Room database is initialized
  - TodoItem entity covers category, priority, and due date
  - Repository provides flows for task list and single tasks
  - Project builds successfully

### Task_2_Navigation_And_ViewModel: Implement the application's navigation structure using Navigation 3. Create ViewModels to manage task state, filtering, and sorting logic.
- **Status:** COMPLETED
- **Updates:** Implemented Navigation 3 with TaskListRoute and AddEditTodoRoute. Created TodoListViewModel for list management (filtering, sorting, CRUD) and AddEditTodoViewModel for task creation/editing. Set up TodoApplication for DI and updated MainActivity with NavDisplay. verified build.
- **Acceptance Criteria:**
  - Navigation 3 is set up with routes for List and Add/Edit screens
  - ViewModels handle business logic for CRUD and filtering
  - App launches to an empty task list screen

### Task_3_UI_Implementation: Build the Task List screen and the Add/Edit Task screen using Jetpack Compose and Material Design 3. Ensure the UI is adaptive for different screen sizes.
- **Status:** COMPLETED
- **Updates:** Built Task List screen with Category/Priority chips and filtering via BottomSheet. Implemented Add/Edit screen with full Material 3 DatePicker and selection chips. Integrated NavigableListDetailPaneScaffold for adaptive layouts. Ensured Edge-to-Edge support and vibrant M3 theming. Created adaptive app icon.
- **Acceptance Criteria:**
  - Task List screen shows tasks with priority and category indicators
  - Add/Edit screen includes pickers for Category, Priority, and Due Date
  - Adaptive layout (List-Detail or Pane) works on larger screens
  - UI follows Material 3 guidelines and is Edge-to-Edge
- **Duration:** N/A

### Task_4_App_Icon_And_Verification: Create an adaptive app icon, refine the Material 3 theme with a vibrant color scheme, and perform a final verification of all features.
- **Status:** IN_PROGRESS
- **Acceptance Criteria:**
  - Adaptive app icon is present
  - App uses a vibrant M3 color scheme (Light/Dark)
  - All CRUD operations, filtering, and sorting work as expected
  - No crashes during run and verify
  - Build pass
  - Make sure all existing tests pass
- **StartTime:** 2026-07-07 12:51:02 IST

