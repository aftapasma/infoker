package org.d3if.infoker.ui.screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.d3if.infoker.repository.FirestoreRepository

class AddJobViewModel(private val firestoreRepository: FirestoreRepository) : ViewModel() {
    private val _addJobResult = MutableLiveData<Boolean>()
    val addJobResult: LiveData<Boolean> get() = _addJobResult

    fun addJob(title: String, location: String, salary: Float, description: String) {
        viewModelScope.launch {
            val jobAdded = firestoreRepository.addJob(title, location, salary, description)
            if (jobAdded) {
                Log.d("JobViewModel", "Job added to Firestore")
            } else {
                Log.e("JobViewModel", "Failed to add job to Firestore")
            }
        }
    }
}