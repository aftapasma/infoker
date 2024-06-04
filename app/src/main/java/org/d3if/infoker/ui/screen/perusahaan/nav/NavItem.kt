package org.d3if.infoker.ui.screen.perusahaan.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(icon: ImageVector?) {
    object Home :
        Item(route = NavPath.HOME.toString(), title = NavTitle.HOME, icon = Icons.Default.Home)

    object List :
        Item(route = NavPath.LIST.toString(), title = NavTitle.LIST, icon = Icons.Default.List)

    object Profile :
        Item(
            route = NavPath.PROFILE.toString(),
            title = NavTitle.PROFILE,
            icon = Icons.Default.Person
        )

    object AddJob :
            Item(route = NavPath.ADDJOB.toString(), title = NavTitle.ADDJOB, icon = null)

    object Login :
        Item(route = NavPath.LOGIN.toString(), title = NavTitle.LOGIN, icon = null)

    object Register :
            Item(route = NavPath.REGISTER.toString(), title = NavTitle.REGISTER, icon = null)

    object JobList :
            Item(route = NavPath.JOBLIST.toString(), title = NavTitle.JOBLIST, icon = null)

//    object JobDetail :
//            Item(route = NavPath.JOBDETAIL.toString(), title = NavTitle.JOBDETAIL, icon = null)

    object Activity :
            Item(route = NavPath.ACTIVITY.toString(), title = NavTitle.ACTIVITY, icon = null)

}