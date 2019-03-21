package kz.brotandos.trailerman.movies.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kz.brotandos.trailerman.common.api.ApiUtil.successCall
import kz.brotandos.trailerman.common.models.Keyword
import kz.brotandos.trailerman.common.models.Resource
import kz.brotandos.trailerman.common.models.Review
import kz.brotandos.trailerman.common.models.Video
import kz.brotandos.trailerman.common.models.network.KeywordsResponse
import kz.brotandos.trailerman.common.models.network.ReviewsResponse
import kz.brotandos.trailerman.common.models.network.VideosResponse
import kz.brotandos.trailerman.movies.Movie
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MovieRepositoryTest {
    private lateinit var repository: MovieRepository
    private val movieDao = mock<MovieDao>()
    private val service = mock<MovieService>()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        repository = MovieRepository(service, movieDao)
    }

    private fun mockMovie() = Movie(123, "", 1, emptyList(), emptyList(), emptyList(), "", 0f, 0, false, "", "", ArrayList(), "", "", "", false, 0f)

    private fun mockKeywordList(): List<Keyword> {
        val keywords = ArrayList<Keyword>()
        keywords.add(Keyword(100, "keyword0"))
        keywords.add(Keyword(101, "keyword1"))
        keywords.add(Keyword(102, "keyword2"))
        return keywords
    }

    private fun mockVideoList(): List<Video> {
        val videos = ArrayList<Video>()
        videos.add(Video("123", "video0", "", "", 0, ""))
        videos.add(Video("123", "video0", "", "", 0, ""))
        return videos
    }

    private fun mockReviewList(): List<Review> {
        val reviews = ArrayList<Review>()
        reviews.add(Review("123", "", "", ""))
        reviews.add(Review("123", "", "", ""))
        return reviews
    }

    @Test
    fun loadKeywordListFromNetwork() {
        val loadFromDB = mockMovie()
        whenever(movieDao.getMovie(123)).thenReturn(loadFromDB)

        val mockResponse = KeywordsResponse(123, mockKeywordList())
        val call = successCall(mockResponse)
        whenever(service.fetchKeywords(123)).thenReturn(call)

        val data = repository.loadKeywords(123)
        verify(movieDao).getMovie(123)
        verifyNoMoreInteractions(service)

        val observer = mock<Observer<Resource<List<Keyword>>>>()
        data.observeForever(observer)
        verify(observer).onChanged(Resource.success(mockKeywordList(), true))

        val updatedMovie = mockMovie()
        updatedMovie.keywords = mockKeywordList()
        verify(movieDao).updateMovie(updatedMovie)
    }

    @Test
    fun loadVideoListFromNetwork() {
        val loadFromDB = mockMovie()
        whenever(movieDao.getMovie(123)).thenReturn(loadFromDB)

        val mockResponse = VideosResponse(123, mockVideoList())
        val call = successCall(mockResponse)
        whenever(service.fetchVideos(123)).thenReturn(call)

        val data = repository.loadVideos(123)
        verify(movieDao).getMovie(123)
        verifyNoMoreInteractions(service)

        val observer = mock<Observer<Resource<List<Video>>>>()
        data.observeForever(observer)
        verify(observer).onChanged(Resource.success(mockVideoList(), true))

        val updatedMovie = mockMovie()
        updatedMovie.videos = mockVideoList()
        verify(movieDao).updateMovie(updatedMovie)
    }

    @Test
    fun loadReviewListFromNetwork() {
        val loadFromDB = mockMovie()
        whenever(movieDao.getMovie(123)).thenReturn(loadFromDB)

        val mockResponse = ReviewsResponse(123, 1, mockReviewList(), 100, 100)
        val call = successCall(mockResponse)
        whenever(service.fetchReviews(123)).thenReturn(call)

        val data = repository.loadReviews(123)
        verify(movieDao).getMovie(123)
        verifyNoMoreInteractions(service)

        val observer = mock<Observer<Resource<List<Review>>>>()
        data.observeForever(observer)
        verify(observer).onChanged(Resource.success(mockReviewList(), true))

        val updatedMovie = mockMovie()
        updatedMovie.reviews = mockReviewList()
        verify(movieDao).updateMovie(updatedMovie)
    }
}