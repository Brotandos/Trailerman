package kz.brotandos.trailerman.common.mappers

import kz.brotandos.trailerman.common.models.NetworkResponseModel


interface NetworkResponseMapper<in FROM : NetworkResponseModel> {
    fun onLastPage(response: FROM): Boolean
}
