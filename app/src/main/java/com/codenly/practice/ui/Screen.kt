package com.codenly.practice.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codenly.practice.data.local.OwnerEntity
import com.codenly.practice.viewmodel.OwnerViewModel

@Preview
@Composable
fun OwnerScreen(
    ownerViewModel: OwnerViewModel = viewModel()
) {
    val uiState by ownerViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { ownerViewModel.addTestOwner() }) {
                Text("+")
            }
        },
        snackbarHost = {

        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage ?: "Неизвестная ошибка",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.owner.isEmpty() -> {
                    Text(
                        text = "Нет ни одного владельца",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.owner) { owner ->
                            OwnerItem(owner = owner)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OwnerItem(owner: OwnerEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = owner.fullName,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = owner.phoneNumber,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = owner.email,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}