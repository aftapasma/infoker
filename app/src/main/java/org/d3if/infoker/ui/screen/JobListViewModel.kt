package org.d3if.infoker.ui.screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch
import org.d3if.infoker.repository.FirestoreRepository

class JobListViewModel(private val firestoreRepository: FirestoreRepository) : ViewModel() {
    private val _jobs = MutableLiveData<List<DocumentSnapshot>>()
    val jobs: LiveData<List<DocumentSnapshot>> get() = _jobs

    init {
        getJobs()
    }

    private fun getJobs() {
        viewModelScope.launch {
            val fetchedJobs = firestoreRepository.getJobs()
            Log.d("JobListViewModel", "Fetched jobs: $fetchedJobs")
            _jobs.value = fetchedJobs
        }
    }
    fun searchJobs(query: String) {
        val filteredJobs = if (query.isNotBlank()) {
            jobs.value?.filter { job ->
                val title = job.getString("title") ?: ""
                title.contains(query, ignoreCase = true)
            } ?: emptyList()
        } else {
            jobs.value ?: emptyList()
        }
        _jobs.value = filteredJobs
    }
}
