package com.cableguy.volunteer_app.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val expenseId: Int = 0,
    val tripId: Int, // Foreign Key
    val amount: Double,
    val description: String,
    val date: Long
)
