package com.cableguy.volunteer_app.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.cableguy.volunteer_app.room.entity.Expense
import com.cableguy.volunteer_app.room.entity.ExpenseTravellerCrossRef
import com.cableguy.volunteer_app.room.entity.relations.ExpenseWithTravellers
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenseTravellerCrossRefs(crossRefs: List<ExpenseTravellerCrossRef>)

    @Transaction
    @Query("SELECT * FROM expenses WHERE tripId = :tripId")
    fun getExpensesWithTravellersForTrip(tripId: Int): Flow<List<ExpenseWithTravellers>>
}