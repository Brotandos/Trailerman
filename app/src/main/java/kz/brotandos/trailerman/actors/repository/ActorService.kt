package kz.brotandos.trailerman.actors.repository

import androidx.lifecycle.LiveData
import kz.brotandos.trailerman.actors.actor.ActorDetails
import kz.brotandos.trailerman.common.api.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ActorService {
    @GET("/3/person/popular?language=en")
    fun fetchActors(@Query("page") page: Int): LiveData<ApiResponse<ActorsResponse>>

    @GET("/3/person/{person_id}")
    fun fetchActorDetails(@Path("person_id") id: Int): LiveData<ApiResponse<ActorDetails>>
}