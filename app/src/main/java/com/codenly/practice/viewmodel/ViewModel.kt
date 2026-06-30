package com.codenly.practice.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codenly.practice.data.OwnerRepository
import com.codenly.practice.data.local.OwnerEntity
import com.codenly.practice.data.TypeRepository
import com.codenly.practice.data.local.TypeEntity
import com.codenly.practice.data.ClientRepository
import com.codenly.practice.data.local.ClientEntity
import com.codenly.practice.data.RealtorRepository
import com.codenly.practice.data.local.RealtorEntity
import com.codenly.practice.data.PropertyRepository
import com.codenly.practice.data.local.PropertyEntity
import com.codenly.practice.data.ViewRepository
import com.codenly.practice.data.local.ViewEntity
import com.codenly.practice.data.DealRepository
import com.codenly.practice.data.local.DealEntity
import com.codenly.practice.data.local.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OwnerListUiState(
    val owners: List<OwnerEntity> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

data class OwnerEditUiState(
    val id: Long? = null,
    val fullName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val isNew: Boolean = true,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null
)

class OwnerListViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = OwnerRepository(db.getOwnerDao())

    private val _uiState = MutableStateFlow(OwnerListUiState())
    val uiState: StateFlow<OwnerListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allOwners.collect { owners ->
                _uiState.update {
                    it.copy(
                        owners = owners,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun deleteOwner(owner: OwnerEntity) {
        viewModelScope.launch {
            try {
                repository.deleteOwner(owner)
            } catch (e: Exception){
                _uiState.update { it.copy(errorMessage = "Ошибка удаления ${e.message}") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

class OwnerEditViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = OwnerRepository(db.getOwnerDao())

    private val ownerId: Long? = savedStateHandle.get<String>("ownerId")?.toLongOrNull()

    private val _uiState = MutableStateFlow(OwnerEditUiState())
    val uiState: StateFlow<OwnerEditUiState> = _uiState.asStateFlow()

    init {
        if (ownerId != null && ownerId != -1L){
            viewModelScope.launch {
                val owner = repository.getOwnerById(ownerId)
                if (owner != null) {
                    _uiState.update {
                        it.copy(
                            fullName = owner.fullName,
                            phoneNumber = owner.phoneNumber,
                            email = owner.email,
                            isNew = false
                        )
                    }
                }
            }
        }
    }

    fun onFullNameChange(fullName: String) {
        _uiState.update { it.copy(fullName = fullName) }
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        _uiState.update { it.copy(phoneNumber = phoneNumber) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun save() {
        if (_uiState.value.fullName.isBlank() || _uiState.value.phoneNumber.isBlank()
            || _uiState.value.email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Поле не может быть пустым") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val owner = OwnerEntity(
                    id = if (ownerId != null && ownerId != -1L) ownerId else 0,
                    fullName = _uiState.value.fullName,
                    phoneNumber = _uiState.value.phoneNumber,
                    email = _uiState.value.email
                )
                if (_uiState.value.isNew) {
                    repository.addOwner(owner)
                } else {
                    repository.updateOwner(owner)
                }
                _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, errorMessage = "Ошибка сохранения ${e.message}") }
            }
        }
    }
}

data class TypeListUiState(
    val types: List<TypeEntity> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

data class TypeEditUiState(
    val id: Long? = null,
    val name: String = "",
    val isNew: Boolean = true,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null
)

class TypeListViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = TypeRepository(db.getTypeDao())

    private val _uiState = MutableStateFlow(TypeListUiState())
    val uiState: StateFlow<TypeListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allTypes.collect { types ->
                _uiState.update {
                    it.copy(
                        types = types,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun deleteType(type: TypeEntity) {
        viewModelScope.launch {
            try {
                repository.deleteType(type)
            } catch (e: Exception){
                _uiState.update { it.copy(errorMessage = "Ошибка удаления ${e.message}") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

class TypeEditViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = TypeRepository(db.getTypeDao())

    private val typeId: Long? = savedStateHandle.get<String>("typeId")?.toLongOrNull()

    private val _uiState = MutableStateFlow(TypeEditUiState())
    val uiState: StateFlow<TypeEditUiState> = _uiState.asStateFlow()

    init {
        if (typeId != null && typeId != -1L){
            viewModelScope.launch {
                val type = repository.getTypeById(typeId)
                if (type != null) {
                    _uiState.update {
                        it.copy(
                            name = type.name,
                            isNew = false
                        )
                    }
                }
            }
        }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun save() {
        if (_uiState.value.name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Поле не может быть пустым") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val type = TypeEntity(
                    id = if (typeId != null && typeId != -1L) typeId else 0,
                    name = _uiState.value.name
                )
                if (_uiState.value.isNew) {
                    repository.addType(type)
                } else {
                    repository.updateType(type)
                }
                _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, errorMessage = "Ошибка сохранения ${e.message}") }
            }
        }
    }
}

data class ClientListUiState(
    val clients: List<ClientEntity> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

data class ClientEditUiState(
    val id: Long? = null,
    val fullName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val preference: String = "",
    val isNew: Boolean = true,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null
)

class ClientListViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = ClientRepository(db.getClientDao())

    private val _uiState = MutableStateFlow(ClientListUiState())
    val uiState: StateFlow<ClientListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allClients.collect { clients ->
                _uiState.update {
                    it.copy(
                        clients = clients,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun deleteClient(client: ClientEntity) {
        viewModelScope.launch {
            try {
                repository.deleteClient(client)
            } catch (e: Exception){
                _uiState.update { it.copy(errorMessage = "Ошибка удаления ${e.message}") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

class ClientEditViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = ClientRepository(db.getClientDao())

    private val clientId: Long? = savedStateHandle.get<String>("clientId")?.toLongOrNull()

    private val _uiState = MutableStateFlow(ClientEditUiState())
    val uiState: StateFlow<ClientEditUiState> = _uiState.asStateFlow()

    init {
        if (clientId != null && clientId != -1L){
            viewModelScope.launch {
                val client = repository.getClientById(clientId)
                if (client != null) {
                    _uiState.update {
                        it.copy(
                            fullName = client.fullName,
                            phoneNumber = client.phoneNumber,
                            email = client.email,
                            preference = client.preference,
                            isNew = false
                        )
                    }
                }
            }
        }
    }

    fun onFullNameChange(fullName: String) {
        _uiState.update { it.copy(fullName = fullName) }
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        _uiState.update { it.copy(phoneNumber = phoneNumber) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPreferenceChange(preference: String) {
        _uiState.update { it.copy(preference = preference) }
    }

    fun save() {
        if (_uiState.value.fullName.isBlank() || _uiState.value.phoneNumber.isBlank()
            || _uiState.value.email.isBlank() || _uiState.value.preference.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Поле не может быть пустым") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val client = ClientEntity(
                    id = if (clientId != null && clientId != -1L) clientId else 0,
                    fullName = _uiState.value.fullName,
                    phoneNumber = _uiState.value.phoneNumber,
                    email = _uiState.value.email,
                    preference = _uiState.value.preference
                )
                if (_uiState.value.isNew) {
                    repository.addClient(client)
                } else {
                    repository.updateClient(client)
                }
                _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, errorMessage = "Ошибка сохранения ${e.message}") }
            }
        }
    }
}

data class RealtorListUiState(
    val realtors: List<RealtorEntity> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

data class RealtorEditUiState(
    val id: Long? = null,
    val fullName: String = "",
    val login: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val isNew: Boolean = true,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null
)

class RealtorListViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = RealtorRepository(db.getRealtorDao())

    private val _uiState = MutableStateFlow(RealtorListUiState())
    val uiState: StateFlow<RealtorListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allRealtors.collect { realtors ->
                _uiState.update {
                    it.copy(
                        realtors = realtors,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun deleteRealtor(realtor: RealtorEntity) {
        viewModelScope.launch {
            try {
                repository.deleteRealtor(realtor)
            } catch (e: Exception){
                _uiState.update { it.copy(errorMessage = "Ошибка удаления ${e.message}") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

class RealtorEditViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = RealtorRepository(db.getRealtorDao())

    private val realtorId: Long? = savedStateHandle.get<String>("realtorId")?.toLongOrNull()

    private val _uiState = MutableStateFlow(RealtorEditUiState())
    val uiState: StateFlow<RealtorEditUiState> = _uiState.asStateFlow()

    init {
        if (realtorId != null && realtorId != -1L){
            viewModelScope.launch {
                val realtor = repository.getRealtorById(realtorId)
                if (realtor != null) {
                    _uiState.update {
                        it.copy(
                            fullName = realtor.fullName,
                            login = realtor.login,
                            password = realtor.password,
                            phoneNumber = realtor.phoneNumber,
                            isNew = false
                        )
                    }
                }
            }
        }
    }

    fun onFullNameChange(fullName: String) {
        _uiState.update { it.copy(fullName = fullName) }
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        _uiState.update { it.copy(phoneNumber = phoneNumber) }
    }

    fun onLoginChange(login: String) {
        _uiState.update { it.copy(login = login) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun save() {
        if (_uiState.value.fullName.isBlank() || _uiState.value.phoneNumber.isBlank()
            || _uiState.value.login.isBlank() || _uiState.value.login.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Поле не может быть пустым") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val realtor = RealtorEntity(
                    id = if (realtorId != null && realtorId != -1L) realtorId else 0,
                    fullName = _uiState.value.fullName,
                    login = _uiState.value.login,
                    password = _uiState.value.password,
                    phoneNumber = _uiState.value.phoneNumber
                )
                if (_uiState.value.isNew) {
                    repository.addRealtor(realtor)
                } else {
                    repository.updateRealtor(realtor)
                }
                _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, errorMessage = "Ошибка сохранения ${e.message}") }
            }
        }
    }
}

//data class OwnerListUiState(
//    val owners: List<OwnerEntity> = emptyList(),
//    val isLoading: Boolean = true,
//    val errorMessage: String? = null
//)
//
//data class OwnerEditUiState(
//    val id: Long? = null,
//    val fullName: String = "",
//    val phoneNumber: String = "",
//    val email: String = "",
//    val isNew: Boolean = true,
//    val isSaving: Boolean = false,
//    val saveSuccess: Boolean = false,
//    val errorMessage: String? = null
//)
//
//class OwnerListViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val db = AppDatabase.getDatabase(application)
//    private val repository = OwnerRepository(db.getOwnerDao())
//
//    private val _uiState = MutableStateFlow(OwnerListUiState())
//    val uiState: StateFlow<OwnerListUiState> = _uiState.asStateFlow()
//
//    init {
//        viewModelScope.launch {
//            repository.allOwners.collect { owners ->
//                _uiState.update {
//                    it.copy(
//                        owners = owners,
//                        isLoading = false,
//                        errorMessage = null
//                    )
//                }
//            }
//        }
//    }
//
//    fun deleteOwner(owner: OwnerEntity) {
//        viewModelScope.launch {
//            try {
//                repository.deleteOwner(owner)
//            } catch (e: Exception){
//                _uiState.update { it.copy(errorMessage = "Ошибка удаления ${e.message}") }
//            }
//        }
//    }
//
//    fun clearError() {
//        _uiState.update { it.copy(errorMessage = null) }
//    }
//}
//
//class OwnerEditViewModel(
//    application: Application,
//    savedStateHandle: SavedStateHandle
//) : AndroidViewModel(application) {
//
//    private val db = AppDatabase.getDatabase(application)
//    private val repository = OwnerRepository(db.getOwnerDao())
//
//    private val ownerId: Long? = savedStateHandle.get<String>("ownerId")?.toLongOrNull()
//
//    private val _uiState = MutableStateFlow(OwnerEditUiState())
//    val uiState: StateFlow<OwnerEditUiState> = _uiState.asStateFlow()
//
//    init {
//        if (ownerId != null && ownerId != -1L){
//            viewModelScope.launch {
//                val owner = repository.getOwnerById(ownerId)
//                if (owner != null) {
//                    _uiState.update {
//                        it.copy(
//                            fullName = owner.fullName,
//                            phoneNumber = owner.phoneNumber,
//                            email = owner.email,
//                            isNew = false
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    fun onFullNameChange(fullName: String) {
//        _uiState.update { it.copy(fullName = fullName) }
//    }
//
//    fun onPhoneNumberChange(phoneNumber: String) {
//        _uiState.update { it.copy(phoneNumber = phoneNumber) }
//    }
//
//    fun onEmailChange(email: String) {
//        _uiState.update { it.copy(email = email) }
//    }
//
//    fun save() {
//        if (_uiState.value.fullName.isBlank() || _uiState.value.phoneNumber.isBlank()
//            || _uiState.value.email.isBlank()) {
//            _uiState.update { it.copy(errorMessage = "Поле не может быть пустым") }
//            return
//        }
//        viewModelScope.launch {
//            _uiState.update { it.copy(isSaving = true) }
//            try {
//                val owner = OwnerEntity(
//                    id = if (ownerId != null && ownerId != -1L) ownerId else 0,
//                    fullName = _uiState.value.fullName,
//                    phoneNumber = _uiState.value.phoneNumber,
//                    email = _uiState.value.email
//                )
//                if (_uiState.value.isNew) {
//                    repository.addOwner(owner)
//                } else {
//                    repository.updateOwner(owner)
//                }
//                _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
//            } catch (e: Exception) {
//                _uiState.update { it.copy(isSaving = false, errorMessage = "Ошибка сохранения ${e.message}") }
//            }
//        }
//    }
//}
//
//data class OwnerListUiState(
//    val owners: List<OwnerEntity> = emptyList(),
//    val isLoading: Boolean = true,
//    val errorMessage: String? = null
//)
//
//data class OwnerEditUiState(
//    val id: Long? = null,
//    val fullName: String = "",
//    val phoneNumber: String = "",
//    val email: String = "",
//    val isNew: Boolean = true,
//    val isSaving: Boolean = false,
//    val saveSuccess: Boolean = false,
//    val errorMessage: String? = null
//)
//
//class OwnerListViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val db = AppDatabase.getDatabase(application)
//    private val repository = OwnerRepository(db.getOwnerDao())
//
//    private val _uiState = MutableStateFlow(OwnerListUiState())
//    val uiState: StateFlow<OwnerListUiState> = _uiState.asStateFlow()
//
//    init {
//        viewModelScope.launch {
//            repository.allOwners.collect { owners ->
//                _uiState.update {
//                    it.copy(
//                        owners = owners,
//                        isLoading = false,
//                        errorMessage = null
//                    )
//                }
//            }
//        }
//    }
//
//    fun deleteOwner(owner: OwnerEntity) {
//        viewModelScope.launch {
//            try {
//                repository.deleteOwner(owner)
//            } catch (e: Exception){
//                _uiState.update { it.copy(errorMessage = "Ошибка удаления ${e.message}") }
//            }
//        }
//    }
//
//    fun clearError() {
//        _uiState.update { it.copy(errorMessage = null) }
//    }
//}
//
//class OwnerEditViewModel(
//    application: Application,
//    savedStateHandle: SavedStateHandle
//) : AndroidViewModel(application) {
//
//    private val db = AppDatabase.getDatabase(application)
//    private val repository = OwnerRepository(db.getOwnerDao())
//
//    private val ownerId: Long? = savedStateHandle.get<String>("ownerId")?.toLongOrNull()
//
//    private val _uiState = MutableStateFlow(OwnerEditUiState())
//    val uiState: StateFlow<OwnerEditUiState> = _uiState.asStateFlow()
//
//    init {
//        if (ownerId != null && ownerId != -1L){
//            viewModelScope.launch {
//                val owner = repository.getOwnerById(ownerId)
//                if (owner != null) {
//                    _uiState.update {
//                        it.copy(
//                            fullName = owner.fullName,
//                            phoneNumber = owner.phoneNumber,
//                            email = owner.email,
//                            isNew = false
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    fun onFullNameChange(fullName: String) {
//        _uiState.update { it.copy(fullName = fullName) }
//    }
//
//    fun onPhoneNumberChange(phoneNumber: String) {
//        _uiState.update { it.copy(phoneNumber = phoneNumber) }
//    }
//
//    fun onEmailChange(email: String) {
//        _uiState.update { it.copy(email = email) }
//    }
//
//    fun save() {
//        if (_uiState.value.fullName.isBlank() || _uiState.value.phoneNumber.isBlank()
//            || _uiState.value.email.isBlank()) {
//            _uiState.update { it.copy(errorMessage = "Поле не может быть пустым") }
//            return
//        }
//        viewModelScope.launch {
//            _uiState.update { it.copy(isSaving = true) }
//            try {
//                val owner = OwnerEntity(
//                    id = if (ownerId != null && ownerId != -1L) ownerId else 0,
//                    fullName = _uiState.value.fullName,
//                    phoneNumber = _uiState.value.phoneNumber,
//                    email = _uiState.value.email
//                )
//                if (_uiState.value.isNew) {
//                    repository.addOwner(owner)
//                } else {
//                    repository.updateOwner(owner)
//                }
//                _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
//            } catch (e: Exception) {
//                _uiState.update { it.copy(isSaving = false, errorMessage = "Ошибка сохранения ${e.message}") }
//            }
//        }
//    }
//}
//
//data class OwnerListUiState(
//    val owners: List<OwnerEntity> = emptyList(),
//    val isLoading: Boolean = true,
//    val errorMessage: String? = null
//)
//
//data class OwnerEditUiState(
//    val id: Long? = null,
//    val fullName: String = "",
//    val phoneNumber: String = "",
//    val email: String = "",
//    val isNew: Boolean = true,
//    val isSaving: Boolean = false,
//    val saveSuccess: Boolean = false,
//    val errorMessage: String? = null
//)
//
//class OwnerListViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val db = AppDatabase.getDatabase(application)
//    private val repository = OwnerRepository(db.getOwnerDao())
//
//    private val _uiState = MutableStateFlow(OwnerListUiState())
//    val uiState: StateFlow<OwnerListUiState> = _uiState.asStateFlow()
//
//    init {
//        viewModelScope.launch {
//            repository.allOwners.collect { owners ->
//                _uiState.update {
//                    it.copy(
//                        owners = owners,
//                        isLoading = false,
//                        errorMessage = null
//                    )
//                }
//            }
//        }
//    }
//
//    fun deleteOwner(owner: OwnerEntity) {
//        viewModelScope.launch {
//            try {
//                repository.deleteOwner(owner)
//            } catch (e: Exception){
//                _uiState.update { it.copy(errorMessage = "Ошибка удаления ${e.message}") }
//            }
//        }
//    }
//
//    fun clearError() {
//        _uiState.update { it.copy(errorMessage = null) }
//    }
//}
//
//class OwnerEditViewModel(
//    application: Application,
//    savedStateHandle: SavedStateHandle
//) : AndroidViewModel(application) {
//
//    private val db = AppDatabase.getDatabase(application)
//    private val repository = OwnerRepository(db.getOwnerDao())
//
//    private val ownerId: Long? = savedStateHandle.get<String>("ownerId")?.toLongOrNull()
//
//    private val _uiState = MutableStateFlow(OwnerEditUiState())
//    val uiState: StateFlow<OwnerEditUiState> = _uiState.asStateFlow()
//
//    init {
//        if (ownerId != null && ownerId != -1L){
//            viewModelScope.launch {
//                val owner = repository.getOwnerById(ownerId)
//                if (owner != null) {
//                    _uiState.update {
//                        it.copy(
//                            fullName = owner.fullName,
//                            phoneNumber = owner.phoneNumber,
//                            email = owner.email,
//                            isNew = false
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    fun onFullNameChange(fullName: String) {
//        _uiState.update { it.copy(fullName = fullName) }
//    }
//
//    fun onPhoneNumberChange(phoneNumber: String) {
//        _uiState.update { it.copy(phoneNumber = phoneNumber) }
//    }
//
//    fun onEmailChange(email: String) {
//        _uiState.update { it.copy(email = email) }
//    }
//
//    fun save() {
//        if (_uiState.value.fullName.isBlank() || _uiState.value.phoneNumber.isBlank()
//            || _uiState.value.email.isBlank()) {
//            _uiState.update { it.copy(errorMessage = "Поле не может быть пустым") }
//            return
//        }
//        viewModelScope.launch {
//            _uiState.update { it.copy(isSaving = true) }
//            try {
//                val owner = OwnerEntity(
//                    id = if (ownerId != null && ownerId != -1L) ownerId else 0,
//                    fullName = _uiState.value.fullName,
//                    phoneNumber = _uiState.value.phoneNumber,
//                    email = _uiState.value.email
//                )
//                if (_uiState.value.isNew) {
//                    repository.addOwner(owner)
//                } else {
//                    repository.updateOwner(owner)
//                }
//                _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
//            } catch (e: Exception) {
//                _uiState.update { it.copy(isSaving = false, errorMessage = "Ошибка сохранения ${e.message}") }
//            }
//        }
//    }
//}