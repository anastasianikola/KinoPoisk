package com.example.kinopoisk.domain

import com.example.kinopoisk.data.FilmsResponse
import retrofit2.http.GET

interface RetrofitAPI {
    @GET("films.json")
    suspend fun getFilms(): FilmsResponse
}