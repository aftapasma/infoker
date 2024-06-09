package org.d3if.infoker.ui.screen.perusahaan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.d3if.infoker.repository.FirestoreRepository

class ApplicantDetailViewModel(private val firestoreRepository: FirestoreRepository) : ViewModel() {
    private val _applicantDetail = MutableStateFlow<DocumentSnapshot?>(null)
    val applicantDetail: StateFlow<DocumentSnapshot?> = _applicantDetail

    fun getApplicationById(applicationId: String) {
        viewModelScope.launch {
            val application = firestoreRepository.getApplicationById(applicationId)
            _applicantDetail.value = application
        }
    }

    fun updateApplicationStatus(applicationId: String, status: String) {
        viewModelScope.launch {
            firestoreRepository.updateApplicationStatus(applicationId, status)
            // Fetch the updated application details
            val updatedApplication = firestoreRepository.getApplicationById(applicationId)
            _applicantDetail.value = updatedApplication
        }
    }

    suspend fun getUserPhotoUrl(email: String): String? {
        return firestoreRepository.getPhotoUrl(email)
    }
}