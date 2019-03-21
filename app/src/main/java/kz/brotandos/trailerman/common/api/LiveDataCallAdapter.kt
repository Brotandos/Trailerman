package kz.brotandos.trailerman.common.api

import androidx.lifecycle.LiveData
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean


class LiveDataCallAdapter<R>(private val responseType: Type) : CallAdapter<R, LiveData<ApiResponse<R>>> {

    override fun responseType(): Type = responseType

    override fun adapt(call: Call<R>): LiveData<ApiResponse<R>> = object : LiveData<ApiResponse<R>>() {
        var started = AtomicBoolean(false)
        override fun onActive() {
            super.onActive()
            if (!started.compareAndSet(false, true)) return
            call.enqueue(object : Callback<R> {
                override fun onResponse(call: Call<R>, response: Response<R>) = postValue(
                    ApiResponse(
                        response
                    )
                )
                override fun onFailure(call: Call<R>, throwable: Throwable) = postValue(
                    ApiResponse(
                        throwable
                    )
                )
            })
        }
    }
}

class LiveDataCallAdapterFactory : CallAdapter.Factory() {

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): LiveDataCallAdapter<*>? {
        if (CallAdapter.Factory.getRawType(returnType) != LiveData::class.java) {
            return null
        }
        val observableType = CallAdapter.Factory.getParameterUpperBound(0, returnType as ParameterizedType)
        val rawObservableType = CallAdapter.Factory.getRawType(observableType)
        if (rawObservableType != ApiResponse::class.java) {
            throw IllegalArgumentException("type must be a resource")
        }
        if (observableType !is ParameterizedType) {
            throw IllegalArgumentException("resource must be parameterized")
        }
        val bodyType = CallAdapter.Factory.getParameterUpperBound(0, observableType)
        return LiveDataCallAdapter<Type>(bodyType)
    }
}