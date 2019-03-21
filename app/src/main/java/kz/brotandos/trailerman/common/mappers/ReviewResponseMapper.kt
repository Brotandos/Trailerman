package kz.brotandos.trailerman.common.mappers

import kz.brotandos.trailerman.common.models.network.ReviewsResponse


class ReviewResponseMapper :
    NetworkResponseMapper<ReviewsResponse> {
    override fun onLastPage(response: ReviewsResponse): Boolean = true
}