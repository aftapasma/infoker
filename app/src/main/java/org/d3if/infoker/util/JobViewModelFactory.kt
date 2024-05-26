package org.d3if.infoker.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if.infoker.repository.FirestoreRepository
import org.d3if.infoker.ui.screen.AddJobViewModel
import org.d3if.infoker.ui.screen.JobDetailViewModel
import org.d3if.infoker.ui.screen.JobListViewModel

class JobViewModelFactory(private val repository: FirestoreRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AddJobViewModel::class.java) -> {
                AddJobViewModel(repository) as T
            }
            modelClass.isAssignableFrom(JobListViewModel::class.java) -> {
                JobListViewModel(repository) as T
            }
            modelClass.isAssignableFrom(JobDetailViewModel::class.java) -> {
                JobDetailViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}