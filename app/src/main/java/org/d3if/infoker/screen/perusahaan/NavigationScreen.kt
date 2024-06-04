package org.d3if.infoker.screen.perusahaan

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.d3if.infoker.screen.perusahaan.nav.NavItem
import org.d3if.infoker.screen.perusahaan.tabs.HomeScreen
import org.d3if.infoker.screen.perusahaan.tabs.ListScreen
import org.d3if.infoker.screen.perusahaan.tabs.ProfileScreen

@Composable
fun NavigationScreen(navController: NavHostController) {
    NavHost(navController, startDestination = NavItem.Home.path) {
        composable(NavItem.Home.path) { HomeScreen() }
        composable(NavItem.List.path) { ListScreen() }
        composable(NavItem.Profile.path) { ProfileScreen() }
    }
}