package org.d3if.infoker.ui.screen.perusahaan.tabs

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.d3if.infoker.model.UserProfile
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
    private val _companyProfile = MutableLiveData<UserProfile?>()
    val userProfile: LiveData<UserProfile?> get() = _companyProfile

    init {
        // Initialize with some data
        _companyProfile.value = UserProfile(biodata = "")
    }

    fun fetchUserProfile(email: String) {
        viewModelScope.launch {
            val userDocument = firestoreRepository.getUserByEmail(email)
            _companyProfile.value = userDocument?.toObject(UserProfile::class.java)
        }
    }

    fun saveBiodataCompany(biodata: String) {
        val updatedProfile = _companyProfile.value?.copy(biodata = biodata)
        _companyProfile.value = updatedProfile

        viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            if (user != null) {
                val email = user.email
                if (email != null) {
                    val success = firestoreRepository.saveBiodataByEmail(email, biodata)
                    if (success) {
                        fetchUserProfile(email)
                        Log.d("AuthViewModel", "Biodata saved successfully")
                    } else {
                        Log.e("AuthViewModel", "Failed to save biodata")
                    }
                } else {
                    Log.e("AuthViewModel", "User email is null")
                }
            } else {
                Log.e("AuthViewModel", "User is not authenticated")
            }
        }
    }
}