package org.d3if.infoker.ui.screen.perusahaan

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.d3if.infoker.ui.screen.perusahaan.nav.NavItem
import org.d3if.infoker.ui.screen.perusahaan.tabs.HomeScreen
import org.d3if.infoker.ui.screen.perusahaan.tabs.ListScreen
import org.d3if.infoker.ui.screen.perusahaan.tabs.ProfileScreen

@Composable
fun NavigationScreen(navController: NavHostController) {
    NavHost(navController, startDestination = NavItem.Home.route) {
        composable(NavItem.Home.route) { HomeScreen() }
        composable(NavItem.List.route) { ListScreen() }
        composable(NavItem.Profile.route) { ProfileScreen() }
    }
}