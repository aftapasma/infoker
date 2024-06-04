package org.d3if.infoker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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

@Composable
fun SetUpNavGraph(navController: NavHostController = rememberNavController()) {
//    val authRepository = AuthRepository()
//    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
//    val authViewModel: AuthViewModel =
//        viewModel(factory = AuthViewModelFactory(authRepository, firestoreRepository))

//    val authResult by authViewModel.authResult.observeAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.JobList.route
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