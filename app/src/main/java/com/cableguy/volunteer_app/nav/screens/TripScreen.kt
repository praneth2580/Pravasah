package com.cableguy.volunteer_app.nav.screens

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import com.cableguy.volunteer_app.room.entity.Traveller
import com.cableguy.volunteer_app.room.viewmodel.TripViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cableguy.volunteer_app.room.LocalDatabase
import com.cableguy.volunteer_app.room.entity.Trip
import com.cableguy.volunteer_app.room.entity.relations.TripWithTravellers
import com.cableguy.volunteer_app.room.factory.TravellerViewModelFactory
import com.cableguy.volunteer_app.room.factory.TripViewModelFactory
import com.cableguy.volunteer_app.room.viewmodel.TravellerViewModel

@Composable
fun TripScreen(
    database: LocalDatabase,
    modifier: Modifier = Modifier
) {

    val travellerDao = remember { database.travellerDao() }
    val travelerViewModel: TravellerViewModel = viewModel(factory = TravellerViewModelFactory(travellerDao))
    val travellers by travelerViewModel.allTravellers.collectAsState(initial = emptyList())

    val tripDao = remember { database.tripDao() }
    val viewModel: TripViewModel = viewModel(factory = TripViewModelFactory(tripDao))
    val trips by viewModel.allTrips.collectAsState(initial = emptyList())

    var showAddTripForm by remember { mutableStateOf(false) }
    var showTravellerMapping by remember { mutableStateOf(false) }
    var showTripInDetail by remember { mutableStateOf(false) }
    var selectedTrip by remember { mutableStateOf<Trip?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        if (showAddTripForm && !showTravellerMapping) {
            TripForm(
                onTripAdded = {
                    showAddTripForm = false
                    viewModel.addTrip(it)
                }
            )
        } else if (!showAddTripForm && showTravellerMapping) {
            MapTravellerToTrip(
                travellers = travellers,
                trips = trips,
                onMap = { travellerId, tripId ->
                    viewModel.addTravellerToTrip(travellerId, tripId)
                    showTravellerMapping = false
                }
            )
        } else {
            Button(
                onClick = { showAddTripForm = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Trips")
            }

            LazyColumn {
                items(trips) { trip ->
                    TripItem(
                        trip = trip,
                        onClick = {
                            showTripInDetail = true
                            selectedTrip = trip
                        },
                        onDeleteTrip = {

                        },
                        onAddTraveller = {
                            showTravellerMapping = true
                            selectedTrip = trip
                        }
                    )
                }
            }

            if (trips.isEmpty()) {
                Text(
                    text = "No trips found. Add one using the + button above!",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

    // Traveller Mapping Dialog
    if (showTripInDetail && selectedTrip != null) {


        val selectedTripDetails by viewModel.tripWithTravellers.observeAsState()

        // Load the data when this composable is shown
        LaunchedEffect(selectedTrip!!.tripId) {
            viewModel.getTripWithTravellersByTripID(selectedTrip!!.tripId)
        }

        AlertDialog(
            onDismissRequest = { showTripInDetail = false },
            title = { Text("Trip Details") },
            text = {
                selectedTrip?.tripId?.let { data ->
                    TripDetails(
                        tripWithTraveller = selectedTripDetails,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                } ?: run {
                    Text("Loading...", style = MaterialTheme.typography.bodyLarge)
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }
}

@Composable
fun TripItem(
    trip: Trip,
    onClick: () -> Unit,
    onDeleteTrip: () -> Unit,
    onAddTraveller: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Destination: ${trip.destination}", style = MaterialTheme.typography.bodyLarge)
            Text("Dates: ${trip.startDate} - ${trip.endDate}", style = MaterialTheme.typography.bodyMedium)
            Text("Description: ${trip.description}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Button(onClick = onAddTraveller) {
                    Text("Add Traveller")
                }
                Button(onClick = onDeleteTrip) {
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
fun TripForm(onTripAdded: (Trip) -> Unit) {
    var destination by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = destination,
            onValueChange = { destination = it },
            label = { Text("Destination") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = startDate,
            onValueChange = { startDate = it },
            label = { Text("Start Date") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = endDate,
            onValueChange = { endDate = it },
            label = { Text("End Date") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (destination.isNotEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty()) {
                    onTripAdded(
                        Trip(
                            destination = destination,
                            startDate = startDate,
                            endDate = endDate,
                            description = description
                        )
                    )
                }
            }
        ) {
            Text("Add Trip")
        }
    }
}

@Composable
fun MapTravellerToTrip(
    travellers: List<Traveller>,
    trips: List<Trip>,
    onMap: (Int, Int) -> Unit
) {
    var selectedTraveller by remember { mutableStateOf<Traveller?>(null) }
    var isTravellerDropdownExpanded by remember { mutableStateOf(false) }

    var selectedTrip by remember { mutableStateOf<Trip?>(null) }
    var isTripDropdownExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        // Traveller Dropdown
        Text("Select Traveller", style = MaterialTheme.typography.bodyLarge)
        Box {
            // Dropdown Trigger
            OutlinedTextField(
                value = selectedTraveller?.name ?: "Select a traveller",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth().clickable { isTravellerDropdownExpanded = true },
                enabled = false, // Disable direct text input
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
            )

            DropdownMenu(
                expanded = isTravellerDropdownExpanded,
                onDismissRequest = { isTravellerDropdownExpanded = false }
            ) {
                travellers.forEach { traveller ->
                    DropdownMenuItem(
                        onClick = {
                            selectedTraveller = traveller
                            isTravellerDropdownExpanded = false
                        },
                        text = {
                            Text(traveller.name)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Trip Dropdown
        Text("Select Trip", style = MaterialTheme.typography.bodyLarge)
        Box {
            // Dropdown Trigger
            OutlinedTextField(
                value = selectedTrip?.destination ?: "Select a trip",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth().clickable { isTripDropdownExpanded = true },
                enabled = false, // Disable direct text input
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
            )

            DropdownMenu(
                expanded = isTripDropdownExpanded,
                onDismissRequest = { isTripDropdownExpanded = false }
            ) {
                trips.forEach { trip ->
                    DropdownMenuItem(
                        onClick = {
                            selectedTrip = trip
                            isTripDropdownExpanded = false
                        },
                        text = {
                            Text(trip.destination)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Map Button
        Button(
            onClick = {
                selectedTraveller?.let { traveller ->
                    selectedTrip?.let { trip ->
                        onMap(traveller.travellerId, trip.tripId)
                    }
                }
            },
            enabled = selectedTraveller != null && selectedTrip != null // Enable only if both are selected
        ) {
            Text("Map Traveller to Trip")
        }
    }
}

@Composable
fun TripDetails(tripWithTraveller: TripWithTravellers?, modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
    ) {
        Text(text = "Destination: ${tripWithTraveller?.trip?.destination}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Start Date: ${tripWithTraveller?.trip?.startDate}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "End Date: ${tripWithTraveller?.trip?.endDate}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Description: ${tripWithTraveller?.trip?.description}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(10.dp))

        if (tripWithTraveller != null) {
            if (tripWithTraveller.travellers != null) {
                Text(text = "Travellers on Trip", style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(value = 18f, TextUnitType.Sp)
                ))
                Spacer(modifier = Modifier.height(4.dp))
                LazyColumn {
                    items(tripWithTraveller!!.travellers) { traveller ->
                        Text(text = traveller.name)
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTripsScreen() {
    val mockTravellers = listOf(
        Traveller(1, "John Doe", "1234567890", "john@example.com", "Male", 30, "1234-5678-9012"),
        Traveller(2, "Jane Smith", "9876543210", "jane@example.com", "Female", 28, "5678-1234-9012")
    )

//    val mockViewModel = TripViewModel(/* Pass your DAO or repository */)
//    TripsScreen(travellers = mockTravellers, viewModel = mockViewModel)
}

