package kz.brotandos.trailerman.common.api

import androidx.lifecycle.LiveData
import kz.brotandos.trailerman.movies.repository.MoviesResponse
import kz.brotandos.trailerman.series.repository.SeriesListResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface DiscoverService {
    @GET("/3/discover/movie?language=en&sort_by=popularity.desc")
    fun fetchDiscoverMovie(@Query("page") page: Int): LiveData<ApiResponse<MoviesResponse>>

    @GET("/3/discover/tv?language=en&sort_by=popularity.desc")
    fun fetchDiscoverTv(@Query("page") page: Int): LiveData<ApiResponse<SeriesListResponse>>
}