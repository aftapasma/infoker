package org.d3if.infoker.ui.screen.perusahaan.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import org.d3if.infoker.repository.FirestoreRepository

class ListViewModel(private val firestoreRepository: FirestoreRepository) : ViewModel() {
    fun getAcceptedApplicationsByCompany(email: String) = liveData {
        val acceptedApplications = firestoreRepository.getAcceptedApplicationsByCompany(email)
        emit(acceptedApplications)
    }

    fun getRejectedApplicationsByCompany(email: String) = liveData {
        val rejectedApplications = firestoreRepository.getRejectedApplicationsByCompany(email)
        emit(rejectedApplications)
    }
}