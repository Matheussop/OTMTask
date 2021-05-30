package com.dreamteam.tasksotm.data

import com.google.gson.annotations.SerializedName


data class TaskModel(
    @SerializedName(value = "id")
    val id: Int,
    @SerializedName(value = "name")
    val name: String,
    @SerializedName(value = "workload")
    val workload: Double,
    @SerializedName(value = "workload_percent")
    val workloadPercent: Double = 0.0,
    @SerializedName(value = "points")
    val points: Double
)