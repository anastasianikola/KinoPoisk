package com.example.kinopoisk.data

import com.google.gson.annotations.JsonAdapter

data class FilmsModel(
    val id: Int,
    val localized_name: String,
    val name: String,
    val year: Int,
    val rating: Float,
    val image_url: String,
    val description: String,
    val genres: List<String>
)
