package com.example.todolistjadwal.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val taskDao: TaskDao, private val scheduleDao: ScheduleDao) {

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()
    val allSchedules: Flow<List<Schedule>> = scheduleDao.getAllSchedules()

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun insertSchedule(schedule: Schedule) {
        scheduleDao.insertSchedule(schedule)
    }

    suspend fun updateSchedule(schedule: Schedule) {
        scheduleDao.updateSchedule(schedule)
    }

    suspend fun deleteSchedule(schedule: Schedule) {
        scheduleDao.deleteSchedule(schedule)
    }
}
