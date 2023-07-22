package com.example.tokokomputer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.tokokomputer.model.Computer
import com.example.tokokomputer.repository.ComputerRepository
import kotlinx.coroutines.launch

class ComputerViewModel(private val repository: ComputerRepository): ViewModel() {
    val allComputers: LiveData<List<Computer>> = repository.allComputer.asLiveData()

    fun insert(computer: Computer) = viewModelScope.launch {
        repository.insertComputer(computer)

    }
    fun delete(computer: Computer) = viewModelScope.launch {
        repository.deleteComputer(computer)
    }
    fun update(computer: Computer) = viewModelScope.launch {
        repository.updateComputer(computer)
    }
}

class ComputerViewModelFactory(private val repository: ComputerRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((ComputerViewModel::class.java))) {
            return ComputerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}