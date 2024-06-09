package org.d3if.infoker.ui.screen.perusahaan.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository

class ProfilViewModel(
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {
    fun savePhotoUrl(photoUrl: String) {
        val user = authRepository.getCurrentUser()
        user?.let {
            viewModelScope.launch {
                firestoreRepository.savePhotoUrl(it.email ?: "", photoUrl)
            }
        }
    }

    fun getPhotoUrl(callback: (String?) -> Unit) {
        val user = authRepository.getCurrentUser()
        user?.let {
            viewModelScope.launch {
                val photoUrl = firestoreRepository.getPhotoUrl(it.email ?: "")
                callback(photoUrl)
            }
        }
    }
}