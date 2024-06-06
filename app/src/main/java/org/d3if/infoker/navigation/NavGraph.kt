package org.d3if.infoker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository
import org.d3if.infoker.ui.screen.AuthViewModel
import org.d3if.infoker.ui.screen.user.ActivityScreen
import org.d3if.infoker.ui.screen.perusahaan.AddJobScreen
import org.d3if.infoker.ui.screen.user.JobDetailScreen
import org.d3if.infoker.ui.screen.user.JobListScreen
import org.d3if.infoker.ui.screen.user.KEY_JOB_ID
import org.d3if.infoker.ui.screen.LoginScreen
import org.d3if.infoker.ui.screen.RegisterScreen
import org.d3if.infoker.ui.screen.perusahaan.tabs.HomeScreen
import org.d3if.infoker.ui.screen.perusahaan.tabs.ListScreen
import org.d3if.infoker.ui.screen.user.ProfilScreen
import org.d3if.infoker.ui.screen.user.Profile2
import org.d3if.infoker.util.AuthViewModelFactory

@Composable
fun SetUpNavGraph(navController: NavHostController = rememberNavController()) {
    val authRepository = AuthRepository()
    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
    val authViewModel: AuthViewModel =
        viewModel(factory = AuthViewModelFactory(authRepository, firestoreRepository))

    var startDestination by remember { mutableStateOf(Screen.JobList.route) }

    LaunchedEffect(Unit) {
        val user = authRepository.getCurrentUser()
        if (user != null) {
            val role = firestoreRepository.getUserRoleByEmail(user.email!!)
            startDestination = if (role == "company") Screen.Home.route else Screen.JobList.route
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(route = Screen.AddJob.route) {
            AddJobScreen(navController)
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
            ProfilScreen()
        }
    }

//    LaunchedEffect(authResult) {
//        if (authResult is AuthResult.Success && (authResult as AuthResult.Success).data != null) {
//            navController.navigate(Screen.JobList.route) {
//                popUpTo(Screen.Login.route) { inclusive = true }
//            }
//        } else {
//            navController.navigate(Screen.Login.route) {
//                popUpTo(Screen.JobList.route) { inclusive = true }
//            }
//        }
//    }
}