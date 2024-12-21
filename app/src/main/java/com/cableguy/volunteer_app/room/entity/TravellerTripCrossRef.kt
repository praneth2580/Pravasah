package com.cableguy.volunteer_app.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "traveller_trip_cross_ref",
    primaryKeys = ["travellerId", "tripId"],
    foreignKeys = [
        ForeignKey(entity = Traveller::class, parentColumns = ["travellerId"], childColumns = ["travellerId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Trip::class, parentColumns = ["tripId"], childColumns = ["tripId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [
        Index(value = ["travellerId"]),
        Index(value = ["tripId"])
    ]
)
data class TravellerTripCrossRef (
    val travellerId: Int,
    val tripId: Int
)
