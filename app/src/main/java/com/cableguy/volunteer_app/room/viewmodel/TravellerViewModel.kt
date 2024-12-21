package com.cableguy.volunteer_app.room.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cableguy.volunteer_app.room.dao.TravellerDao
import com.cableguy.volunteer_app.room.entity.Traveller
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TravellerViewModel(private val travellerDao: TravellerDao): ViewModel() {

    val allTravellers: Flow<List<Traveller>> = travellerDao.getAll()

    fun addTraveller(traveller: Traveller) {
        viewModelScope.launch {
            travellerDao.insertTraveller(traveller)
        }
    }

    fun updateTraveller(traveller: Traveller) {
        viewModelScope.launch {
            travellerDao.updateTraveller(traveller)
        }
    }

    fun deleteTraveller(traveller: Traveller) {
        viewModelScope.launch {
            travellerDao.deleteTraveller(traveller)
        }
    }

}