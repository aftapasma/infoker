package org.d3if.infoker.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.d3if.infoker.R
import org.d3if.infoker.ui.theme.InfokerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.activity),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = { },
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
                            onClick = { },
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
                            onClick = { },
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
        },
        content = { paddingValues ->
            MyApplication(modifier = Modifier.padding(paddingValues))
        }
    )
}

@Composable
fun MyApplication(modifier: Modifier = Modifier) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Disimpan", "Dilamar")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            contentColor = Color.Black,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(title)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        when (selectedTabIndex) {
            0 -> SavedApplicationsList()
            1 -> AppliedApplicationsList()
        }
    }
}

@Composable
fun SavedApplicationsList() {
    Column {
        JobApplicationItem(
            title = "Senior Financial Accountant - 12 Month Contract",
            company = "SEEK Asia (Jobstreet)",
            date = "15 Dec 2022",
            location = "Jakarta"
        )
        Spacer(modifier = Modifier.height(8.dp))
        JobApplicationItem(
            title = "Pricing Analyst",
            company = "SEEK Asia (Jobstreet)",
            date = "5 Jan 2023",
            location = "Jakarta"
        )
    }
}

@Composable
fun AppliedApplicationsList() {
    Column {
        JobApplicationItem(
            title = "Software Engineer",
            company = "Tech Company",
            date = "1 Jan 2023",
            location = "Bandung",
            status = "Dilamar di situs perusahaan\nDilihat oleh perusahaan",
            statusDate = "10 Jan 2023"
        )
        Spacer(modifier = Modifier.height(8.dp))
        JobApplicationItem(
            title = "Data Scientist",
            company = "Data Corp",
            date = "10 Feb 2023",
            location = "Surabaya",
            status = "Dilamar",
            statusDate = "15 Feb 2023"
        )
    }
}

@Composable
fun JobApplicationItem(
    title: String,
    company: String,
    date: String,
    location: String,
    status: String? = null,
    statusDate: String? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = company,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = date,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = location,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (status != null && statusDate != null) {
                Divider()
                Text(
                    text = status,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = statusDate,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewActivityScreen() {
    InfokerTheme {
        ActivityScreen()
    }
}
