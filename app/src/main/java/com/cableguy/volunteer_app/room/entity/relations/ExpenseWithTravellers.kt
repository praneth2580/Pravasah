package com.cableguy.volunteer_app.room.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cableguy.volunteer_app.room.entity.Expense
import com.cableguy.volunteer_app.room.entity.ExpenseTravellerCrossRef
import com.cableguy.volunteer_app.room.entity.Traveller

data class ExpenseWithTravellers(
    @Embedded val expense: Expense,
    @Relation(
        parentColumn = "expenseId",
        entityColumn = "travellerId",
        associateBy = Junction(ExpenseTravellerCrossRef::class)
    )
    val travellers: List<Traveller>
)
