package kz.brotandos.trailerman.actors.actor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kz.brotandos.trailerman.common.models.Resource
import kz.brotandos.trailerman.actors.repository.ActorRepository
import kz.brotandos.trailerman.common.utils.AbsentLiveData


class ActorDetailsViewModel(private val repository: ActorRepository) : ViewModel() {

    private val personIdLiveData: MutableLiveData<Int> = MutableLiveData()
    private val personLiveData: LiveData<Resource<ActorDetails>>

    init {
        personLiveData = Transformations.switchMap(personIdLiveData) {
            personIdLiveData.value?.let { repository.loadActorDetails(it) }
                ?: AbsentLiveData.create()
        }
    }

    fun getPersonObservable() = personLiveData
    fun postPersonId(id: Int) = personIdLiveData.postValue(id)
}
