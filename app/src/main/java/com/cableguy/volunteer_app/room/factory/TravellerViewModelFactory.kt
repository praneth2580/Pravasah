package com.cableguy.volunteer_app.room.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cableguy.volunteer_app.room.dao.TravellerDao
import com.cableguy.volunteer_app.room.viewmodel.TravellerViewModel

class TravellerViewModelFactory(private val travellerDao: TravellerDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TravellerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TravellerViewModel(travellerDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}