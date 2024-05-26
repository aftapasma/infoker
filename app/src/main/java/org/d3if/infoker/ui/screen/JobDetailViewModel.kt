package org.d3if.infoker.ui.screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch
import org.d3if.infoker.repository.FirestoreRepository

class JobDetailViewModel(private val repository: FirestoreRepository) : ViewModel() {
    private val _jobDetail = MutableLiveData<DocumentSnapshot?>()
    val jobDetail: LiveData<DocumentSnapshot?> get() = _jobDetail

    fun getJobById(id: String) {
        viewModelScope.launch {
            val job = repository.getJobById(id)
            Log.d("JobDetailViewModel", "Fetched job: $job")
            _jobDetail.value = job
        }
    }
}