package com.cableguy.volunteer_app.room.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cableguy.volunteer_app.room.entity.Traveller
import com.cableguy.volunteer_app.room.entity.TravellerTripCrossRef
import com.cableguy.volunteer_app.room.entity.Trip

data class TripWithTravellers(
    @Embedded val trip: Trip,
    @Relation(
        parentColumn = "tripId",         // Primary key in Trip table
        entityColumn = "travellerId",    // Foreign key in the cross-reference table
        associateBy = Junction(TravellerTripCrossRef::class) // Define the Junction table
    )
    val travellers: List<Traveller>
)
