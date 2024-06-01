package org.d3if.infoker.ui.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository

class JobDetailViewModel(private val authRepository: AuthRepository, private val firestoreRepository: FirestoreRepository) : ViewModel() {
    private val _jobDetail = MutableLiveData<DocumentSnapshot?>()
    val jobDetail: LiveData<DocumentSnapshot?> get() = _jobDetail

    private val _isBookmarked = MutableLiveData<Boolean>()
    val isBookmarked: LiveData<Boolean> get() = _isBookmarked

    private val _isApplied = MutableLiveData<Boolean>()
    val isApplied: LiveData<Boolean> get() = _isApplied

    fun getJobById(id: String) {
        viewModelScope.launch {
            val job = firestoreRepository.getJobById(id)
            _jobDetail.value = job

            // Fetch bookmark status
            authRepository.getCurrentUser()?.email?.let { email ->
                val isBookmarked = firestoreRepository.isJobBookmarkedByUser(email, id)
                _isBookmarked.value = isBookmarked
                val isApplied = firestoreRepository.isJobAppliedByUser(email, id)
                _isApplied.value = isApplied
            }
        }
    }

    fun toggleApplication(job: DocumentSnapshot) {
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser()
            currentUser?.email?.let { email ->
                if (_isApplied.value == true) {
                    val success = firestoreRepository.deleteApplication(email, job.id)
                    if (success) _isApplied.value = false
                } else {
                    val user = firestoreRepository.getUserByEmail(email)
                    if (user != null) {
                        val success = firestoreRepository.applyForJob(user, job)
                        if (success) _isApplied.value = true
                    }
                }
            }
        }
    }

    fun toggleBookmark(job: DocumentSnapshot) {
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser()
            currentUser?.email?.let { email ->
                if (_isBookmarked.value == true) {
                    val success = firestoreRepository.deleteBookmark(email, job.id)
                    if (success) _isBookmarked.value = false
                } else {
                    val user = firestoreRepository.getUserByEmail(email)
                    if (user != null) {
                        val success = firestoreRepository.addBookmark(user, job)
                        if (success) _isBookmarked.value = true
                    }
                }
            }
        }
    }
}