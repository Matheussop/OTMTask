package com.dreamteam.tasksotm.retrofit

import com.dreamteam.tasksotm.data.ProblemModel
import com.dreamteam.tasksotm.data.TaskModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object APIController {
    fun sendProblem(
        problemModel: ProblemModel,
        onSuccess: (response: List<TaskModel>) -> Unit,
        onError: () -> Unit
    ) {
        val call = RetrofitConfig().apiService().sendProblem(problemModel)

        call.enqueue(object : Callback<List<TaskModel>> {
            override fun onResponse(
                call: Call<List<TaskModel>>?,
                response: Response<List<TaskModel>>?
            ) {
                if (response != null) {
                    when {
                        response.isSuccessful -> {
                            onSuccess(response.body())
                        }
                        else -> onError()
                    }
                } else
                    onError()
            }

            override fun onFailure(call: Call<List<TaskModel>>?, t: Throwable?) {
                onError()
            }
        })
    }
}