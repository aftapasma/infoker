package org.d3if.infoker.ui.screen.user

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if.infoker.navigation.Screen
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository
import org.d3if.infoker.ui.screen.component.UserBottomBar
import org.d3if.infoker.ui.theme.InfokerTheme
import org.d3if.infoker.util.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(navController: NavHostController) {
    val authRepository = AuthRepository()

    val currentUser = authRepository.getCurrentUser()

    if (currentUser == null) {
        navController.navigate(Screen.Login.route)
    }

    BackHandler {
        navController.navigate(Screen.JobList.route)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Daftar Pekerjaan",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            UserBottomBar(navController = navController)
        },
        content = { paddingValues ->
            MyApplication(navController = navController, modifier = Modifier.padding(paddingValues))
        }
    )
}

@Composable
fun MyApplication(navController: NavHostController, modifier: Modifier = Modifier) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Disimpan", "Dilamar")
    val pagerState = rememberPagerState {
        tabs.size
    }
    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }

    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
    val authRepository = AuthRepository()
    val activityViewModel: ActivityViewModel = viewModel(factory = ViewModelFactory(authRepository, firestoreRepository))

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
                    Text(title)
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
                0 -> SavedApplicationsList(navController, activityViewModel)
                1 -> AppliedApplicationsList(navController, activityViewModel)
            }
        }
    }
}

@Composable
fun SavedApplicationsList(navController: NavHostController, viewModel: ActivityViewModel) {
    val bookmarks by viewModel.getBookmarksForCurrentUser().collectAsState(initial = emptyList())
    val pattern = "dd MMM yyyy"
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())

    LazyColumn {
        if (bookmarks.isEmpty()) {
            item {
                Text("No bookmarks found.")
            }
        } else {
            items(bookmarks) { document ->
                val jobData = document.get("job") as? Map<*, *>
                val jobId = jobData?.get("id") as? String ?: "N/A"
                val title = jobData?.get("title") as? String ?: "N/A"
                val company = jobData?.get("company") as? String ?: "Afta Tunas Jaya Abadi Tbk."
                val timestamp = jobData?.get("createdAt") as? Timestamp
                val date = timestamp?.toDate()?.let { dateFormat.format(it) } ?: "N/A"
                val location = jobData?.get("location") as? String ?: "N/A"

                JobApplicationItem(
                    title = title,
                    company = company,
                    date = date,
                    location = location,
                    onClick = { navController.navigate(Screen.JobDetail.withId(jobId)) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun AppliedApplicationsList(navController: NavHostController, viewModel: ActivityViewModel) {
    val applications by viewModel.getApplicationsForCurrentUser().collectAsState(initial = emptyList())
    val pattern = "dd MMM yyyy"
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())

    LazyColumn {
        if (applications.isEmpty()) {
            item {
                Text("No applications found.")
            }
        } else {
            items(applications) { document ->
                val jobData = document.get("job") as? Map<*, *>
                val jobId = jobData?.get("id") as? String ?: "N/A"
                val title = jobData?.get("title") as? String ?: "N/A"
                val company = jobData?.get("company") as? String ?: "Afta Tunas Jaya Abadi Tbk."
                val timestamp = jobData?.get("createdAt") as? Timestamp
                val date = timestamp?.toDate()?.let { dateFormat.format(it) } ?: "N/A"
                val location = jobData?.get("location") as? String ?: "N/A"
                val status = document.get("status").toString()
                val statusDate = "10 Jan 2023"

                JobApplicationItem(
                    title = title,
                    company = company,
                    date = date,
                    location = location,
                    status = status,
                    statusDate = statusDate,
                    onClick = { navController.navigate(Screen.JobDetail.withId(jobId)) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun JobApplicationItem(
    title: String,
    company: String,
    date: String,
    location: String,
    status: String? = null,
    statusDate: String? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
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
                HorizontalDivider()
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
        ActivityScreen(rememberNavController())
    }
}
