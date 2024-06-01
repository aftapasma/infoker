package org.d3if.infoker.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.d3if.infoker.repository.FirestoreRepository

class AddJobViewModel(private val firestoreRepository: FirestoreRepository) : ViewModel() {

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