package kz.brotandos.trailerman.common.mappers

import kz.brotandos.trailerman.common.models.network.VideosResponse

class VideoResponseMapper :
    NetworkResponseMapper<VideosResponse> {
    override fun onLastPage(response: VideosResponse): Boolean = true
}