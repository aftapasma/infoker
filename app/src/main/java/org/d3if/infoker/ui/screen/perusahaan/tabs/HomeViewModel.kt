package org.d3if.infoker.ui.screen.perusahaan.tabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository

class HomeViewModel(private val authRepository: AuthRepository, private val firestoreRepository: FirestoreRepository) : ViewModel() {
    private val _companyJobs = MutableLiveData<List<DocumentSnapshot>>()
    val companyJobs: LiveData<List<DocumentSnapshot>> get() = _companyJobs

    fun getJobs() {
        val currentUser = authRepository.getCurrentUser()
        currentUser?.email?.let { email ->
            viewModelScope.launch {
                val jobs = firestoreRepository.getJobsByCompany(email)
                _companyJobs.value = jobs
            }
        }
    }

    fun hasApplicationsForJob(jobId: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch {
            result.value = firestoreRepository.hasApplicationsForJob(jobId)
        }
        return result
    }
}