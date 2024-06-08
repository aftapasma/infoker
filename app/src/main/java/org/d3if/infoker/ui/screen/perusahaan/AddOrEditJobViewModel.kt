package org.d3if.infoker.ui.screen.perusahaan

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository

class AddOrEditJobViewModel(private val authRepository: AuthRepository, private val firestoreRepository: FirestoreRepository) : ViewModel() {
    var title by mutableStateOf("")
    var location by mutableStateOf("")
    var salary by mutableStateOf("")
    var description by mutableStateOf("")
    var isEditMode by mutableStateOf(false)

    fun addOrEditJob(jobId: String? = null) {
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser()
            if (currentUser != null) {
                val userEmail = currentUser.email ?: ""
                if (jobId == null) {
                    val jobAdded = firestoreRepository.addJob(title, location, salary.toFloat(), description, userEmail)
                    if (jobAdded) {
                        Log.d("JobViewModel", "Job added to Firestore")
                    } else {
                        Log.e("JobViewModel", "Failed to add job to Firestore")
                    }
                } else {
                    val jobUpdated = firestoreRepository.updateJob(jobId, title, location, salary.toFloat(), description)
                    if (jobUpdated) {
                        Log.d("JobViewModel", "Job updated in Firestore")
                    } else {
                        Log.e("JobViewModel", "Failed to update job in Firestore")
                    }
                }
            } else {
                Log.e("JobViewModel", "No current user found")
            }
        }
    }

    fun loadJobDetails(jobId: String) {
        viewModelScope.launch {
            val job = firestoreRepository.getJobById(jobId)
            if (job != null) {
                title = job.getString("title") ?: ""
                location = job.getString("location") ?: ""
                salary = job.getDouble("salary")?.toString() ?: ""
                description = job.getString("description") ?: ""
                isEditMode = true
            } else {
                Log.e("JobViewModel", "Job not found")
            }
        }
    }
}