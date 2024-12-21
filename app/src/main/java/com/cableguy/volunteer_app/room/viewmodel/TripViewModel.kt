package com.cableguy.volunteer_app.room.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cableguy.volunteer_app.room.dao.TripDao
import com.cableguy.volunteer_app.room.entity.TravellerTripCrossRef
import com.cableguy.volunteer_app.room.entity.Trip
import com.cableguy.volunteer_app.room.entity.relations.TripWithTravellers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TripViewModel(private val dao: TripDao): ViewModel() {

    private val _tripWithTravellers = MutableLiveData<TripWithTravellers?>()
    val allTrips: Flow<List<Trip>> = dao.getAll()
    val tripWithTravellers: LiveData<TripWithTravellers?> get() = _tripWithTravellers

    val allTripsWithTravellers: LiveData<List<TripWithTravellers>> = dao
        .getAllTripsWithTravellers()
        .asLiveData()

    fun addTrip(trip: Trip) {
        viewModelScope.launch {
            dao.insertTrip(trip)
        }
    }

    fun addTravellerToTrip(travellerId: Int, tripId: Int) {
        viewModelScope.launch {
            dao.insertTravellerTripCrossRef(TravellerTripCrossRef(travellerId, tripId))
        }
    }

//    fun getTripsForTraveller(travellerId: Int): LiveData<List<TravellerWithTrips>> {
//        return liveData {
//            emit(dao.getTripsForTraveller(travellerId))
//        }
//    }
//
    fun getTripWithTravellersByTripID(tripId: Int) {
        viewModelScope.launch {
            _tripWithTravellers.value = dao.getTripWithTravellers(tripId)
        }
    }

}