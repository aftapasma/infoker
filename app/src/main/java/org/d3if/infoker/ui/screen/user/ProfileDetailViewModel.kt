package org.d3if.infoker.ui.screen.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository

class ProfileDetailViewModel(
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {
    var photoUrl by mutableStateOf<String?>(null)
        private set

    fun savePhotoUrl(photoUrl: String) {
        val user = authRepository.getCurrentUser()
        user?.let {
            viewModelScope.launch {
                firestoreRepository.savePhotoUrl(it.email ?: "", photoUrl)
                this@ProfileDetailViewModel.photoUrl = photoUrl
            }
        }
    }

    fun fetchPhotoUrl() {
        val user = authRepository.getCurrentUser()
        user?.let {
            viewModelScope.launch {
                val url = firestoreRepository.getPhotoUrl(it.email ?: "")
                photoUrl = url
            }
        }
    }
}