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