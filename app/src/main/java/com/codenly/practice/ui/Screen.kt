package com.codenly.practice.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.codenly.practice.data.local.OwnerEntity
import com.codenly.practice.viewmodel.OwnerEditViewModel
import com.codenly.practice.viewmodel.OwnerListViewModel
import com.codenly.practice.data.local.TypeEntity
import com.codenly.practice.viewmodel.TypeEditViewModel
import com.codenly.practice.viewmodel.TypeListViewModel
import com.codenly.practice.data.local.ClientEntity
import com.codenly.practice.viewmodel.ClientEditViewModel
import com.codenly.practice.viewmodel.ClientListViewModel
import com.codenly.practice.data.local.RealtorEntity
import com.codenly.practice.viewmodel.RealtorEditViewModel
import com.codenly.practice.viewmodel.RealtorListViewModel
//import com.codenly.practice.data.local.PropertyEntity
//import com.codenly.practice.viewmodel.PropertyEditViewModel
//import com.codenly.practice.viewmodel.PropertyListViewModel
//import com.codenly.practice.data.local.ViewEntity
//import com.codenly.practice.viewmodel.ViewEditViewModel
//import com.codenly.practice.viewmodel.ViewListViewModel
//import com.codenly.practice.data.local.DealEntity
//import com.codenly.practice.viewmodel.DealEditViewModel
//import com.codenly.practice.viewmodel.DealListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToOwners: () -> Unit,
    onNavigateToTypes: () -> Unit,
    onNavigateToClients: () -> Unit,
    onNavigateToRealtors: () -> Unit,
//    onNavigateToProperties: () -> Unit,
//    onNavigateToViews: () -> Unit,
//    onNavigateToDeals: () -> Unit,
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
            Button(onNavigateToTypes, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Типы недвижимости")
            }
            Button(onNavigateToClients, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Клиенты")
            }
            Button(onNavigateToRealtors, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Риелторы")
            }
//            Button(onNavigateToProperties, modifier = Modifier.fillMaxWidth(0.8f)) {
//                Text("Объекты недвижимости")
//            }
//            Button(onNavigateToViews, modifier = Modifier.fillMaxWidth(0.8f)) {
//                Text("Просмотры")
//            }
//            Button(onNavigateToDeals, modifier = Modifier.fillMaxWidth(0.8f)) {
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
    val focusManager = LocalFocusManager.current

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
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            OutlinedTextField(
                value = uiState.phoneNumber,
                onValueChange = ownerEditViewModel::onPhoneNumberChange,
                label = { Text("Номер телефона") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            OutlinedTextField(
                value = uiState.email,
                onValueChange = ownerEditViewModel::onEmailChange,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeListScreen(
    onNavigateToEdit: (Long?) -> Unit,
    typeViewModel: TypeListViewModel = viewModel()
) {
    val uiState by typeViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            typeViewModel.clearError()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Тип недвижимости") }) },
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
                uiState.types.isEmpty() -> {
                    Text(
                        text = "Нет ни одного типа недвижимости",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.types, key = { it.id }) { type ->
                            TypeItem(
                                type = type,
                                onEdit = { onNavigateToEdit(type.id) },
                                onDelete = { typeViewModel.deleteType(type) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TypeItem(
    type: TypeEntity,
    onEdit: () ->Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize()
        .padding(4.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = type.name, style = MaterialTheme.typography.bodyMedium)
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
fun TypeEditScreen(
    navController: NavController,
    typeEditViewModel: TypeEditViewModel = viewModel()
) {
    val uiState by typeEditViewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (uiState.isNew) "Новый тип недвижимости" else "Редактирование") })
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = typeEditViewModel::onNameChange,
                label = { Text("Название") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            if (uiState.errorMessage != null) {
                Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
            }
            Button(
                onClick = typeEditViewModel::save,
                enabled = !uiState.isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isSaving) CircularProgressIndicator(modifier = Modifier.size(20.dp))
                else Text("Сохранить")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientListScreen(
    onNavigateToEdit: (Long?) -> Unit,
    clientViewModel: ClientListViewModel = viewModel()
) {
    val uiState by clientViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            clientViewModel.clearError()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Клиенты") }) },
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
                uiState.clients.isEmpty() -> {
                    Text(
                        text = "Нет ни одного клиента",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.clients, key = { it.id }) { client ->
                            ClientItem(
                                client = client,
                                onEdit = { onNavigateToEdit(client.id) },
                                onDelete = { clientViewModel.deleteClient(client) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClientItem(
    client: ClientEntity,
    onEdit: () ->Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize()
            .padding(4.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = client.fullName, style = MaterialTheme.typography.bodyMedium)
                Text(text = client.phoneNumber,style = MaterialTheme.typography.bodyMedium)
                Text(text = client.email,style = MaterialTheme.typography.bodyMedium)
                Text(text = client.preference,style = MaterialTheme.typography.bodyMedium)
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
fun ClientEditScreen(
    navController: NavController,
    clientEditViewModel: ClientEditViewModel = viewModel()
) {
    val uiState by clientEditViewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (uiState.isNew) "Новый клиент" else "Редактирование") })
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = uiState.fullName,
                onValueChange = clientEditViewModel::onFullNameChange,
                label = { Text("ФИО") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            OutlinedTextField(
                value = uiState.phoneNumber,
                onValueChange = clientEditViewModel::onPhoneNumberChange,
                label = { Text("Номер телефона") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            OutlinedTextField(
                value = uiState.email,
                onValueChange = clientEditViewModel::onEmailChange,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Email,
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            OutlinedTextField(
                value = uiState.preference,
                onValueChange = clientEditViewModel::onPreferenceChange,
                label = { Text("Предпочтения") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            if (uiState.errorMessage != null) {
                Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
            }
            Button(
                onClick = clientEditViewModel::save,
                enabled = !uiState.isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isSaving) CircularProgressIndicator(modifier = Modifier.size(20.dp))
                else Text("Сохранить")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealtorListScreen(
    onNavigateToEdit: (Long?) -> Unit,
    realtorViewModel: RealtorListViewModel = viewModel()
) {
    val uiState by realtorViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            realtorViewModel.clearError()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Риелторы") }) },
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
                uiState.realtors.isEmpty() -> {
                    Text(
                        text = "Нет ни одного риелтора",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.realtors, key = { it.id }) { realtor ->
                            RealtorItem(
                                realtor = realtor,
                                onEdit = { onNavigateToEdit(realtor.id) },
                                onDelete = { realtorViewModel.deleteRealtor(realtor) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RealtorItem(
    realtor: RealtorEntity,
    onEdit: () ->Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize()
            .padding(4.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = realtor.fullName, style = MaterialTheme.typography.bodyMedium)
                Text(text = realtor.login,style = MaterialTheme.typography.bodyMedium)
                Text(text = realtor.password,style = MaterialTheme.typography.bodyMedium)
                Text(text = realtor.phoneNumber,style = MaterialTheme.typography.bodyMedium)
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
fun RealtorEditScreen(
    navController: NavController,
    realtorEditViewModel: RealtorEditViewModel = viewModel()
) {
    val uiState by realtorEditViewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (uiState.isNew) "Новый риелтор" else "Редактирование") })
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = uiState.fullName,
                onValueChange = realtorEditViewModel::onFullNameChange,
                label = { Text("ФИО") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            OutlinedTextField(
                value = uiState.login,
                onValueChange = realtorEditViewModel::onLoginChange,
                label = { Text("Логин") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Email,
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            OutlinedTextField(
                value = uiState.password,
                onValueChange = realtorEditViewModel::onPasswordChange,
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Password,
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            OutlinedTextField(
                value = uiState.phoneNumber,
                onValueChange = realtorEditViewModel::onPhoneNumberChange,
                label = { Text("Номер телефона") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            if (uiState.errorMessage != null) {
                Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
            }
            Button(
                onClick = realtorEditViewModel::save,
                enabled = !uiState.isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isSaving) CircularProgressIndicator(modifier = Modifier.size(20.dp))
                else Text("Сохранить")
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PropertyListScreen(
//    onNavigateToEdit: (Long?) -> Unit,
//    propertyViewModel: PropertyListViewModel = viewModel()
//) {
//    val uiState by propertyViewModel.uiState.collectAsStateWithLifecycle()
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    LaunchedEffect(uiState.errorMessage) {
//        uiState.errorMessage?.let {
//            snackbarHostState.showSnackbar(it)
//            propertyViewModel.clearError()
//        }
//    }
//
//    Scaffold(
//        topBar = { TopAppBar(title = { Text("Объекты недвижимости") }) },
//        floatingActionButton = {
//            FloatingActionButton(onClick = { onNavigateToEdit(null) }) {
//                Text("+")
//            }
//        },
//        snackbarHost = { SnackbarHost(snackbarHostState) }
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            when {
//                uiState.isLoading -> {
//                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//                }
//                uiState.properties.isEmpty() -> {
//                    Text(
//                        text = "Нет ни одного объекта недвижимости",
//                        modifier = Modifier.align(Alignment.Center)
//                    )
//                }
//                else -> {
//                    LazyColumn(
//                        contentPadding = PaddingValues(16.dp),
//                        verticalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        items(uiState.properties, key = { it.id }) { property ->
//                            PropertyItem(
//                                property = property,
//                                onEdit = { onNavigateToEdit(property.id) },
//                                onDelete = { propertyViewModel.deleteProperty(property) }
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun PropertyItem(
//    property: PropertyEntity,
//    onEdit: () ->Unit,
//    onDelete: () -> Unit
//) {
//    Row(
//        modifier = Modifier.fillMaxSize()
//            .padding(4.dp)
//    ) {
//        Card(modifier = Modifier.fillMaxWidth()) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text(text = property.ownerId, style = MaterialTheme.typography.bodyMedium)
//                Text(text = property.typeId, style = MaterialTheme.typography.bodyMedium)
//                Text(text = property.address, style = MaterialTheme.typography.bodyMedium)
//                Text(text = property.city, style = MaterialTheme.typography.bodyMedium)
//                Text(text = property.district, style = MaterialTheme.typography.bodyMedium)
//                Text(text = property.area, style = MaterialTheme.typography.bodyMedium)
//                Text(text = property.rooms, style = MaterialTheme.typography.bodyMedium)
//                Text(text = property.floor, style = MaterialTheme.typography.bodyMedium)
//                Text(text = property.floorsTotal, style = MaterialTheme.typography.bodyMedium)
//                Text(text = property.price, style = MaterialTheme.typography.bodyMedium)
//                Text(text = property.status, style = MaterialTheme.typography.bodyMedium)
//                Text(text = property.date, style = MaterialTheme.typography.bodyMedium)
//                Text(text = property.description, style = MaterialTheme.typography.bodyMedium)
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    TextButton(onClick = onEdit) { Text("Изменить") }
//                    TextButton(onClick = onDelete) { Text("Удалить") }
//                }
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PropertyEditScreen(
//    navController: NavController,
//    propertyEditViewModel: PropertyEditViewModel = viewModel()
//) {
//    val uiState by propertyEditViewModel.uiState.collectAsStateWithLifecycle()
//
//    LaunchedEffect(uiState.saveSuccess) {
//        if (uiState.saveSuccess) {
//            navController.popBackStack()
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text(if (uiState.isNew) "Новый объект недвижимости" else "Редактирование") })
//        }
//    ) { paddingValues ->
//        Column(modifier = Modifier
//            .fillMaxSize()
//            .padding(paddingValues)
//            .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)) {
//            OutlinedTextField(
//                value = uiState.ownerId,
//                onValueChange = propertyEditViewModel::onOwnerIdChange,
//                label = { Text("Id владельца") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.typeId,
//                onValueChange = propertyEditViewModel::onTypeIdChange,
//                label = { Text("Id типа") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.address,
//                onValueChange = propertyEditViewModel::onAddressChange,
//                label = { Text("Адрес") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.city,
//                onValueChange = propertyEditViewModel::onCityChange,
//                label = { Text("Город") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.district,
//                onValueChange = propertyEditViewModel::onDistrictChange,
//                label = { Text("Район") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.area,
//                onValueChange = propertyEditViewModel::onAreaChange,
//                label = { Text("Площадь") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.rooms,
//                onValueChange = propertyEditViewModel::onRoomsChange,
//                label = { Text("Комнат") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.floor,
//                onValueChange = propertyEditViewModel::onFloorChange,
//                label = { Text("Этаж") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.floorsTotal,
//                onValueChange = propertyEditViewModel::onFloorsTotalChange,
//                label = { Text("Этажей в доме") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.price,
//                onValueChange = propertyEditViewModel::onPriceChange,
//                label = { Text("Цена") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.status,
//                onValueChange = propertyEditViewModel::onStatusChange,
//                label = { Text("Статус") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.date,
//                onValueChange = propertyEditViewModel::onDateChange,
//                label = { Text("Дата размещения") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.description,
//                onValueChange = propertyEditViewModel::onDescriptionChange,
//                label = { Text("Описание") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            if (uiState.errorMessage != null) {
//                Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
//            }
//            Button(
//                onClick = propertyEditViewModel::save,
//                enabled = !uiState.isSaving,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                if (uiState.isSaving) CircularProgressIndicator(modifier = Modifier.size(20.dp))
//                else Text("Сохранить")
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ViewListScreen(
//    onNavigateToEdit: (Long?) -> Unit,
//    viewViewModel: ViewListViewModel = viewModel()
//) {
//    val uiState by viewViewModel.uiState.collectAsStateWithLifecycle()
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    LaunchedEffect(uiState.errorMessage) {
//        uiState.errorMessage?.let {
//            snackbarHostState.showSnackbar(it)
//            viewViewModel.clearError()
//        }
//    }
//
//    Scaffold(
//        topBar = { TopAppBar(title = { Text("Просмотры") }) },
//        floatingActionButton = {
//            FloatingActionButton(onClick = { onNavigateToEdit(null) }) {
//                Text("+")
//            }
//        },
//        snackbarHost = { SnackbarHost(snackbarHostState) }
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            when {
//                uiState.isLoading -> {
//                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//                }
//                uiState.views.isEmpty() -> {
//                    Text(
//                        text = "Нет ни одного просмотра",
//                        modifier = Modifier.align(Alignment.Center)
//                    )
//                }
//                else -> {
//                    LazyColumn(
//                        contentPadding = PaddingValues(16.dp),
//                        verticalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        items(uiState.views, key = { it.id }) { view ->
//                            ViewItem(
//                                view = view,
//                                onEdit = { onNavigateToEdit(view.id) },
//                                onDelete = { viewViewModel.deleteView(view) }
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ViewItem(
//    view: ViewEntity,
//    onEdit: () ->Unit,
//    onDelete: () -> Unit
//) {
//    Row(
//        modifier = Modifier.fillMaxSize()
//            .padding(4.dp)
//    ) {
//        Card(modifier = Modifier.fillMaxWidth()) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text(text = view.propertyId, style = MaterialTheme.typography.bodyMedium)
//                Text(text = view.clientId, style = MaterialTheme.typography.bodyMedium)
//                Text(text = view.realtorId, style = MaterialTheme.typography.bodyMedium)
//                Text(text = view.date, style = MaterialTheme.typography.bodyMedium)
//                Text(text = view.result, style = MaterialTheme.typography.bodyMedium)
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    TextButton(onClick = onEdit) { Text("Изменить") }
//                    TextButton(onClick = onDelete) { Text("Удалить") }
//                }
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ViewEditScreen(
//    navController: NavController,
//    viewEditViewModel: ViewEditViewModel = viewModel()
//) {
//    val uiState by viewEditViewModel.uiState.collectAsStateWithLifecycle()
//
//    LaunchedEffect(uiState.saveSuccess) {
//        if (uiState.saveSuccess) {
//            navController.popBackStack()
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text(if (uiState.isNew) "Новый просмотр" else "Редактирование") })
//        }
//    ) { paddingValues ->
//        Column(modifier = Modifier
//            .fillMaxSize()
//            .padding(paddingValues)
//            .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)) {
//            OutlinedTextField(
//                value = uiState.propertyId,
//                onValueChange = viewEditViewModel::onPropertyIdChange,
//                label = { Text("Id объекта недвижимости") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.clientId,
//                onValueChange = viewEditViewModel::onClientIdChange,
//                label = { Text("Id клиента") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.realtorId,
//                onValueChange = viewEditViewModel::onRealtorIdChange,
//                label = { Text("Id риелтора") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.date,
//                onValueChange = viewEditViewModel::onDateChange,
//                label = { Text("Дата и время") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.result,
//                onValueChange = viewEditViewModel::onResultChange,
//                label = { Text("Результат") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            if (uiState.errorMessage != null) {
//                Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
//            }
//            Button(
//                onClick = viewEditViewModel::save,
//                enabled = !uiState.isSaving,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                if (uiState.isSaving) CircularProgressIndicator(modifier = Modifier.size(20.dp))
//                else Text("Сохранить")
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DealListScreen(
//    onNavigateToEdit: (Long?) -> Unit,
//    dealViewModel: DealListViewModel = viewModel()
//) {
//    val uiState by dealViewModel.uiState.collectAsStateWithLifecycle()
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    LaunchedEffect(uiState.errorMessage) {
//        uiState.errorMessage?.let {
//            snackbarHostState.showSnackbar(it)
//            dealViewModel.clearError()
//        }
//    }
//
//    Scaffold(
//        topBar = { TopAppBar(title = { Text("Сделки") }) },
//        floatingActionButton = {
//            FloatingActionButton(onClick = { onNavigateToEdit(null) }) {
//                Text("+")
//            }
//        },
//        snackbarHost = { SnackbarHost(snackbarHostState) }
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            when {
//                uiState.isLoading -> {
//                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//                }
//                uiState.deals.isEmpty() -> {
//                    Text(
//                        text = "Нет ни одной сделки",
//                        modifier = Modifier.align(Alignment.Center)
//                    )
//                }
//                else -> {
//                    LazyColumn(
//                        contentPadding = PaddingValues(16.dp),
//                        verticalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        items(uiState.deals, key = { it.id }) { deal ->
//                            DealItem(
//                                deal = deal,
//                                onEdit = { onNavigateToEdit(deal.id) },
//                                onDelete = { dealViewModel.deleteDeal(deal) }
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun DealItem(
//    deal: DealEntity,
//    onEdit: () ->Unit,
//    onDelete: () -> Unit
//) {
//    Row(
//        modifier = Modifier.fillMaxSize()
//            .padding(4.dp)
//    ) {
//        Card(modifier = Modifier.fillMaxWidth()) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text(text = deal.propertyId, style = MaterialTheme.typography.bodyMedium)
//                Text(text = deal.clientId, style = MaterialTheme.typography.bodyMedium)
//                Text(text = deal.realtorId, style = MaterialTheme.typography.bodyMedium)
//                Text(text = deal.date, style = MaterialTheme.typography.bodyMedium)
//                Text(text = deal.type, style = MaterialTheme.typography.bodyMedium)
//                Text(text = deal.finalPrice, style = MaterialTheme.typography.bodyMedium)
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    TextButton(onClick = onEdit) { Text("Изменить") }
//                    TextButton(onClick = onDelete) { Text("Удалить") }
//                }
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DealEditScreen(
//    navController: NavController,
//    dealEditViewModel: DealEditViewModel = viewModel()
//) {
//    val uiState by dealEditViewModel.uiState.collectAsStateWithLifecycle()
//
//    LaunchedEffect(uiState.saveSuccess) {
//        if (uiState.saveSuccess) {
//            navController.popBackStack()
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text(if (uiState.isNew) "Новая сделка" else "Редактирование") })
//        }
//    ) { paddingValues ->
//        Column(modifier = Modifier
//            .fillMaxSize()
//            .padding(paddingValues)
//            .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)) {
//            OutlinedTextField(
//                value = uiState.propertyId,
//                onValueChange = dealEditViewModel::onPropertyIdChange,
//                label = { Text("Id объекта недвижимости") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.clientId,
//                onValueChange = dealEditViewModel::onClientIdChange,
//                label = { Text("Id клиента") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.realtorId,
//                onValueChange = dealEditViewModel::onRealtorIdChange,
//                label = { Text("Id риелтора") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.date,
//                onValueChange = dealEditViewModel::onDateChange,
//                label = { Text("Дата и время") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.type,
//                onValueChange = dealEditViewModel::onTypeChange,
//                label = { Text("Тип") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            OutlinedTextField(
//                value = uiState.finalPrice,
//                onValueChange = dealEditViewModel::onFinalPriceChange,
//                label = { Text("Окончательная стоимость") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.Sentences
//                )
//            )
//            if (uiState.errorMessage != null) {
//                Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
//            }
//            Button(
//                onClick = dealEditViewModel::save,
//                enabled = !uiState.isSaving,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                if (uiState.isSaving) CircularProgressIndicator(modifier = Modifier.size(20.dp))
//                else Text("Сохранить")
//            }
//        }
//    }
//}