package kz.brotandos.trailerman.movies.repository

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

class MovieRepository(val service: MovieService, val movieDao: MovieDao) {

    fun loadKeywords(id: Int): LiveData<Resource<List<Keyword>>> =
        object : NetworkBoundRepository<List<Keyword>, KeywordsResponse, KeywordResponseMapper>() {
            override fun saveFetchData(items: KeywordsResponse) {
                val movie = movieDao.getMovie(id_ = id)
                movie.keywords = items.keywords
                movieDao.updateMovie(movie = movie)
            }

            override fun shouldFetch(data: List<Keyword>?): Boolean =
                data == null || data.isEmpty()

            override fun loadFromDb(): LiveData<List<Keyword>> {
                val movie = movieDao.getMovie(id_ = id)
                val data: MutableLiveData<List<Keyword>> = MutableLiveData()
                data.value = movie.keywords
                return data
            }

            override fun fetchService(): LiveData<ApiResponse<KeywordsResponse>> =
                service.fetchKeywords(id = id)

            override fun mapper(): KeywordResponseMapper =
                KeywordResponseMapper()

            override fun onFetchFailed(message: String?) =
                Timber.d("onFetchFailed : $message")
        }.asLiveData()

    fun loadVideos(id: Int): LiveData<Resource<List<Video>>> =
        object : NetworkBoundRepository<List<Video>, VideosResponse, VideoResponseMapper>() {
            override fun saveFetchData(items: VideosResponse) {
                val movie = movieDao.getMovie(id_ = id)
                movie.videos = items.results
                movieDao.updateMovie(movie = movie)
            }

            override fun shouldFetch(data: List<Video>?): Boolean =
                data == null || data.isEmpty()

            override fun loadFromDb(): LiveData<List<Video>> {
                val movie = movieDao.getMovie(id_ = id)
                val data: MutableLiveData<List<Video>> = MutableLiveData()
                data.value = movie.videos
                return data
            }

            override fun fetchService(): LiveData<ApiResponse<VideosResponse>> =
                service.fetchVideos(id = id)

            override fun mapper(): VideoResponseMapper =
                VideoResponseMapper()

            override fun onFetchFailed(message: String?) =
                Timber.d("onFetchFailed : $message")
        }.asLiveData()

    fun loadReviews(id: Int): LiveData<Resource<List<Review>>> =
        object : NetworkBoundRepository<List<Review>, ReviewsResponse, ReviewResponseMapper>() {
            override fun saveFetchData(items: ReviewsResponse) {
                val movie = movieDao.getMovie(id_ = id)
                movie.reviews = items.results
                movieDao.updateMovie(movie = movie)
            }

            override fun shouldFetch(data: List<Review>?): Boolean =
                data == null || data.isEmpty()

            override fun loadFromDb(): LiveData<List<Review>> {
                val movie = movieDao.getMovie(id_ = id)
                val data: MutableLiveData<List<Review>> = MutableLiveData()
                data.value = movie.reviews
                return data
            }

            override fun fetchService(): LiveData<ApiResponse<ReviewsResponse>> =
                service.fetchReviews(id = id)

            override fun mapper(): ReviewResponseMapper =
                ReviewResponseMapper()

            override fun onFetchFailed(message: String?) =
                Timber.d("onFetchFailed : $message")
        }.asLiveData()
}
