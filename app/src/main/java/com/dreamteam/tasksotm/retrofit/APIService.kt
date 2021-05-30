package com.dreamteam.tasksotm.retrofit

import com.dreamteam.tasksotm.data.ProblemModel
import com.dreamteam.tasksotm.data.TaskModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {
    @POST("problem/solve")
    fun sendProblem(@Body problem: ProblemModel): Call<List<TaskModel>>
}