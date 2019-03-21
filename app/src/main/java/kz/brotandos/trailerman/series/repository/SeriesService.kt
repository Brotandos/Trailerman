package kz.brotandos.trailerman.series.repository

import androidx.lifecycle.LiveData
import kz.brotandos.trailerman.common.api.ApiResponse
import kz.brotandos.trailerman.common.models.network.KeywordsResponse
import kz.brotandos.trailerman.common.models.network.ReviewsResponse
import kz.brotandos.trailerman.common.models.network.VideosResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface SeriesService {
    @GET("/3/tv/{tv_id}/keywords")
    fun fetchKeywords(@Path("tv_id") id: Int): LiveData<ApiResponse<KeywordsResponse>>

    @GET("/3/tv/{tv_id}/videos")
    fun fetchVideos(@Path("tv_id") id: Int): LiveData<ApiResponse<VideosResponse>>

    @GET("/3/tv/{tv_id}/reviews")
    fun fetchReviews(@Path("tv_id") id: Int): LiveData<ApiResponse<ReviewsResponse>>
}