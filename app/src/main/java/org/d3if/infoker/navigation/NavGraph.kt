package org.d3if.infoker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.AuthResult
import org.d3if.infoker.repository.FirestoreRepository
import org.d3if.infoker.ui.screen.RegisterScreen
import org.d3if.infoker.ui.screen.AddJobScreen
import org.d3if.infoker.ui.screen.AuthViewModel
import org.d3if.infoker.ui.screen.JobDetailScreen
import org.d3if.infoker.ui.screen.JobListScreen
import org.d3if.infoker.ui.screen.KEY_JOB_ID
import org.d3if.infoker.ui.screen.LoginScreen
import org.d3if.infoker.util.AuthViewModelFactory

@Composable
fun SetUpNavGraph(navController: NavHostController = rememberNavController()) {
    val authRepository = AuthRepository()
    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
    val authViewModel: AuthViewModel =
        viewModel(factory = AuthViewModelFactory(authRepository, firestoreRepository))

    val authResult by authViewModel.authResult.observeAsState()

    NavHost(
        navController = navController,
        startDestination = if (authResult is AuthResult.Success && (authResult as AuthResult.Success).data != null) Screen.JobList.route else Screen.Login.route
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
    }

    LaunchedEffect(authResult) {
        if (authResult is AuthResult.Success && (authResult as AuthResult.Success).data != null) {
            navController.navigate(Screen.JobList.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        } else {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.JobList.route) { inclusive = true }
            }
        }
    }
}