package kz.brotandos.trailerman.actors.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kz.brotandos.trailerman.actors.Actor

@Dao
interface ActorDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertActor(actors: List<Actor>)

  @Update
  fun updateActor(actor: Actor)

  @Query("SELECT * FROM Actor WHERE id = :id_")
  fun getActor(id_: Int): Actor

  @Query("SELECT * FROM Actor WHERE page = :page_")
  fun getActors(page_: Int): LiveData<List<Actor>>
}