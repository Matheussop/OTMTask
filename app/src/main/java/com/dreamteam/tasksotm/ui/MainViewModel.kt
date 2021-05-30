package com.dreamteam.tasksotm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamteam.tasksotm.data.ProblemModel
import com.dreamteam.tasksotm.data.TaskModel
import com.dreamteam.tasksotm.retrofit.APIController
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var incompleteTasks = true
    var timeAvailable = 5.0

    private var taskIdCount = 0
        get() {
            return field++
        }

    private var _tasksList = MutableLiveData(emptyList<TaskModel>())
    var tasksList: LiveData<List<TaskModel>> = _tasksList

    init {
        // Dummy
        _tasksList.postValue(
            listOf(
                TaskModel(id = -3, name = "Prova de Cálculo 2", workload = 4.0, points = 30.0),
                TaskModel(id = -2, name = "Prova de Compiladores", workload = 3.0, points = 20.0),
                TaskModel(id = -1, name = "Trabalho de inglês", workload = 0.5, points = 2.0),
            )
        )
    }

    fun addTask(taskName: String, taskWorkload: Double, taskPoints: Double) {
        val newTask = TaskModel(
            id = taskIdCount,
            name = taskName,
            workload = taskWorkload,
            points = taskPoints
        )

        tasksList.value?.let { list ->
            _tasksList.postValue(
                list + listOf(newTask)
            )
        }
    }

    fun removeTask(taskId: Int) {
        tasksList.value?.let { list ->
            _tasksList.postValue(
                list.filter { it.id != taskId }
            )
        }
    }

    fun runSolver(onSuccess: (List<TaskModel>) -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            val problemModel = ProblemModel(
                tasksList = tasksList.value!!,
                timeAvailable = timeAvailable,
                acceptIncompleteTasks = incompleteTasks
            )
            APIController.sendProblem(problemModel,
                onSuccess = { response ->
                    onSuccess(response)
                }, onError = {
                    onError()
                }
            )
        }
    }
}