package kz.brotandos.trailerman.common.models

data class ResponseModel(
    val page: Int,
    val results: Any,
    val totalResults: Int,
    val totalPages: Int
)
