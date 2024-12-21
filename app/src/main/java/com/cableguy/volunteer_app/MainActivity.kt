package com.cableguy.volunteer_app

import TravellerScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cableguy.volunteer_app.ui.theme.VolunteerAppTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CardTravel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.cableguy.volunteer_app.nav.screens.TripScreen
import com.cableguy.volunteer_app.room.LocalDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database by lazy { LocalDatabase.getDatabase(this) }

        setContent {
            VolunteerAppTheme {
                MainLayout(database)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(database: LocalDatabase, modifier: Modifier = Modifier) {

    var currentScreen by remember { mutableStateOf("home") }
    var showCreate by remember { mutableStateOf(false) }
    var pageTitle by remember { mutableStateOf("Travel Ledger") }

    Scaffold(
        topBar = {
//            CenterAlignedTopAppBar(title = { Text(text = pageTitle) })
            TopAppBar(
                title = { Text(text = pageTitle) },
                navigationIcon = {
                    if (currentScreen != "home") {
                        IconButton(onClick = {
                            currentScreen = "home"
                            pageTitle = "Travel Ledger"
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { currentScreen = "traveller" }) {
                        Icon(Icons.Filled.Person, contentDescription = "Traveller Page")
                    }
                    IconButton(onClick = { currentScreen = "trip" }) {
                        Icon(Icons.Filled.CardTravel, contentDescription = "Trip")
                    }
                    IconButton(onClick = { currentScreen = "report" }) {
                        Icon(Icons.Filled.QueryStats, contentDescription = "Report")
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { showCreate = !showCreate },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        if (showCreate) {
                            Icon(Icons.AutoMirrored.Filled.List, "Add To Ledger")
                        } else {
                            Icon(Icons.Filled.Add, "Add To Ledger")
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
         if (currentScreen == "traveller") {
             pageTitle = "Travellers"
             TravellerScreen(
                 database = database,
                 modifier = Modifier.padding(innerPadding)
             )
         } else if (currentScreen == "trip") {
             pageTitle = "Trips"
             TripScreen(
                 database = database,
                 modifier = Modifier.padding(innerPadding)
             )
         } else {
             Home(
                 modifier = Modifier.padding(innerPadding)
             )
         }
    }
}

@Composable
fun Home( modifier: Modifier = Modifier) {
    Text(
        text = "Hello",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VolunteerAppTheme {
        Home()
    }
}