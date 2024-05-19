package org.d3if.infoker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.d3if.infoker.ui.screen.JobDetailScreen
import org.d3if.infoker.ui.screen.JobListScreen

@Composable
fun SetUpNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            JobListScreen()
        }
        composable(route = Screen.JobDetail.route) {
            JobDetailScreen()
        }
    }
}