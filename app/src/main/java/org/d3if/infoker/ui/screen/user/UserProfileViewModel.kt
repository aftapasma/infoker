package org.d3if.infoker.ui.screen.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.d3if.infoker.model.UserProfile
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository

class UserProfileViewModel(
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _userProfile = MutableLiveData<UserProfile?>()
    val userProfile: LiveData<UserProfile?> get() = _userProfile

    init {
        // Initialize with some data
        _userProfile.value = UserProfile(biodata = "")
    }

    fun fetchUserProfile(email: String) {
        viewModelScope.launch {
            val userDocument = firestoreRepository.getUserByEmail(email)
            _userProfile.value = userDocument?.toObject(UserProfile::class.java)
        }
    }

    fun saveBiodata(biodata: String) {
        val updatedProfile = _userProfile.value?.copy(biodata = biodata)
        _userProfile.value = updatedProfile

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
