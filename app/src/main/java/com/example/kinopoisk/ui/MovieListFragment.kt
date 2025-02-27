package com.example.kinopoisk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.kinopoisk.data.FilmsModel
import com.example.kinopoisk.ui.theme.TopAppBarColor

class MovieListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MovieListScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(viewModel: MovieListViewModel = viewModel()) {
    val films by viewModel.films.collectAsState()
    val genres by viewModel.genres.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val state by viewModel.state.collectAsState()

    var selectedGenre by remember { mutableStateOf<String?>(null) }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    val columns = if (isLandscape) 3 else 2

    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Фильмы",
                    fontSize = 18.sp,
                    fontWeight = FontWeight(500),
                    color = Color.White
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = TopAppBarColor,
                titleContentColor = Color.White
            ),
            modifier = Modifier.shadow(4.dp)
        )

        when {
            state.isEmpty() -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                )
            }
            errorMessage?.isNotEmpty() == true -> {
                Text(
                    text = errorMessage ?: "Неизвестная ошибка",
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    item {
                        Text(
                            text = "Жанры",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    // Список жанров
                    items(genres?.filterNotNull() ?: emptyList()) { genre ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .clickable {
                                    selectedGenre = if (selectedGenre == genre) null else genre
                                }
                                .background(
                                    if (selectedGenre == genre) Color.LightGray else Color.Transparent
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = genre.toString(),
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Фильмы",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    val filteredFilms = if (selectedGenre == null) {
                        films
                    } else {
                        films.filter { film ->
                            film.genres?.contains(selectedGenre) == true
                        }
                    }

                    items(filteredFilms.chunked(columns)) { chunk ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            chunk.forEach { film ->
                                FilmCard(film = film)
                            }
                            repeat(columns - chunk.size) {
                                Spacer(modifier = Modifier.width(160.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilmCard(film: FilmsModel) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .padding(horizontal = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White)
        ) {
            AsyncImage(
                model = film.image_url,
                contentDescription = "Постер фильма ${film.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = film.localized_name,
                textAlign = TextAlign.Start, // Текст выровнен по левому краю
                fontWeight = FontWeight(700),
                fontSize = 16.sp,
                lineHeight = 20.sp,
                maxLines = 2
            )
        }
    }
}