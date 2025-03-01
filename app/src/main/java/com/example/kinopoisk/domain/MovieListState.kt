package com.example.kinopoisk.domain

import com.example.kinopoisk.data.FilmsModel

sealed class MovieListState {
    object Loading : MovieListState()
    data class Success(val films: List<FilmsModel>) : MovieListState()
    data class Error(val message: String) : MovieListState()
}