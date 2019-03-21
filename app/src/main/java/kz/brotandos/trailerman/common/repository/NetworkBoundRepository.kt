package kz.brotandos.trailerman.common.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kz.brotandos.trailerman.common.api.ApiResponse
import kz.brotandos.trailerman.common.mappers.NetworkResponseMapper
import kz.brotandos.trailerman.common.models.NetworkResponseModel
import kz.brotandos.trailerman.common.models.Resource

abstract class NetworkBoundRepository<
        ResultType,
        RequestType : NetworkResponseModel,
        Mapper : NetworkResponseMapper<RequestType>>
internal constructor() {

    private val result: MediatorLiveData<Resource<ResultType>> = MediatorLiveData()

    init {
        val loadedFromDB = this.loadFromDb()
        result.addSource(loadedFromDB) { data ->
            result.removeSource(loadedFromDB)
            if (shouldFetch(data)) {
                result.postValue(Resource.loading(null))
                fetchFromNetwork(loadedFromDB)
            } else {
                result.addSource<ResultType>(loadedFromDB) { newData ->
                    setValue(Resource.success(newData, false))
                }
            }
        }
    }

    private fun fetchFromNetwork(loadedFromDB: LiveData<ResultType>) {
        val apiResponse = fetchService()
        result.addSource(apiResponse) { response ->
            if (response.isSuccessful) {
                response.body?.let {
                    saveFetchData(it)
                    val loaded = loadFromDb()
                    result.addSource(loaded) { newData ->
                        setValue(Resource.success(newData, mapper().onLastPage(response.body)))
                    }
                }
            } else {
                result.removeSource(loadedFromDB)
                onFetchFailed(response.message)
                response.message?.let {
                    result.addSource<ResultType>(loadedFromDB) { newData ->
                        setValue(Resource.error(it, newData))
                    }
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) = result.setValue(newValue)

    fun asLiveData(): LiveData<Resource<ResultType>> = result

    @WorkerThread
    protected abstract fun saveFetchData(items: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun fetchService(): LiveData<ApiResponse<RequestType>>

    @MainThread
    protected abstract fun mapper(): Mapper

    @MainThread
    protected abstract fun onFetchFailed(message: String?)
}
