package kz.brotandos.trailerman.series.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kz.brotandos.trailerman.common.models.Keyword
import kz.brotandos.trailerman.common.models.Resource
import kz.brotandos.trailerman.common.models.Review
import kz.brotandos.trailerman.common.models.Video
import kz.brotandos.trailerman.common.utils.AbsentLiveData
import kz.brotandos.trailerman.series.repository.SeriesRepository


class SeriesDetailViewModel(private val repository: SeriesRepository) : ViewModel() {

    private val keywordIdLiveData: MutableLiveData<Int> = MutableLiveData()
    private val keywordListLiveData: LiveData<Resource<List<Keyword>>>

    private val videoIdLiveData: MutableLiveData<Int> = MutableLiveData()
    private val videoListLiveData: LiveData<Resource<List<Video>>>

    private val reviewIdLiveData: MutableLiveData<Int> = MutableLiveData()
    private val reviewListLiveData: LiveData<Resource<List<Review>>>

    init {
        keywordListLiveData = Transformations.switchMap(keywordIdLiveData) {
            keywordIdLiveData.value?.let(repository::loadKeywords) ?: AbsentLiveData.create()
        }

        videoListLiveData = Transformations.switchMap(videoIdLiveData) {
            videoIdLiveData.value?.let(repository::loadVideos) ?: AbsentLiveData.create()
        }

        reviewListLiveData = Transformations.switchMap(reviewIdLiveData) {
            reviewIdLiveData.value?.let(repository::loadReviews) ?: AbsentLiveData.create()
        }
    }

    fun getKeywordListObservable() = keywordListLiveData
    fun postKeywordId(id: Int) = keywordIdLiveData.postValue(id)

    fun getVideoListObservable() = videoListLiveData
    fun postVideoId(id: Int) = videoIdLiveData.postValue(id)

    fun getReviewListObservable() = reviewListLiveData
    fun postReviewId(id: Int) = reviewIdLiveData.postValue(id)
}
