package org.d3if.infoker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.Timestamp
import org.d3if.infoker.R
import org.d3if.infoker.navigation.Screen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val dummyAcceptedApplicants = listOf(
    mapOf(
        "name" to "Alice",
        "jobTitle" to "Software Engineer",
        "company" to "Tech Corp",
        "location" to "Jakarta",
        "salary" to 12000000.0,
        "createdAt" to Date()
    ),
    mapOf(
        "name" to "Bob",
        "jobTitle" to "Data Analyst",
        "company" to "Data Inc.",
        "location" to "Bandung",
        "salary" to 10000000.0,
        "createdAt" to Date()
    )
)

val dummyRejectedApplicants = listOf(
    mapOf(
        "name" to "Charlie",
        "jobTitle" to "Product Manager",
        "company" to "Productive",
        "location" to "Surabaya",
        "salary" to 15000000.0,
        "createdAt" to Date()
    ),
    mapOf(
        "name" to "David",
        "jobTitle" to "UX Designer",
        "company" to "Design Studio",
        "location" to "Bali",
        "salary" to 9000000.0,
        "createdAt" to Date()
    )
)

@Composable
fun ApplicantDetailListScreen(navController: NavHostController? = null) {
    Scaffold(
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
            ApplicantDetailList(navController = navController, modifier = Modifier.padding(paddingValues))
        }
    )
}

@Composable
fun ApplicantDetailList(navController: NavHostController?, modifier: Modifier = Modifier) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Diterima", "Ditolak")
    val pagerState = rememberPagerState {
        tabs.size
    }
    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }

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
                    selected = index == selectedTabIndex,
                    onClick = { selectedTabIndex = index },
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(title, style = MaterialTheme.typography.titleLarge)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when (selectedTabIndex) {
                0 -> ApplicantAccept(navController)
                1 -> ApplicantRejected(navController)
            }
        }
    }
}

@Composable
fun ApplicantAccept(navController: NavHostController?) {
    val pattern = "dd MMM yyyy"
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())

    LazyColumn {
        if (dummyAcceptedApplicants.isEmpty()) {
            item {
                Text("No bookmarks found.")
            }
        } else {
            items(dummyAcceptedApplicants) { document ->
                val name = document["name"] as? String ?: "N/A"
                val jobTitle = document["jobTitle"] as? String ?: "N/A"
                val company = document["company"] as? String ?: "N/A"
                val location = document["location"] as? String ?: "N/A"
                val salary = document["salary"] as? Double ?: 0.0
                val createdAt = document["createdAt"] as? Date ?: Date()

                JobApplicationItem(
                    name = name,
                    jobTitle = jobTitle,
                    company = company,
                    location = location,
                    salary = salary,
                    createdAt = createdAt,
                    onClick = { navController?.navigate(Screen.JobDetail.withId(name)) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ApplicantRejected(navController: NavHostController?) {
    val pattern = "dd MMM yyyy"
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())

    LazyColumn {
        if (dummyRejectedApplicants.isEmpty()) {
            item {
                Text("No applications found.")
            }
        } else {
            items(dummyRejectedApplicants) { document ->
                val name = document["name"] as? String ?: "N/A"
                val jobTitle = document["jobTitle"] as? String ?: "N/A"
                val company = document["company"] as? String ?: "N/A"
                val location = document["location"] as? String ?: "N/A"
                val salary = document["salary"] as? Double ?: 0.0
                val createdAt = document["createdAt"] as? Date ?: Date()

                JobApplicationItem(
                    name = name,
                    jobTitle = jobTitle,
                    company = company,
                    location = location,
                    salary = salary,
                    createdAt = createdAt,
                    onClick = { navController?.navigate(Screen.JobDetail.withId(name)) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun JobApplicationItem(
    name: String,
    jobTitle: String,
    company: String,
    location: String,
    salary: Double,
    createdAt: Date,
    onClick: () -> Unit
) {
    val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(createdAt)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        Column (
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = name, style = MaterialTheme.typography.titleLarge)
            Text(text = jobTitle, style = MaterialTheme.typography.titleMedium)
            Text(text = company, style = MaterialTheme.typography.titleMedium)
            Text(text = location, style = MaterialTheme.typography.titleSmall)
            Text(text = stringResource(id = R.string.salary_format, salary), style = MaterialTheme.typography.titleSmall)
            Text(text = formattedDate, style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApplicantDetailListScreen() {
    ApplicantDetailListScreen()
}
