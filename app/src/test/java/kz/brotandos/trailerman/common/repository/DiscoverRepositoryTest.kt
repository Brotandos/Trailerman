package kz.brotandos.trailerman.common.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kz.brotandos.trailerman.common.api.ApiResponse
import kz.brotandos.trailerman.common.api.DiscoverService
import kz.brotandos.trailerman.common.models.Resource
import kz.brotandos.trailerman.movies.Movie
import kz.brotandos.trailerman.movies.repository.MovieDao
import kz.brotandos.trailerman.movies.repository.MoviesResponse
import kz.brotandos.trailerman.series.Series
import kz.brotandos.trailerman.series.repository.SeriesDao
import kz.brotandos.trailerman.series.repository.SeriesListResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

@RunWith(JUnit4::class)
class DiscoverRepositoryTest {

    private lateinit var repository: DiscoverRepository
    private val movieDao = mock<MovieDao>()
    private val seriesDao = mock<SeriesDao>()
    private val service = mock<DiscoverService>()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        repository = DiscoverRepository(service, movieDao, seriesDao)
    }

    @Test
    fun loadMovieListFromNetwork() {
        val loadFromDB = MutableLiveData<List<Movie>>()
        whenever(movieDao.getMovieList(1)).thenReturn(loadFromDB)

        val mockResponse = MoviesResponse(1, emptyList(), 100, 10)
        val call = successCall(mockResponse)
        whenever(service.fetchDiscoverMovie(1)).thenReturn(call)

        val data = repository.loadMovies(1)
        verify(movieDao).getMovieList(1)
        verifyNoMoreInteractions(service)

        val observer = mock<Observer<Resource<List<Movie>>>>()
        data.observeForever(observer)
        verifyNoMoreInteractions(service)
        val updatedData = MutableLiveData<List<Movie>>()
        whenever(movieDao.getMovieList(1)).thenReturn(updatedData)

        loadFromDB.postValue(null)
        verify(observer).onChanged(Resource.loading(null))
        verify(service).fetchDiscoverMovie(1)
        verify(movieDao).insertMovieList(mockResponse.results)

        updatedData.postValue(mockResponse.results)
        verify(observer).onChanged(Resource.success(mockResponse.results, false))
    }

    @Test
    fun loadTvListFromNetwork() {
        val loadFromDb = MutableLiveData<List<Series>>()
        whenever(seriesDao.getSeriesList(1)).thenReturn(loadFromDb)

        val mockResponse = SeriesListResponse(1, emptyList(), 100, 10)
        val call = successCall(mockResponse)
        whenever(service.fetchDiscoverTv(1)).thenReturn(call)

        val data = repository.loadSeriesList(1)
        verify(seriesDao).getSeriesList(1)
        verifyNoMoreInteractions(service)

        val observer = mock<Observer<Resource<List<Series>>>>()
        data.observeForever(observer)
        verifyNoMoreInteractions(service)
        val updateData = MutableLiveData<List<Series>>()
        whenever(seriesDao.getSeriesList(1)).thenReturn(updateData)

        loadFromDb.postValue(null)
        verify(observer).onChanged(Resource.loading(null))
        verify(service).fetchDiscoverTv(1)
        verify(seriesDao).insertSeries(mockResponse.results)

        updateData.postValue(mockResponse.results)
        verify(observer).onChanged(Resource.success(mockResponse.results, false))
    }

    private fun <T : Any> successCall(data: T) = createCall(Response.success(data))

    private fun <T : Any> createCall(response: Response<T>) = MutableLiveData<ApiResponse<T>>().apply {
        value = ApiResponse(response)
    } as LiveData<ApiResponse<T>>
}