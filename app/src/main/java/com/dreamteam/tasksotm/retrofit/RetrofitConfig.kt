package com.dreamteam.tasksotm.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConfig {
    private val retrofit: Retrofit
    private val baseUrl = "https://329f87d79542.ngrok.io/"

    constructor() {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun apiService(): APIService = retrofit.create(APIService::class.java)
}