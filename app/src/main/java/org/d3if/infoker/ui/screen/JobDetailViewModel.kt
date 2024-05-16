package org.d3if.infoker.ui.screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.d3if.infoker.FirestoreRepository
import org.d3if.infoker.model.Job

class JobDetailViewModel(private val repository: FirestoreRepository) : ViewModel() {
    private val _addJobResult = MutableLiveData<Boolean>()

    fun addJob(title: String, description: String, salary: Float) {
        viewModelScope.launch {
            val job = Job(title = title, description = description, salary = salary)
            _addJobResult.value = repository.addJob(job)
        }
    }
}