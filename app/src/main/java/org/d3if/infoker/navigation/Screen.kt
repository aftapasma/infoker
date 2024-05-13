package org.d3if.infoker.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object JobDetail: Screen("jobDetailScreen")
}