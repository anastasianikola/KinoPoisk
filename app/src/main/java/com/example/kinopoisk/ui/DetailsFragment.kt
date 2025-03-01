package com.example.kinopoisk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import coil.compose.AsyncImage
import com.example.kinopoisk.R
import com.example.kinopoisk.ui.theme.TopAppBarColor
import androidx.compose.foundation.lazy.LazyColumn

class DetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val filmName = arguments?.getString("film_name") ?: ""
        val filmLocalizedName = arguments?.getString("film_localized_name") ?: ""
        val filmGenres = arguments?.getString("film_genres") ?: ""
        val filmRating = arguments?.getString("film_rating") ?: ""
        val filmImageUrl = arguments?.getString("film_image_url") ?: ""
        val filmYear = arguments?.getString("film_year") ?: ""
        val filmDescription = arguments?.getString("film_description") ?: ""

        return ComposeView(requireContext()).apply {
            setContent {
                DetailsScreen(
                    filmName = filmName,
                    filmImageUrl = filmImageUrl,
                    filmDescription = filmDescription,
                    filmLocalizedName = filmLocalizedName,
                    filmGenres = filmGenres,
                    filmRating = filmRating,
                    filmYear = filmYear,
                    onBack = {
                        parentFragmentManager.popBackStack()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetailsScreen(
    filmName: String,
    filmImageUrl: String,
    filmLocalizedName: String,
    filmDescription: String,
    filmGenres: String,
    filmYear: String,
    filmRating: String,
    onBack: () -> Unit
) {
    val filmRatingToDouble = filmRating.toDoubleOrNull() ?: 0.0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        item {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = filmName,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight(500),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TopAppBarColor
                )
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = filmImageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(132.dp)
                            .height(200.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        error = painterResource(R.drawable.films_image)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = filmLocalizedName,
                    fontSize = 26.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight(700)
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalArrangement = Arrangement.Center,
                    maxItemsInEachRow = 3
                ) {
                    if (filmGenres.isNotEmpty()) {
                        Text(
                            text = filmGenres + ",",
                            fontSize = 16.sp,
                            fontWeight = FontWeight(400),
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                    Text(
                        text = filmYear,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(400),
                        lineHeight = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "%.1f".format(filmRatingToDouble),
                        color = TopAppBarColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight(700),
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "КиноПоиск",
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        lineHeight = 16.sp,
                        color = TopAppBarColor,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = filmDescription,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight(400),
                    color = Color.Black
                )
            }
        }
    }
}