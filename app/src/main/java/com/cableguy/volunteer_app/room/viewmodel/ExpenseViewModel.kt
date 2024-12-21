package com.cableguy.volunteer_app.room.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cableguy.volunteer_app.room.dao.ExpenseDao
import com.cableguy.volunteer_app.room.entity.Expense
import com.cableguy.volunteer_app.room.entity.ExpenseTravellerCrossRef
import com.cableguy.volunteer_app.room.entity.Traveller
import kotlinx.coroutines.launch

class ExpenseViewModel(private val expenseDao: ExpenseDao) : ViewModel() {

    fun addExpense(tripId: Int, description: String, amount: Double, selectedTravellers: List<Traveller>) {
        viewModelScope.launch {
            val expenseId = expenseDao.insertExpense(
                Expense(
                    tripId = tripId,
                    amount = amount,
                    description = description,
                    date = System.currentTimeMillis()
                )
            )

            val crossRefs = selectedTravellers.map { traveller ->
                ExpenseTravellerCrossRef(
                    expenseId = expenseId.toInt(),
                    travellerId = traveller.travellerId
                )
            }

            expenseDao.insertExpenseTravellerCrossRefs(crossRefs)
        }
    }
}