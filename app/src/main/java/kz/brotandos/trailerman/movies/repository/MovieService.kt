package kz.brotandos.trailerman.movies.repository

import androidx.lifecycle.LiveData
import kz.brotandos.trailerman.common.api.ApiResponse
import kz.brotandos.trailerman.common.models.network.KeywordsResponse
import kz.brotandos.trailerman.common.models.network.ReviewsResponse
import kz.brotandos.trailerman.common.models.network.VideosResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface MovieService {
    @GET("/3/movie/{movie_id}/keywords")
    fun fetchKeywords(@Path("movie_id") id: Int): LiveData<ApiResponse<KeywordsResponse>>

    @GET("/3/movie/{movie_id}/videos")
    fun fetchVideos(@Path("movie_id") id: Int): LiveData<ApiResponse<VideosResponse>>

    @GET("/3/movie/{movie_id}/reviews")
    fun fetchReviews(@Path("movie_id") id: Int): LiveData<ApiResponse<ReviewsResponse>>
}