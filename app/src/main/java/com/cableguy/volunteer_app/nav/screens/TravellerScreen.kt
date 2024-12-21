import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cableguy.volunteer_app.room.LocalDatabase
import com.cableguy.volunteer_app.room.factory.TravellerViewModelFactory
import com.cableguy.volunteer_app.room.entity.Traveller
import com.cableguy.volunteer_app.room.viewmodel.TravellerViewModel

@Composable
fun TravellerScreen(database: LocalDatabase, modifier: Modifier = Modifier) {
    val travellerDao = remember { database.travellerDao() }
    val viewModel: TravellerViewModel = viewModel(factory = TravellerViewModelFactory(travellerDao))

    var showInputs by remember { mutableStateOf(false) }
    val travellers by viewModel.allTravellers.collectAsState(initial = emptyList())
    var travellerToEdit by remember { mutableStateOf<Traveller?>(null) }

    Column(modifier = modifier) {
        if (showInputs) {
            TravellerCreate(
                viewModel = viewModel,
                onClose = {
                    showInputs = false
                    travellerToEdit = null
                },
                travellerToEdit = travellerToEdit,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            TravellerList(
                travellers = travellers,
                onEdit = {
                    travellerToEdit = it
                    showInputs = true
                },
                onDelete = { viewModel.deleteTraveller(it) },
                onOpenCreate = { showInputs = true },
                modifier = Modifier.padding(
                    horizontal = 2.dp
                )
            )
        }
    }
}

@Composable
fun TravellerCreate(viewModel: TravellerViewModel, travellerToEdit: Traveller?, onClose: () -> Unit,modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf(travellerToEdit?.name ?: "") }
    var phoneNo by remember { mutableStateOf(travellerToEdit?.phoneNo ?: "") }
    var email by remember { mutableStateOf(travellerToEdit?.email ?: "") }
    var gender by remember { mutableStateOf(travellerToEdit?.gender ?: "") }
    var age by remember { mutableStateOf((travellerToEdit?.age ?: "").toString()) }
    var adhaarCardNo by remember { mutableStateOf(travellerToEdit?.adhaarCardNo ?: "") }

    Column(modifier = modifier.fillMaxWidth()) {
        // Input fields for adding a Traveller
//        Text(text = "Add Traveller", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = name, onValueChange = { name = it }, label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = phoneNo, onValueChange = { phoneNo = it }, label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = email, onValueChange = { email = it }, label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = gender, onValueChange = { gender = it }, label = { Text("Gender") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = age, onValueChange = { age = it }, label = { Text("Age") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = adhaarCardNo, onValueChange = { adhaarCardNo = it }, label = { Text("Aadhaar Card Number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { onClose() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth(fraction = .5f)
            ) {
                Text("Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (name.isNotEmpty() && phoneNo.isNotEmpty() && email.isNotEmpty() &&
                        gender.isNotEmpty() && age.isNotEmpty() && adhaarCardNo.isNotEmpty()
                    ) {
                        if (travellerToEdit == null) {
                            viewModel.addTraveller(
                                Traveller(
                                    name = name,
                                    phoneNo = phoneNo,
                                    email = email,
                                    gender = gender,
                                    age = age.toInt(),
                                    adhaarCardNo = adhaarCardNo
                                )
                            )
                        } else {
                            viewModel.updateTraveller(
                                travellerToEdit.copy(
                                    name = name,
                                    phoneNo = phoneNo,
                                    email = email,
                                    gender = gender,
                                    age = age.toInt(),
                                    adhaarCardNo = adhaarCardNo
                                )
                            )
                        }
                        // Clear input fields
                        name = ""; phoneNo = ""; email = ""; gender = ""; age = ""; adhaarCardNo = ""
                        onClose()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (travellerToEdit == null) {
                    Text("Add Traveller")
                } else {
                    Text("Update Traveller")
                }
            }
        }
    }
}

@Composable
fun TravellerList(travellers: List<Traveller>, onEdit: (Traveller) -> Unit, onDelete: (Traveller) -> Unit, onOpenCreate: () -> Unit, modifier: Modifier = Modifier) {
    // Display list of Travellers
//    Text(text = "Travellers List", style = MaterialTheme.typography.titleLarge)/
    Button(
        onClick = { onOpenCreate() },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Add Traveller")
    }
    LazyColumn (
        modifier = modifier
    ) {
        items(travellers) { traveller ->
            TravellerItem(traveller, onEdit = { onEdit(traveller) }, onDelete = { onDelete(traveller) })
        }
    }
}

@Composable
fun TravellerItem(
    traveller: Traveller,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Traveller Information
            Text(text = "Name: ${traveller.name}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Phone: ${traveller.phoneNo}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Email: ${traveller.email}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Gender: ${traveller.gender}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Age: ${traveller.age}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Aadhaar: ${traveller.adhaarCardNo}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(12.dp))

            // Actions Row (Edit and Delete buttons)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Edit Button
                Button(
                    onClick = onEdit,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Edit")
                }

                // Delete Button
                Button(
                    onClick = { showDeleteConfirmation = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            }
        }
    }

    // Confirmation Dialog
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this traveller? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmation = false
                        onDelete() // Call the delete action
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTravellerItem() {
    TravellerItem(
        traveller = Traveller(
            travellerId = 1,
            name = "John Doe",
            phoneNo = "1234567890",
            email = "johndoe@example.com",
            gender = "Male",
            age = 30,
            adhaarCardNo = "1234-5678-9012"
        ),
        onEdit = { /* Handle Edit */ },
        onDelete = { /* Handle Delete */ }
    )
}

