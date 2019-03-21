package kz.brotandos.trailerman.common.models.network

import kz.brotandos.trailerman.common.models.NetworkResponseModel
import kz.brotandos.trailerman.common.models.Review


class ReviewsResponse(
    val id: Int,
    val page: Int,
    val results: List<Review>,
    val totalPages: Int,
    val totalResults: Int
) : NetworkResponseModel