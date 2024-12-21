package com.cableguy.volunteer_app.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.cableguy.volunteer_app.room.entity.TravellerTripCrossRef
import com.cableguy.volunteer_app.room.entity.Trip
import com.cableguy.volunteer_app.room.entity.relations.TripWithTravellers
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {

    // Insert new Trip
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: Trip): Long

    // Insert a cross-reference (map traveller to trip)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTravellerTripCrossRef(crossRef: TravellerTripCrossRef)

    // Delete a trip
    @Delete
    suspend fun deleteTrip(trip: Trip)

    // Get all trips
    @Transaction
    @Query("SELECT * FROM trips order by tripId desc")
    fun getAll(): Flow<List<Trip>>

    // get all traveller for all trips
    @Transaction
    @Query("SELECT * FROM trips")
    fun getAllTripsWithTravellers(): Flow<List<TripWithTravellers>>

    // Get all travellers for a trip
    @Transaction
    @Query("SELECT * FROM trips WHERE tripId = :tripId")
    suspend fun getTripWithTravellers(tripId: Int): TripWithTravellers
}