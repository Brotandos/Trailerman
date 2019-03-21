package kz.brotandos.trailerman.common.models.network

import kz.brotandos.trailerman.common.models.Keyword
import kz.brotandos.trailerman.common.models.NetworkResponseModel

data class KeywordsResponse(
    val id: Int,
    val keywords: List<Keyword>
) : NetworkResponseModel