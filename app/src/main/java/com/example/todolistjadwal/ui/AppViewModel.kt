package com.example.todolistjadwal.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.todolistjadwal.data.*
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AppRepository
    val allTasks: LiveData<List<Task>>
    val allSchedules: LiveData<List<Schedule>>

    init {
        val database = AppDatabase.getDatabase(application)
        repository = AppRepository(database.taskDao(), database.scheduleDao())
        allTasks = repository.allTasks.asLiveData()
        allSchedules = repository.allSchedules.asLiveData()
    }

    fun insertTask(task: Task) = viewModelScope.launch {
        repository.insertTask(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        repository.updateTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
    }

    fun insertSchedule(schedule: Schedule) = viewModelScope.launch {
        repository.insertSchedule(schedule)
    }

    fun updateSchedule(schedule: Schedule) = viewModelScope.launch {
        repository.updateSchedule(schedule)
    }

    fun deleteSchedule(schedule: Schedule) = viewModelScope.launch {
        repository.deleteSchedule(schedule)
    }
}
