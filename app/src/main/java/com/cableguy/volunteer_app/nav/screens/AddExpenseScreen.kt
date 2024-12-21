package com.cableguy.volunteer_app.nav.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.cableguy.volunteer_app.room.entity.Traveller
import com.cableguy.volunteer_app.room.viewmodel.ExpenseViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun AddExpenseScreen(
    tripId: Int,
    travellers: List<Traveller>,
    onExpenseAdded: () -> Unit,
    viewModel: ExpenseViewModel
) {
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val selectedTravellers = remember { mutableStateListOf<Traveller>() }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Add Expense", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Select Travellers", style = MaterialTheme.typography.headlineMedium)

        LazyColumn(modifier = Modifier.fillMaxHeight(0.5f)) {
            items(travellers) { traveller ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = selectedTravellers.contains(traveller),
                        onCheckedChange = {
                            if (it) {
                                selectedTravellers.add(traveller)
                            } else {
                                selectedTravellers.remove(traveller)
                            }
                        }
                    )
                    Text(traveller.name, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (description.isNotBlank() && amount.toDoubleOrNull() != null) {
                    viewModel.addExpense(
                        tripId = tripId,
                        description = description,
                        amount = amount.toDouble(),
                        selectedTravellers = selectedTravellers
                    )
                    onExpenseAdded()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Expense")
        }
    }
}
