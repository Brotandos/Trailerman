package kz.brotandos.trailerman.series.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import kz.brotandos.trailerman.series.Series


@Dao
interface SeriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSeries(series: List<Series>)

    @Update
    fun updateSeries(series: Series)

    @Query("SELECT * FROM Series WHERE id = :id_")
    fun getSeries(id_: Int): Series

    @Query("SELECT * FROM Series WHERE page = :page_")
    fun getSeriesList(page_: Int): LiveData<List<Series>>
}
