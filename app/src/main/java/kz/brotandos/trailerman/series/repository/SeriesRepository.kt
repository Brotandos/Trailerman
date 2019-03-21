package kz.brotandos.trailerman.series.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.brotandos.trailerman.common.api.ApiResponse
import kz.brotandos.trailerman.common.mappers.KeywordResponseMapper
import kz.brotandos.trailerman.common.mappers.ReviewResponseMapper
import kz.brotandos.trailerman.common.mappers.VideoResponseMapper
import kz.brotandos.trailerman.common.models.Keyword
import kz.brotandos.trailerman.common.models.Resource
import kz.brotandos.trailerman.common.models.Review
import kz.brotandos.trailerman.common.models.Video
import kz.brotandos.trailerman.common.models.network.KeywordsResponse
import kz.brotandos.trailerman.common.models.network.ReviewsResponse
import kz.brotandos.trailerman.common.models.network.VideosResponse
import kz.brotandos.trailerman.common.repository.NetworkBoundRepository
import timber.log.Timber


class SeriesRepository(val service: SeriesService, val seriesDao: SeriesDao) {

    fun loadKeywords(id: Int): LiveData<Resource<List<Keyword>>> =
        object : NetworkBoundRepository<List<Keyword>, KeywordsResponse, KeywordResponseMapper>() {
            override fun saveFetchData(items: KeywordsResponse) {
                val series = seriesDao.getSeries(id)
                series.keywords = items.keywords
                seriesDao.updateSeries(series)
            }

            override fun shouldFetch(data: List<Keyword>?): Boolean =
                data == null || data.isEmpty()

            override fun loadFromDb(): LiveData<List<Keyword>> {
                val series = seriesDao.getSeries(id)
                val data: MutableLiveData<List<Keyword>> = MutableLiveData()
                data.value = series.keywords
                return data
            }

            override fun fetchService(): LiveData<ApiResponse<KeywordsResponse>> =
                service.fetchKeywords(id)

            override fun mapper() = KeywordResponseMapper()

            override fun onFetchFailed(message: String?) = Timber.d("onFetchFailed : $message")

        }.asLiveData()

    fun loadVideos(id: Int): LiveData<Resource<List<Video>>> {
        return object : NetworkBoundRepository<List<Video>, VideosResponse, VideoResponseMapper>() {
            override fun saveFetchData(items: VideosResponse) {
                val series = seriesDao.getSeries(id)
                series.videos = items.results
                seriesDao.updateSeries(series)
            }

            override fun shouldFetch(data: List<Video>?): Boolean =
                data == null || data.isEmpty()

            override fun loadFromDb(): LiveData<List<Video>> {
                val series = seriesDao.getSeries(id)
                val data: MutableLiveData<List<Video>> = MutableLiveData()
                data.value = series.videos
                return data
            }

            override fun fetchService(): LiveData<ApiResponse<VideosResponse>> =
                service.fetchVideos(id)

            override fun mapper() = VideoResponseMapper()

            override fun onFetchFailed(message: String?) = Timber.d("onFetchFailed : $message")

        }.asLiveData()
    }

    fun loadReviews(id: Int): LiveData<Resource<List<Review>>> =
        object : NetworkBoundRepository<List<Review>, ReviewsResponse, ReviewResponseMapper>() {
            override fun saveFetchData(items: ReviewsResponse) {
                val series = seriesDao.getSeries(id)
                series.reviews = items.results
                seriesDao.updateSeries(series)
            }

            override fun shouldFetch(data: List<Review>?): Boolean =
                data == null || data.isEmpty()

            override fun loadFromDb(): LiveData<List<Review>> {
                val series = seriesDao.getSeries(id)
                val data: MutableLiveData<List<Review>> = MutableLiveData()
                data.value = series.reviews
                return data
            }

            override fun fetchService(): LiveData<ApiResponse<ReviewsResponse>> =
                service.fetchReviews(id)

            override fun mapper(): ReviewResponseMapper =
                ReviewResponseMapper()

            override fun onFetchFailed(message: String?) =
                Timber.d("onFetchFailed : $message")

        }.asLiveData()
}
