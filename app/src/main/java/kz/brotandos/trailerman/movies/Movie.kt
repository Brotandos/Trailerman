package kz.brotandos.trailerman.movies

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kz.brotandos.trailerman.common.models.Keyword
import kz.brotandos.trailerman.common.models.Review
import kz.brotandos.trailerman.common.models.Video

@Parcelize
@Entity(primaryKeys = ["id"])
data class Movie(
    val id: Int,
    val title: String,
    var page: Int,
    var keywords: List<Keyword>? = ArrayList(),
    var videos: List<Video>? = ArrayList(),
    var reviews: List<Review>? = ArrayList(),
    val overview: String,
    val popularity: Float,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("adult")
    val isAdult: Boolean,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("poster_path")
    val posterUrl: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("backdrop_path")
    val backdropUrl: String?,
    @SerializedName("video")
    val isVideo: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Float
) : Parcelable
