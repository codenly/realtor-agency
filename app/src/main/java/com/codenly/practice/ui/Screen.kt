package com.codenly.practice.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.codenly.practice.data.local.OwnerEntity
import com.codenly.practice.viewmodel.OwnerEditViewModel
import com.codenly.practice.viewmodel.OwnerListViewModel
import org.intellij.lang.annotations.JdkConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToOwners: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Главная") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onNavigateToOwners, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Владельцы")
            }
//            Button(onNavigateToTypes, modifier = Modifier.fillMaxWidth()) {
//                Text("Типы недвижимости")
//            }
//            Button(onNavigateToClients, modifier = Modifier.fillMaxWidth()) {
//                Text("Клиенты")
//            }
//            Button(onNavigateToRealtors, modifier = Modifier.fillMaxWidth()) {
//                Text("Риелторы")
//            }
//            Button(onNavigateToProperties, modifier = Modifier.fillMaxWidth()) {
//                Text("Объекты недвижимости")
//            }
//            Button(onNavigateToViews, modifier = Modifier.fillMaxWidth()) {
//                Text("Просмотры")
//            }
//            Button(onNavigateToDeals, modifier = Modifier.fillMaxWidth()) {
//                Text("Сделки")
//            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerListScreen(
    onNavigateToEdit: (Long?) -> Unit,
    ownerViewModel: OwnerListViewModel = viewModel()
) {
    val uiState by ownerViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            ownerViewModel.clearError()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Владельцы") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToEdit(null) }) {
                Text("+")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                uiState.owners.isEmpty() -> {
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
                        items(uiState.owners, key = { it.id }) { owner ->
                            OwnerItem(
                                owner = owner,
                                onEdit = { onNavigateToEdit(owner.id) },
                                onDelete = { ownerViewModel.deleteOwner(owner) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OwnerItem(
    owner: OwnerEntity,
    onEdit: () ->Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize()
        .padding(4.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = owner.fullName, style = MaterialTheme.typography.bodyMedium)
                Text(text = owner.phoneNumber,style = MaterialTheme.typography.bodyMedium)
                Text(text = owner.email,style = MaterialTheme.typography.bodyMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onEdit) { Text("Изменить") }
                    TextButton(onClick = onDelete) { Text("Удалить") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerEditScreen(
    navController: NavController,
    ownerEditViewModel: OwnerEditViewModel = viewModel()
) {
    val uiState by ownerEditViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (uiState.isNew) "Новый владелец" else "Редактирование") })
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = uiState.fullName,
                onValueChange = ownerEditViewModel::onFullNameChange,
                label = { Text("ФИО") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.phoneNumber,
                onValueChange = ownerEditViewModel::onPhoneNumberChange,
                label = { Text("Номер телефона") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.email,
                onValueChange = ownerEditViewModel::onEmailChange,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.errorMessage != null) {
                Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
            }
            Button(
                onClick = ownerEditViewModel::save,
                enabled = !uiState.isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isSaving) CircularProgressIndicator(modifier = Modifier.size(20.dp))
                else Text("Сохранить")
            }
        }
    }
}