package com.codenly.practice.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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
    val owner: List<OwnerEntity> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class OwnerViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = OwnerRepository(db.getOwnerDao())

    private val _uiState = MutableStateFlow(OwnerListUiState())
    val uiState: StateFlow<OwnerListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allOwners.collect { owner ->
                _uiState.update { currentState ->
                    currentState.copy(
                        owner = owner,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun addTestOwner() {
        viewModelScope.launch {
            try {
                val newOwner = OwnerEntity(
                    fullName = "Сизов Дмитрий Петрович",
                    phoneNumber = "+79990009988",
                    email = "sizZOVv@mail.ru"
                )
                repository.addOwner(newOwner)
            } catch (e: Exception){
                _uiState.update { it.copy(errorMessage = "Ошибка при добавлении ${e.message}") }
            }
        }
    }


}
