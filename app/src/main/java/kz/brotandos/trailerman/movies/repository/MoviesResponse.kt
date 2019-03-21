package kz.brotandos.trailerman.movies.repository

import com.google.gson.annotations.SerializedName
import kz.brotandos.trailerman.common.models.NetworkResponseModel
import kz.brotandos.trailerman.movies.Movie

data class MoviesResponse(
    val page: Int,
    val results: List<Movie>,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int
) : NetworkResponseModel