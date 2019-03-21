package kz.brotandos.trailerman.actors

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kz.brotandos.trailerman.actors.actor.ActorDetails

@Parcelize
@Entity(primaryKeys = ["id"])
data class Actor(
    val id: Int,
    val name: String,
    var page: Int,
    @Embedded var actorDetails: ActorDetails? = null,
    val popularity: Float,
    @SerializedName("profile_path")
    val profileUrl: String?,
    @SerializedName("adult")
    val isAdult: Boolean
) : Parcelable