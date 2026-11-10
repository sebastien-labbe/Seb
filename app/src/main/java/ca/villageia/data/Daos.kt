package ca.villageia.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BuildingDao {
    @Query("SELECT * FROM building ORDER BY slotIndex")
    fun observeAll(): Flow<List<Building>>

    @Query("SELECT * FROM building WHERE id = :id")
    fun observe(id: Long): Flow<Building?>

    @Query("SELECT COUNT(*) FROM building")
    suspend fun count(): Int

    @Insert
    suspend fun insert(building: Building): Long

    @Query("DELETE FROM building WHERE id = :id")
    suspend fun delete(id: Long)
}

@Dao
interface PersonDao {
    @Query("SELECT * FROM person WHERE buildingId = :buildingId ORDER BY id")
    fun observeByBuilding(buildingId: Long): Flow<List<Person>>

    @Query("SELECT * FROM person WHERE id = :id")
    fun observe(id: Long): Flow<Person?>

    @Insert
    suspend fun insert(person: Person): Long

    @Update
    suspend fun update(person: Person)

    @Query("DELETE FROM person WHERE id = :id")
    suspend fun delete(id: Long)
}
