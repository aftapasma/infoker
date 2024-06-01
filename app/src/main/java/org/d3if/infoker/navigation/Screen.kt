package org.d3if.infoker.navigation

import org.d3if.infoker.ui.screen.KEY_JOB_ID

sealed class Screen(val route: String) {
    data object Register: Screen("registerScreen")
    data object Login: Screen("loginScreen")
    data object JobList: Screen("jobListScreen")
    data object JobDetail: Screen("jobDetailScreen/{$KEY_JOB_ID}") {
        fun withId(id: String) = "jobDetailScreen/$id"
    }
    data object AddJob: Screen("AddJobScreen")
    data object Activity: Screen("ActivityScreen")
}