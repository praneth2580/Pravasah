package com.cableguy.volunteer_app.nav.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.cableguy.volunteer_app.room.entity.Traveller
import com.cableguy.volunteer_app.room.viewmodel.ExpenseViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cableguy.volunteer_app.room.LocalDatabase
import com.cableguy.volunteer_app.room.entity.Expense
import com.cableguy.volunteer_app.room.factory.ExpenseViewModelFactory
import com.cableguy.volunteer_app.room.factory.TravellerViewModelFactory
import com.cableguy.volunteer_app.room.viewmodel.TravellerViewModel

@Composable
fun ExpenseScreen(database: LocalDatabase, modifier: Modifier = Modifier) {
    val expenseDao = remember { database.expenseDao() }
    val viewModel: ExpenseViewModel = viewModel(factory = ExpenseViewModelFactory(expenseDao))

    val expenses by viewModel.allExpenses.collectAsState(initial = emptyList())


    LazyColumn (
        modifier = modifier
    ) {
        items(expenses) { expense ->
            ExpenseItem(
                expense = expense,
                onDelete = {

                },
                modifier = Modifier
            )
        }
    }


}

@Composable
fun ExpenseItem(expense: Expense, onDelete: () -> Unit, modifier: Modifier = Modifier) {

    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row {
            Column {
                Text(text = expense.amount.toString())
                Text(text = expense.description)
            }
            Icon(
                Icons.Filled.Delete,
                "Delete Expense",
                modifier = Modifier
                    .clickable {

                    }
            )
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
