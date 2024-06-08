package org.d3if.infoker.ui.screen.perusahaan

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository

class AddJobViewModel(private val authRepository: AuthRepository, private val firestoreRepository: FirestoreRepository) : ViewModel() {

    fun addJob(title: String, location: String, salary: Float, description: String) {
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser()
            if (currentUser != null) {
                val userEmail = currentUser.email ?: ""
                val jobAdded = firestoreRepository.addJob(title, location, salary, description, userEmail)
                if (jobAdded) {
                    Log.d("JobViewModel", "Job added to Firestore")
                } else {
                    Log.e("JobViewModel", "Failed to add job to Firestore")
                }
            } else {
                Log.e("JobViewModel", "No current user found")
            }
        }
    }
}