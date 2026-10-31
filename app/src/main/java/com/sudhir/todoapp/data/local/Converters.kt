package com.sudhir.todoapp.data.local

import androidx.room.TypeConverter
import com.sudhir.todoapp.data.model.Category
import com.sudhir.todoapp.data.model.Priority

class Converters {
    @TypeConverter
    fun fromCategory(value: Category): String {
        return value.name
    }

    @TypeConverter
    fun toCategory(value: String): Category {
        return Category.valueOf(value)
    }

    @TypeConverter
    fun fromPriority(value: Priority): String {
        return value.name
    }

    @TypeConverter
    fun toPriority(value: String): Priority {
        return Priority.valueOf(value)
    }
}
