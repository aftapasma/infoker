package org.d3if.infoker.navigation

import org.d3if.infoker.ui.screen.user.KEY_JOB_ID

sealed class Screen(val route: String) {
    data object UserRegister: Screen("userRegisterScreen")
    data object CompanyRegister: Screen("companyRegisterScreen")
    data object Login: Screen("loginScreen")
    data object JobList: Screen("jobListScreen")
    data object JobDetail: Screen("jobDetailScreen/{$KEY_JOB_ID}") {
        fun withId(id: String) = "jobDetailScreen/$id"
    }
    data object AddJob: Screen("AddJobScreen")
    data object Activity: Screen("ActivityScreen")
    data object Profile: Screen("ProfileScreen")
    data object Home: Screen("HomeScreen")
    data object List: Screen("ListScreen")
    data object CompanyProfile: Screen("CompanyProfileScreen")
    data object ApplicantList: Screen("ApplicantListScreen/{$KEY_JOB_ID}") {
        fun withId(jobId: String) = "ApplicantListScreen/$jobId"
    }
}