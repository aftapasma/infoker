package org.d3if.infoker.ui.screen.perusahaan.tabs

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if.infoker.R
import org.d3if.infoker.navigation.Screen
import org.d3if.infoker.ui.screen.component.CompanyBottomBar
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavHostController) {
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
        content = { paddingValues ->
            ApplicantDetailList(navController = navController, modifier = Modifier.padding(paddingValues))
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
            }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.hapus),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        bottomBar = {
            CompanyBottomBar(navController = navController)
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
                    Text(
                        title, style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
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
    var checked by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column (
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = name, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = jobTitle,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(text = company, style = MaterialTheme.typography.titleMedium)
                Text(text = location, style = MaterialTheme.typography.titleSmall)
                Text(text = stringResource(id = R.string.salary_format, salary), style = MaterialTheme.typography.titleSmall)
                Text(text = formattedDate, style = MaterialTheme.typography.titleSmall)
            }
            Checkbox(
                checked = checked,
                onCheckedChange = {checked = it},
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApplicantDetailListScreen() {
    ListScreen(rememberNavController())
}