package com.dreamteam.tasksotm.data

import com.google.gson.annotations.SerializedName

data class ProblemModel (
    @SerializedName(value = "tasks_list")
    val tasksList: List<TaskModel>,
    @SerializedName(value = "time_available")
    val timeAvailable: Double,
    @SerializedName(value = "accept_incomplete_tasks")
    val acceptIncompleteTasks: Boolean
)