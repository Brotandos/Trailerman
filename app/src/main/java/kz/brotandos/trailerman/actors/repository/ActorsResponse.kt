package kz.brotandos.trailerman.actors.repository

import com.google.gson.annotations.SerializedName
import kz.brotandos.trailerman.common.models.NetworkResponseModel
import kz.brotandos.trailerman.actors.Actor

data class ActorsResponse(
    val page: Int,
    val results: List<Actor>,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int
) : NetworkResponseModel
