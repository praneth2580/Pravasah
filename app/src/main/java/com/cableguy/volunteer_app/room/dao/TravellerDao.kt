package com.cableguy.volunteer_app.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cableguy.volunteer_app.room.entity.Traveller
import kotlinx.coroutines.flow.Flow

@Dao
interface TravellerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTraveller(traveller: Traveller)

    @Update
    suspend fun updateTraveller(traveller: Traveller)

    @Delete
    suspend fun deleteTraveller(traveller: Traveller)

    @Query("select * from travellers order by travellerId desc")
    fun getAll(): Flow<List<Traveller>>


}