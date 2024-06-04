package org.d3if.infoker.ui.screen.component

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if.infoker.navigation.Screen
import org.d3if.infoker.ui.screen.user.JobListScreen
import org.d3if.infoker.ui.theme.InfokerTheme

@Composable
fun CompanyBottomBar(navController: NavHostController) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

    NavigationBar {
//        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                alwaysShowLabel = true,
                label = { Text(text = "home") },
                icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "home") },
                selected = selectedItem == 0,
                onClick = {
                    selectedItem = 0
                    navController.navigate(Screen.Home.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        NavigationBarItem(
            alwaysShowLabel = true,
            icon = { Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = "list")},
            label = { Text(text = "list") },
            selected = selectedItem == 1,
            onClick = {
                selectedItem = 1
                navController.navigate(Screen.List.route) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) { saveState = true }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            alwaysShowLabel = true,
            icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "profile") },
            label = { Text(text = "profile") },
            selected = selectedItem == 2,
            onClick = {
                selectedItem = 2
                navController.navigate(Screen.CompanyProfile.route) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) { saveState = true }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
//        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCompanyBottomBar() {
    InfokerTheme {
        CompanyBottomBar(rememberNavController())
    }
}
