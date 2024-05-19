package org.d3if.infoker.ui.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.d3if.infoker.FirestoreRepository
import org.d3if.infoker.model.Job

class JobDetailViewModel(private val repository: FirestoreRepository) : ViewModel() {
    private val _addJobResult = MutableLiveData<Boolean>()
    val addJobResult: LiveData<Boolean> get() = _addJobResult

    fun addJob(title: String, company: String, location: String, salary: Float, description: String) {
        viewModelScope.launch {
            val job = Job(title = title, company = company, location = location, salary = salary, description = description)
            _addJobResult.value = repository.addJob(job)
        }
    }
}