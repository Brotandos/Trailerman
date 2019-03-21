package kz.brotandos.trailerman.common.models.network

import kz.brotandos.trailerman.common.models.NetworkResponseModel
import kz.brotandos.trailerman.common.models.Video

data class VideosResponse(
    val id: Int,
    val results: List<Video>
) : NetworkResponseModel