package com.cableguy.volunteer_app.room.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cableguy.volunteer_app.room.dao.ExpenseDao
import com.cableguy.volunteer_app.room.dao.TripDao
import com.cableguy.volunteer_app.room.viewmodel.ExpenseViewModel
import com.cableguy.volunteer_app.room.viewmodel.TravellerViewModel
import com.cableguy.volunteer_app.room.viewmodel.TripViewModel

class ExpenseViewModelFactory(private val ExpenseDao: ExpenseDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(ExpenseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}