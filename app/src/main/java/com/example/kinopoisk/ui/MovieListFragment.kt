package com.example.kinopoisk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.kinopoisk.R
import com.example.kinopoisk.data.FilmsModel
import com.example.kinopoisk.domain.MovieListState
import com.example.kinopoisk.ui.theme.SelectedColor
import com.example.kinopoisk.ui.theme.TopAppBarColor

class MovieListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MovieListScreen(onNavigateToDetails = { film ->
                    val detailsFragment = DetailsFragment().apply {
                        arguments = Bundle().apply {
                            putString("film_name", film.name)
                            putString("film_localized_name", film.localized_name)
                            putString("film_genres", film.genres.joinToString(", "))
                            putString("film_rating", film.rating.toString())
                            putString("film_year", film.year.toString())
                            putString("film_image_url", film.image_url)
                            putString("film_description", film.description)
                        }
                    }
                    parentFragmentManager.commit {
                        replace(R.id.fragment_container, detailsFragment)
                        addToBackStack(null)
                    }
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = viewModel(),
    onNavigateToDetails: (FilmsModel) -> Unit
) {
    val films by viewModel.films.collectAsState()
    val genres by viewModel.genres.collectAsState()
    val state by viewModel.state.collectAsState()
    var selectedGenre by remember { mutableStateOf<String?>(null) }

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

        when (state) {
            is MovieListState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                ) {
                    CircularProgressIndicator(color = SelectedColor)
                }
            }
            is MovieListState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    item {
                        Text(
                            text = "Жанры",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    items(genres) { genre ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(35.dp)
                                .clickable {
                                    selectedGenre = if (selectedGenre == genre) null else genre
                                }
                                .background(
                                    color = if (selectedGenre == genre) SelectedColor else Color.White,
                                ),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = genre,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }

                val filteredFilms = if (selectedGenre == null) {
                    films
                } else {
                    films.filter { film ->
                        film.genres.any { genre ->
                            genre.equals(selectedGenre, ignoreCase = true)
                        }
                    }
                }

                Text(
                    text = "Фильмы",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    items(filteredFilms) { film ->
                        FilmCard(film = film, onNavigateToDetails = onNavigateToDetails)
                    }
                }
            }
            is MovieListState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF232323))
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = (state as MovieListState.Error).message,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Повторить",
                                color = SelectedColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { viewModel.retry() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilmCard(film: FilmsModel, onNavigateToDetails: (FilmsModel) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onNavigateToDetails(film) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column {
            AsyncImage(
                model = film.image_url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(1f).clip(RoundedCornerShape(10.dp)),
                error = painterResource(R.drawable.films_image)

            )

            Text(
                text = film.localized_name,
                color = Color.Black,
                fontWeight = FontWeight(700),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
            )
        }
    }
}