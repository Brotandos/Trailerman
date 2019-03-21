package kz.brotandos.trailerman.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kz.brotandos.trailerman.common.models.Resource
import kz.brotandos.trailerman.movies.Movie
import kz.brotandos.trailerman.actors.Actor
import kz.brotandos.trailerman.series.Series
import kz.brotandos.trailerman.common.repository.DiscoverRepository
import kz.brotandos.trailerman.actors.repository.ActorRepository
import kz.brotandos.trailerman.common.utils.AbsentLiveData

class MainActivityViewModel(
    private val discoverRepository: DiscoverRepository,
    private val actorRepository: ActorRepository
) : ViewModel() {

    private var moviePageLiveData: MutableLiveData<Int> = MutableLiveData()
    private val movieListLiveData: LiveData<Resource<List<Movie>>>

    private var tvPageLiveData: MutableLiveData<Int> = MutableLiveData()
    private val seriesListLiveData: LiveData<Resource<List<Series>>>

    private var peoplePageLiveData: MutableLiveData<Int> = MutableLiveData()
    private val peopleLiveData: LiveData<Resource<List<Actor>>>

    init {
        movieListLiveData = Transformations.switchMap(moviePageLiveData) {
            moviePageLiveData.value?.let(discoverRepository::loadMovies) ?: AbsentLiveData.create()
        }

        seriesListLiveData = Transformations.switchMap(tvPageLiveData) {
            tvPageLiveData.value?.let(discoverRepository::loadSeriesList) ?: AbsentLiveData.create()
        }

        peopleLiveData = Transformations.switchMap(peoplePageLiveData) {
            peoplePageLiveData.value?.let(actorRepository::loadActors) ?: AbsentLiveData.create()
        }
    }

    fun getMovieListObservable() = movieListLiveData
    fun getMovieListValues() = getMovieListObservable().value
    fun postMoviePage(page: Int) = moviePageLiveData.postValue(page)

    fun getTvListObservable() = seriesListLiveData
    fun getSeriesListValues() = getTvListObservable().value
    fun postSeriesPage(page: Int) = tvPageLiveData.postValue(page)

    fun getPeopleObservable() = peopleLiveData
    fun getPeopleValues() = getPeopleObservable().value
    fun postPeoplePage(page: Int) = peoplePageLiveData.postValue(page)
}
