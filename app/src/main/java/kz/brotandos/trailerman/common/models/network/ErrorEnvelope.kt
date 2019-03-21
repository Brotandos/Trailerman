package kz.brotandos.trailerman.common.models.network

data class ErrorEnvelope(
    val statusCode: Int,
    val statusMessage: String,
    val success: Boolean
)