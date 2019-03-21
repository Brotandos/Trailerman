package kz.brotandos.trailerman.series.repository

import com.google.gson.annotations.SerializedName
import kz.brotandos.trailerman.common.models.NetworkResponseModel
import kz.brotandos.trailerman.series.Series

data class SeriesListResponse(
    val page: Int,
    val results: List<Series>,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int
) : NetworkResponseModel