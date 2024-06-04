package org.d3if.infoker.ui.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.d3if.infoker.R
import org.d3if.infoker.navigation.Screen

@Composable
fun UserBottomBar(navController: NavHostController) {
    BottomAppBar(
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { navController.navigate(Screen.JobList.route) },
                    modifier = Modifier
                        .size(48.dp)
                        .weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Home"
                    )
                }
                IconButton(
                    onClick = { navController.navigate(Screen.Activity.route) },
                    modifier = Modifier
                        .size(48.dp)
                        .weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_bookmark_border_24),
                        contentDescription = "Bookmark"
                    )
                }
                IconButton(
                    onClick = { navController.navigate(Screen.Profile.route) },
                    modifier = Modifier
                        .size(48.dp)
                        .weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Account"
                    )
                }
            }
        }
    )
}