package kz.brotandos.trailerman.series

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kz.brotandos.trailerman.common.models.Keyword
import kz.brotandos.trailerman.common.models.Review
import kz.brotandos.trailerman.common.models.Video

@Parcelize
@Entity(primaryKeys = ["id"])
data class Series(
    val id: Int,
    val name: String,
    var page: Int,
    var keywords: List<Keyword>? = ArrayList(),
    var videos: List<Video>? = ArrayList(),
    var reviews: List<Review>? = ArrayList(),
    val popularity: Float,
    val overview: String,
    @SerializedName("backdrop_path")
    val backdropUrl: String?,
    @SerializedName("original_name")
    val originalName: String,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("first_air_date")
    val firstAirDate: String,
    @SerializedName("origin_country")
    val originCountry: List<String>,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    @SerializedName("poster_path")
    val posterUrl: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("vote_count")
    val voteCount: Int
) : Parcelable