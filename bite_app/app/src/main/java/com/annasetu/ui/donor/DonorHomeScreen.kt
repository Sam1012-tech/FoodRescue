// bite_app/app/src/main/java/com/annasetu/ui/donor/DonorHomeScreen.kt
package com.annasetu.ui.donor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.annasetu.data.model.Donation
import com.annasetu.viewmodel.DonorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorHomeScreen(onNavigateToPost: () -> Unit) {
    val viewModel: DonorViewModel = viewModel()
    val donations by viewModel.donations.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchDonations()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Your Donations") }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToPost,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("Post Food") }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            items(donations) { donation ->
                DonationCard(donation)
            }
        }
    }
}

@Composable
fun DonationCard(donation: Donation) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Status: ${donation.status}", style = MaterialTheme.typography.titleMedium)
            Text("Created: ${donation.createdAt.toDate()}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
