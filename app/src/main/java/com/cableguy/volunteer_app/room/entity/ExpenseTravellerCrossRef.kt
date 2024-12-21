package com.cableguy.volunteer_app.room.entity

import androidx.room.Entity

@Entity(tableName = "expense_traveller_cross_ref")
data class ExpenseTravellerCrossRef(
    val expenseId: Int, // Foreign Key
    val travellerId: Int // Foreign Key
)
