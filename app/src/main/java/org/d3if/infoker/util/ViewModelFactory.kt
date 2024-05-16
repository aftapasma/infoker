package org.d3if.infoker.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if.infoker.FirestoreRepository
import org.d3if.infoker.ui.screen.JobDetailViewModel

class ViewModelFactory(private val repository: FirestoreRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JobDetailViewModel::class.java)) {
            return JobDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unkown ViewModel Class")
    }
}