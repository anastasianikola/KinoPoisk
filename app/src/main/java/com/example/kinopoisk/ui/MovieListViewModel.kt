package com.example.kinopoisk.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.data.FilmsModel
import com.example.kinopoisk.domain.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieListViewModel : ViewModel() {
    private val _films = MutableStateFlow<List<FilmsModel>>(emptyList())
    val films: StateFlow<List<FilmsModel>> = _films

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _state = MutableStateFlow(emptyList<FilmsModel>())
    val state: StateFlow<List<FilmsModel>> = _state
    private val _genres = MutableStateFlow<List<String?>>(emptyList())
    val genres: StateFlow<List<String?>> = _genres
    init{
        fetchFilms()
        fetchGenres(_films.value)
    }

    fun fetchFilms(){
        viewModelScope.launch {
            try{
                val response = RetrofitInstance.filmsApi.getFilms()
                _films.value = response.films
                _state.value = response.films
                _errorMessage.value = null
                fetchGenres(response.films)
                println(response.films)
            } catch (e: Exception){
                _films.value = emptyList()
                _errorMessage.value = "Ошибка подключения сети"
            }
        }
    }
    fun fetchGenres(films: List<FilmsModel>){
        viewModelScope.launch {
            try{
                val uniqueGenres  = films
                    .flatMap { it.genres }
                    .map { it.replaceFirstChar { char -> char.uppercaseChar() } }
                    .toSet()
                    .sorted()
                    .toList()
                _genres.value = uniqueGenres
                println(uniqueGenres)
            } catch (e: Exception){
                _genres.value = emptyList()
                _errorMessage.value = "Ошибка при извлечении категорий"
            }
        }
    }

}