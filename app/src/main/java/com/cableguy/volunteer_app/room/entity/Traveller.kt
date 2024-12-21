package com.cableguy.volunteer_app.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "travellers")
data class Traveller (
    @PrimaryKey(autoGenerate = true) val travellerId: Int = 0,
    val name: String,
    val phoneNo: String,
    val email: String,
    val gender: String,
    val age: Int,
    val adhaarCardNo: String
)
