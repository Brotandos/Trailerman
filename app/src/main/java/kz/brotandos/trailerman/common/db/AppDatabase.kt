package kz.brotandos.trailerman.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kz.brotandos.trailerman.actors.repository.ActorDao
import kz.brotandos.trailerman.movies.Movie
import kz.brotandos.trailerman.actors.Actor
import kz.brotandos.trailerman.series.Series
import kz.brotandos.trailerman.movies.repository.MovieDao
import kz.brotandos.trailerman.series.repository.SeriesDao

@Database(
    entities = [Movie::class, Series::class, Actor::class],
    version = 1, exportSchema = false
)
@TypeConverters(
    value = [
        StringListConverter::class,
        IntegerListConverter::class,
        KeywordListConverter::class,
        VideoListConverter::class,
        ReviewListConverter::class
    ])
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun seriesDao(): SeriesDao
    abstract fun actorDao(): ActorDao
}