package org.d3if.infoker.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository
import org.d3if.infoker.ui.screen.user.ActivityViewModel
import org.d3if.infoker.ui.screen.perusahaan.AddOrEditJobViewModel
import org.d3if.infoker.ui.screen.perusahaan.ApplicantDetailViewModel
import org.d3if.infoker.ui.screen.perusahaan.ApplicantListViewModel
import org.d3if.infoker.ui.screen.perusahaan.tabs.HomeViewModel
import org.d3if.infoker.ui.screen.perusahaan.tabs.ListViewModel
import org.d3if.infoker.ui.screen.perusahaan.tabs.ProfilViewModel
import org.d3if.infoker.ui.screen.user.JobDetailViewModel
import org.d3if.infoker.ui.screen.user.JobListViewModel
import org.d3if.infoker.ui.screen.user.ProfileDetailViewModel
import org.d3if.infoker.ui.screen.user.UserProfileViewModel

class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AddOrEditJobViewModel::class.java) -> {
                AddOrEditJobViewModel(authRepository, firestoreRepository) as T
            }
            modelClass.isAssignableFrom(JobListViewModel::class.java) -> {
                JobListViewModel(firestoreRepository) as T
            }
            modelClass.isAssignableFrom(JobDetailViewModel::class.java) -> {
                JobDetailViewModel(authRepository, firestoreRepository) as T
            }
            modelClass.isAssignableFrom(ActivityViewModel::class.java) -> {
                ActivityViewModel(authRepository, firestoreRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(authRepository, firestoreRepository) as T
            }
            modelClass.isAssignableFrom(ApplicantListViewModel::class.java) -> {
                ApplicantListViewModel(firestoreRepository) as T
            }
            modelClass.isAssignableFrom(ListViewModel::class.java) -> {
                ListViewModel(firestoreRepository) as T
            }
            modelClass.isAssignableFrom(ApplicantDetailViewModel::class.java) -> {
                ApplicantDetailViewModel(firestoreRepository) as T
            }
            modelClass.isAssignableFrom(UserProfileViewModel::class.java) -> {
                UserProfileViewModel(authRepository, firestoreRepository) as T
            }
            modelClass.isAssignableFrom(ProfilViewModel::class.java) -> {
                ProfilViewModel(authRepository, firestoreRepository) as T
            }
            modelClass.isAssignableFrom(ProfileDetailViewModel::class.java) -> {
                ProfileDetailViewModel(authRepository, firestoreRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
