package com.cableguy.volunteer_app.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = true) val tripId: Int = 0,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val description: String
)
