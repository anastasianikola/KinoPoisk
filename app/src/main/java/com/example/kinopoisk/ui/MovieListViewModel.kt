package com.example.kinopoisk.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.data.FilmsModel
import com.example.kinopoisk.domain.MovieListState
import com.example.kinopoisk.domain.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieListViewModel : ViewModel() {
    private val _films = MutableStateFlow<List<FilmsModel>>(emptyList())
    val films: StateFlow<List<FilmsModel>> = _films

    private val _genres = MutableStateFlow<List<String>>(emptyList())
    val genres: StateFlow<List<String>> = _genres

    private val _state = MutableStateFlow<MovieListState>(MovieListState.Loading)
    val state: StateFlow<MovieListState> = _state

    init {
        fetchFilms()
    }

    private fun fetchFilms() {
        viewModelScope.launch {
            _state.value = MovieListState.Loading
            try {
                val response = RetrofitInstance.filmsApi.getFilms()
                _films.value = response.films
                fetchGenres(response.films)
                _state.value = MovieListState.Success(response.films)
            } catch (e: Exception) {
                _state.value = MovieListState.Error("Ошибка подключения сети")
            }
        }
    }

    private fun fetchGenres(films: List<FilmsModel>) {
        viewModelScope.launch {
            try {
                val uniqueGenres = films
                    .flatMap { it.genres }
                    .map { it.replaceFirstChar { char -> char.uppercaseChar() } }
                    .toSet()
                    .sorted()
                    .toList()
                _genres.value = uniqueGenres
            } catch (e: Exception) {
                _genres.value = emptyList()
                _state.value = MovieListState.Error("Ошибка при извлечении категорий")
            }
        }
    }

    fun retry() {
        fetchFilms()
    }
}