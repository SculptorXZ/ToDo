package com.example.todolistjadwal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedules")
data class Schedule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectName: String,
    val dayOfWeek: Int,
    val startTime: String,
    val endTime: String,
    val room: String
)
