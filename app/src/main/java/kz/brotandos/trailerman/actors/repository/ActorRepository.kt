package kz.brotandos.trailerman.actors.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.brotandos.trailerman.actors.Actor
import kz.brotandos.trailerman.actors.actor.ActorDetails
import kz.brotandos.trailerman.actors.actor.ActorDetailsResponseMapper
import kz.brotandos.trailerman.common.api.ApiResponse
import kz.brotandos.trailerman.common.mappers.NetworkResponseMapper
import kz.brotandos.trailerman.common.models.Resource
import kz.brotandos.trailerman.common.repository.NetworkBoundRepository
import timber.log.Timber

class ActorRepository(val actorService: ActorService, val actorDao: ActorDao) {

    fun loadActors(page: Int): LiveData<Resource<List<Actor>>> =
        object : NetworkBoundRepository<List<Actor>, ActorsResponse, ActorsResponseMapper>() {
            override fun saveFetchData(items: ActorsResponse) {
                items.results.forEach { it.page = page }
                actorDao.insertActor(items.results)
            }

            override fun shouldFetch(data: List<Actor>?): Boolean =
                data == null || data.isEmpty()

            override fun loadFromDb(): LiveData<List<Actor>> =
                actorDao.getActors(page_ = page)

            override fun fetchService(): LiveData<ApiResponse<ActorsResponse>> =
                actorService.fetchActors(page = page)

            override fun mapper() =
                ActorsResponseMapper()

            override fun onFetchFailed(message: String?) = Timber.d("onFetchFailed : $message")

        }.asLiveData()

    fun loadActorDetails(id: Int): LiveData<Resource<ActorDetails>> =
        object : NetworkBoundRepository<ActorDetails, ActorDetails, ActorDetailsResponseMapper>() {
            override fun saveFetchData(items: ActorDetails) {
                val person = actorDao.getActor(id_ = id)
                person.actorDetails = items
                actorDao.updateActor(actor = person)
            }

            override fun shouldFetch(data: ActorDetails?): Boolean =
                data == null || data.biography.isEmpty()

            override fun loadFromDb(): LiveData<ActorDetails> {
                val person = actorDao.getActor(id_ = id)
                val data: MutableLiveData<ActorDetails> = MutableLiveData()
                data.value = person.actorDetails
                return data
            }

            override fun fetchService(): LiveData<ApiResponse<ActorDetails>> =
                actorService.fetchActorDetails(id = id)

            override fun mapper() = ActorDetailsResponseMapper()

            override fun onFetchFailed(message: String?) = Timber.d("onFetchFailed : $message")

        }.asLiveData()


    class ActorsResponseMapper :
        NetworkResponseMapper<ActorsResponse> {
        override fun onLastPage(response: ActorsResponse): Boolean =
            response.page > response.totalPages
    }
}