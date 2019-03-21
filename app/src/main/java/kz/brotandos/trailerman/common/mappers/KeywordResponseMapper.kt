package kz.brotandos.trailerman.common.mappers

import kz.brotandos.trailerman.common.models.network.KeywordsResponse

class KeywordResponseMapper :
    NetworkResponseMapper<KeywordsResponse> {
    override fun onLastPage(response: KeywordsResponse): Boolean = true
}