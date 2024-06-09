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
    var name: String? by mutableStateOf(null)
        private set

    var phone: String? by mutableStateOf(null)
        private set

    var address: String? by mutableStateOf(null)
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

    fun saveProfileData(name: String, phone: String, address: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val userEmail = authRepository.getCurrentUser()?.email ?: return
        val profileData = mapOf(
            "name" to name,
            "phone" to phone,
            "address" to address
        )

        viewModelScope.launch {
            try {
                firestoreRepository.saveProfileData(userEmail, profileData)
                onSuccess()
            } catch (e: Exception) {
                onFailure()
            }
        }
    }

    fun fetchProfileData(onSuccess: () -> Unit, onFailure: () -> Unit) {
        val userEmail = authRepository.getCurrentUser()?.email ?: return

        firestoreRepository.getProfileData(userEmail, { profileData ->
            name = profileData["name"] as? String
            phone = profileData["phone"] as? String
            address = profileData["address"] as? String
            onSuccess()
        }, onFailure)
    }
}