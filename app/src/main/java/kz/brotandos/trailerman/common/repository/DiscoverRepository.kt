package kz.brotandos.trailerman.common.repository

import androidx.lifecycle.LiveData
import kz.brotandos.trailerman.common.api.ApiResponse
import kz.brotandos.trailerman.common.api.DiscoverService
import kz.brotandos.trailerman.common.mappers.NetworkResponseMapper
import kz.brotandos.trailerman.common.models.Resource
import kz.brotandos.trailerman.movies.repository.MoviesResponse
import kz.brotandos.trailerman.movies.Movie
import kz.brotandos.trailerman.movies.repository.MovieDao
import kz.brotandos.trailerman.series.Series
import kz.brotandos.trailerman.series.repository.SeriesDao
import kz.brotandos.trailerman.series.repository.SeriesListResponse
import timber.log.Timber

class DiscoverRepository(
    val discoverService: DiscoverService,
    val movieDao: MovieDao,
    val seriesDao: SeriesDao
) {

    fun loadMovies(page: Int): LiveData<Resource<List<Movie>>> =
        object : NetworkBoundRepository<List<Movie>, MoviesResponse, MovieResponseMapper>() {
            override fun saveFetchData(items: MoviesResponse) {
                items.results.forEach { it.page = page }
                movieDao.insertMovieList(movies = items.results)
            }

            override fun shouldFetch(data: List<Movie>?): Boolean =
                data == null || data.isEmpty()

            override fun loadFromDb(): LiveData<List<Movie>> =
                movieDao.getMovieList(page_ = page)

            override fun fetchService(): LiveData<ApiResponse<MoviesResponse>> =
                discoverService.fetchDiscoverMovie(page = page)

            override fun mapper(): MovieResponseMapper =
                MovieResponseMapper()

            override fun onFetchFailed(message: String?) =
                Timber.d("onFetchFailed $message")
        }.asLiveData()

    fun loadSeriesList(page: Int): LiveData<Resource<List<Series>>> =
        object : NetworkBoundRepository<List<Series>, SeriesListResponse, SeriesResponseMapper>() {
            override fun saveFetchData(items: SeriesListResponse) {
                val tvList = mutableListOf<Series>()
                for (item in items.results) {
                    item.page = page
                    // FIXME poster path could be null.
                    if (item.posterUrl != null) tvList.add(item)
                }
                seriesDao.insertSeries(series = tvList)
            }

            override fun shouldFetch(data: List<Series>?): Boolean =
                data == null || data.isEmpty()

            override fun loadFromDb(): LiveData<List<Series>> =
                seriesDao.getSeriesList(page_ = page)

            override fun fetchService(): LiveData<ApiResponse<SeriesListResponse>> =
                discoverService.fetchDiscoverTv(page = page)

            override fun mapper(): SeriesResponseMapper =
                SeriesResponseMapper()

            override fun onFetchFailed(message: String?) =
                Timber.d("onFetchFailed $message")
        }.asLiveData()


    class SeriesResponseMapper :
        NetworkResponseMapper<SeriesListResponse> {
        override fun onLastPage(response: SeriesListResponse): Boolean =
            response.page > response.totalPages
    }

    class MovieResponseMapper :
        NetworkResponseMapper<MoviesResponse> {
        override fun onLastPage(response: MoviesResponse): Boolean =
            response.page > response.totalPages
    }
}
