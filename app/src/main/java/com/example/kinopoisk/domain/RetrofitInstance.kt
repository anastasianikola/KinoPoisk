package com.example.kinopoisk.domain

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val baseUrl = "https://s3-eu-west-1.amazonaws.com/sequeniatesttask/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val filmsApi = retrofit.create(RetrofitAPI::class.java)
}