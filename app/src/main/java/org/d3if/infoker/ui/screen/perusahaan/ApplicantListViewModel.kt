package org.d3if.infoker.ui.screen.perusahaan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.d3if.infoker.repository.FirestoreRepository

class ApplicantListViewModel(private val firestoreRepository: FirestoreRepository) : ViewModel() {
    private val _applicants = MutableStateFlow<List<DocumentSnapshot>>(emptyList())
    val applicants: StateFlow<List<DocumentSnapshot>> = _applicants

    fun getApplicationsByJobId(jobId: String) {
        viewModelScope.launch {
            val applications = firestoreRepository.getApplicationsByJobId(jobId)
            _applicants.value = applications
        }
    }

    suspend fun getUserPhotoUrl(email: String): String? {
        return firestoreRepository.getPhotoUrl(email)
    }
}