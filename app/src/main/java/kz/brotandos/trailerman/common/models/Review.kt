package kz.brotandos.trailerman.common.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Review(
    val id: String,
    val author: String,
    val content: String,
    val url: String
) : Parcelable