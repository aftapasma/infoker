package org.d3if.infoker.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if.infoker.R
import org.d3if.infoker.navigation.Screen
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository
import org.d3if.infoker.ui.theme.InfokerTheme
import org.d3if.infoker.util.JobViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(navController: NavHostController) {
    val authRepository = AuthRepository()

    val currentUser = authRepository.getCurrentUser()

    if (currentUser == null) {
        navController.navigate(Screen.Login.route)
    }

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
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Disimpan", "Dilamar")

    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
    val authRepository = AuthRepository()
    val activityViewModel: ActivityViewModel = viewModel(factory = JobViewModelFactory(authRepository, firestoreRepository))

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
            1 -> AppliedApplicationsList(viewModel = activityViewModel)
        }
    }
}

@Composable
fun SavedApplicationsList(viewModel: ActivityViewModel = viewModel()) {
    val bookmarks by viewModel.getBookmarksForCurrentUser().collectAsState(initial = emptyList())

    Column {
        if (bookmarks.isEmpty()) {
            Text("No bookmarks found.")
        } else {
            bookmarks.forEach { document ->
                val jobData = document.get("job") as? Map<*, *>
                val title = jobData?.get("title") as? String ?: "N/A"
                val company = jobData?.get("company") as? String ?: "Afta Tunas Jaya Abadi Tbk."
                val date = document.getString("date") ?: "N/A"
                val location = jobData?.get("location") as? String ?: "N/A"

                JobApplicationItem(
                    title = title,
                    company = company,
                    date = date,
                    location = location
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun AppliedApplicationsList(viewModel: ActivityViewModel = viewModel()) {
    val applications by viewModel.getApplicationsForCurrentUser().collectAsState(initial = emptyList())

    Column {
        if (applications.isEmpty()) {
            Text("No applications found.")
        } else {
            applications.forEach { document ->
                val jobData = document.get("job") as? Map<*, *>
                val title = jobData?.get("title") as? String ?: "N/A"
                val company = jobData?.get("company") as? String ?: "Afta Tunas Jaya Abadi Tbk."
                val date = document.getString("date") ?: "N/A"
                val location = jobData?.get("location") as? String ?: "N/A"
                val status = "Dilamar di situs perusahaan"
                val statusDate = "10 Jan 2023"

                JobApplicationItem(
                    title = title,
                    company = company,
                    date = date,
                    location = location,
                    status = status,
                    statusDate = statusDate
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
        ActivityScreen(rememberNavController())
    }
}