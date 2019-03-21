package kz.brotandos.trailerman.common.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kz.brotandos.trailerman.common.models.Review


open class ReviewListConverter {
    @TypeConverter
    fun fromString(value: String): List<Review>? {
        val listType = object : TypeToken<List<Review>>() {}.type
        return Gson().fromJson<List<Review>>(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Review>?): String = Gson().toJson(list)
}
