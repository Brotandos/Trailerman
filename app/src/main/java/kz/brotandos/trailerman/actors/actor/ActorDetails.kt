package kz.brotandos.trailerman.actors.actor

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kz.brotandos.trailerman.common.models.NetworkResponseModel

@Parcelize
data class ActorDetails(
    val birthday: String,
    val biography: String,
    @SerializedName("known_for_department")
    val knownForDepartment: String,
    @SerializedName("place_of_birth")
    val placeOfBirth: String,
    @SerializedName("also_known_as")
    val alsoKnownAs: List<String>
) : Parcelable, NetworkResponseModel
