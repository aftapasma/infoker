package org.d3if.infoker.navigation

import ProfilScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository
import org.d3if.infoker.ui.screen.CompanyRegiterScreen
import org.d3if.infoker.ui.screen.LoginScreen
import org.d3if.infoker.ui.screen.UserRegiterScreen
import org.d3if.infoker.ui.screen.component.LoadingScreen
import org.d3if.infoker.ui.screen.perusahaan.AddOrEditJobScreen
import org.d3if.infoker.ui.screen.perusahaan.ApplicantDetailScreen
import org.d3if.infoker.ui.screen.perusahaan.ApplicantListScreen
import org.d3if.infoker.ui.screen.perusahaan.KEY_APPLICANT_ID
import org.d3if.infoker.ui.screen.perusahaan.KEY_COMPANYJOB_ID
import org.d3if.infoker.ui.screen.perusahaan.KEY_EDITJOB_ID
import org.d3if.infoker.ui.screen.perusahaan.tabs.HomeScreen
import org.d3if.infoker.ui.screen.perusahaan.tabs.ListScreen
import org.d3if.infoker.ui.screen.user.ActivityScreen
import org.d3if.infoker.ui.screen.user.JobDetailScreen
import org.d3if.infoker.ui.screen.user.JobListScreen
import org.d3if.infoker.ui.screen.user.KEY_JOB_ID
import org.d3if.infoker.ui.screen.user.Profile2
import org.d3if.infoker.ui.screen.user.ProfileDetail

@Composable
fun SetUpNavGraph(navController: NavHostController = rememberNavController()) {
    val authRepository = AuthRepository()
    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())

    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val user = authRepository.getCurrentUser()
        if (user != null) {
            val role = firestoreRepository.getUserRoleByEmail(user.email!!)
            startDestination = if (role == "company") Screen.Home.route else Screen.JobList.route
        } else {
            startDestination = Screen.JobList.route
        }
    }

    if (startDestination == null) {
        // Replace with your actual loading screen composable if available
        // You can create a simple LoadingScreen composable for this purpose
        LoadingScreen()
    } else {
        NavHost(
            navController = navController,
            startDestination = startDestination!!
        ) {
            composable(route = Screen.UserRegister.route) {
                UserRegiterScreen(navController)
            }
            composable(route = Screen.CompanyRegister.route) {
                CompanyRegiterScreen(navController)
            }
            composable(route = Screen.Login.route) {
                LoginScreen(navController)
            }
            composable(route = Screen.AddJob.route) {
                AddOrEditJobScreen(navController)
            }
            composable(route = Screen.EditJob.route, arguments = listOf(
                navArgument(KEY_EDITJOB_ID) { type = NavType.StringType }
            )
            ) { navBackStackEntry ->
                val id = navBackStackEntry.arguments?.getString(KEY_EDITJOB_ID)
                AddOrEditJobScreen(navController, id)
            }
            composable(route = Screen.JobList.route) {
                JobListScreen(navController)
            }
            composable(route = Screen.JobDetail.route,
                arguments = listOf(
                    navArgument(KEY_JOB_ID) { type = NavType.StringType }
                )
            ) { navBackStackEntry ->
                val id = navBackStackEntry.arguments?.getString(KEY_JOB_ID)
                JobDetailScreen(navController = navController, id = id)
            }
            composable(route = Screen.Activity.route) {
                ActivityScreen(navController)
            }
            composable(route = Screen.Profile.route) {
                Profile2(navController)
            }
            composable(route = Screen.Home.route) {
                HomeScreen(navController)
            }
            composable(route = Screen.List.route) {
                ListScreen(navController)
            }
            composable(route = Screen.CompanyProfile.route) {
                ProfilScreen(navController)
            }
            composable(route = Screen.ApplicantList.route,
                arguments = listOf(
                    navArgument(KEY_COMPANYJOB_ID) { type = NavType.StringType }
                )
            ) { navBackStackEntry ->
                val jobId = navBackStackEntry.arguments?.getString(KEY_COMPANYJOB_ID)
                ApplicantListScreen(navController = navController, jobId = jobId)
            }
            composable(route = Screen.ApplicantDetail.route,
                arguments = listOf(
                    navArgument(KEY_APPLICANT_ID) { type = NavType.StringType }
                )
            ) { navBackStackEntry ->
                val applicantId = navBackStackEntry.arguments?.getString(KEY_APPLICANT_ID)
                ApplicantDetailScreen(navController = navController, applicantId = applicantId)
            }
            composable(route = Screen.ProfilDetail.route) {
                ProfileDetail(navController)
            }
        }
    }
}