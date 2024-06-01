package org.d3if.infoker.ui.screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.flow
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository

class ActivityViewModel(private val authRepository: AuthRepository, private val firestoreRepository: FirestoreRepository) : ViewModel() {
    fun getApplicationsForCurrentUser() = flow {
        val currentUser = authRepository.getCurrentUser()
        currentUser?.email?.let { email ->
            val applications = firestoreRepository.getApplicationsForCurrentUser(email)
            emit(applications)
        } ?: emit(emptyList())
    }

    fun getBookmarksForCurrentUser() = flow {
        val currentUser = authRepository.getCurrentUser()
        currentUser?.email?.let { email ->
            val bookmarks = firestoreRepository.getBookmarksForCurrentUser(email)
            emit(bookmarks)
        } ?: emit(emptyList())
    }
}