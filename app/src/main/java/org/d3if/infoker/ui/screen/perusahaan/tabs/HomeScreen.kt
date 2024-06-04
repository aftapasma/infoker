package org.d3if.infoker.ui.screen.perusahaan.tabs

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.d3if.infoker.R
import org.d3if.infoker.ui.theme.InfokerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.tambah_pekerjaan),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
//        bottomBar = {
//            BottomAppBar(
//                content = {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Center
//                    ) {
//                        IconButton(
//                            onClick = { },
//                            modifier = Modifier
//                                .size(48.dp)
//                                .weight(1f)
//                        ) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.baseline_bookmark_border_24),
//                                contentDescription = "Bookmark"
//                            )
//                        }
//                        IconButton(
//                            onClick = { },
//                            modifier = Modifier
//                                .size(48.dp)
//                                .weight(1f)
//                        ) {
//                            Icon(
//                                imageVector = Icons.Filled.Home,
//                                contentDescription = "Home"
//                            )
//                        }
//                        IconButton(
//                            onClick = { },
//                            modifier = Modifier
//                                .size(48.dp)
//                                .weight(1f)
//                        ) {
//                            Icon(
//                                imageVector = Icons.Filled.AccountCircle,
//                                contentDescription = "Account"
//                            )
//                        }
//                    }
//                },
//                containerColor = MaterialTheme.colorScheme.primaryContainer,
//
//            )
//        },
        content = { paddingValues ->
//            MyApplication( modifier = Modifier.padding(paddingValues))
        }
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewHomeScreen() {
    InfokerTheme {
        HomeScreen()
    }
}