package org.d3if.infoker.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepository {
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun signInWithEmail(email: String, password: String): LiveData<AuthResult<FirebaseUser>> {
        val resultLiveData = MutableLiveData<AuthResult<FirebaseUser>>()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resultLiveData.value = AuthResult.Success(firebaseAuth.currentUser)
                } else {
                    resultLiveData.value = AuthResult.Error(task.exception)
                }
            }
        return resultLiveData
    }

    fun registerWithEmail(email: String, password: String): LiveData<AuthResult<FirebaseUser>> {
        val resultLiveData = MutableLiveData<AuthResult<FirebaseUser>>()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resultLiveData.value = AuthResult.Success(firebaseAuth.currentUser)
                } else {
                    resultLiveData.value = AuthResult.Error(task.exception)
                }
            }
        return resultLiveData
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}

sealed class AuthResult<out T> {
    data class Success<out T>(val data: T?) : AuthResult<T>()
    data class Error(val exception: Exception?) : AuthResult<Nothing>()
}