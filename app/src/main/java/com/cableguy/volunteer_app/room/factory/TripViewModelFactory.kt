package com.cableguy.volunteer_app.room.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cableguy.volunteer_app.room.dao.TripDao
import com.cableguy.volunteer_app.room.viewmodel.TravellerViewModel
import com.cableguy.volunteer_app.room.viewmodel.TripViewModel

class TripViewModelFactory(private val TripDao: TripDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TripViewModel(TripDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}