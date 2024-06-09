package org.d3if.infoker.ui.screen.perusahaan.tabs

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if.infoker.R
import org.d3if.infoker.navigation.Screen
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavHostController) {
    val authRepository = AuthRepository()
    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
    val listViewModel = ListViewModel(firestoreRepository)

    val currentUser = authRepository.getCurrentUser()
    val userEmail = currentUser?.email ?: ""

    var refreshTrigger by remember { mutableStateOf(false) }
    var checkboxStates by remember { mutableStateOf(mutableMapOf<String, Boolean>()) }
    var selectedApplicationIds by remember { mutableStateOf(listOf<String>()) }

    val acceptedApplications by listViewModel.getAcceptedApplicationsByCompany(userEmail)
        .observeAsState(initial = emptyList())
    val rejectedApplications by listViewModel.getRejectedApplicationsByCompany(userEmail)
        .observeAsState(initial = emptyList())

    var showDialog by remember { mutableStateOf(false) }

    BackHandler {
        navController.navigate(Screen.Home.route)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        content = { paddingValues ->
            ApplicantDetailList(
                navController = navController,
                acceptedApplications = acceptedApplications,
                rejectedApplications = rejectedApplications,
                onApplicationSelected = { applicationId, isSelected ->
                    selectedApplicationIds = if (isSelected) {
                        selectedApplicationIds + applicationId
                    } else {
                        selectedApplicationIds - applicationId
                    }
                    checkboxStates[applicationId] = isSelected
                },
                checkboxStates = checkboxStates,
                modifier = Modifier.padding(paddingValues)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showDialog = true
            }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.hapus),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Konfirmasi") },
            text = { Text("Apakah Anda yakin ingin menghapus data ini?") },
            confirmButton = {
                Button(
                    onClick = {
                        listViewModel.deleteMarkedApplications(selectedApplicationIds)
                            .observeForever { success ->
                                if (success) {
                                    refreshTrigger = !refreshTrigger
                                    checkboxStates.clear()
                                }
                            }
                        showDialog = false
                    }) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    LaunchedEffect(refreshTrigger) {
        listViewModel.getAcceptedApplicationsByCompany(userEmail)
        listViewModel.getRejectedApplicationsByCompany(userEmail)
    }
}

@Composable
fun ApplicantDetailList(
    navController: NavHostController,
    acceptedApplications: List<DocumentSnapshot>,
    rejectedApplications: List<DocumentSnapshot>,
    onApplicationSelected: (String, Boolean) -> Unit,
    checkboxStates: Map<String, Boolean>,
    modifier: Modifier = Modifier
) {
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
                0 -> ApplicantList(
                    navController,
                    acceptedApplications,
                    onApplicationSelected,
                    checkboxStates
                )

                1 -> ApplicantList(
                    navController,
                    rejectedApplications,
                    onApplicationSelected,
                    checkboxStates
                )
            }
        }
    }
}

@Composable
fun ApplicantList(
    navController: NavHostController,
    applications: List<DocumentSnapshot>,
    onApplicationSelected: (String, Boolean) -> Unit,
    checkboxStates: Map<String, Boolean>
) {
    LazyColumn {
        if (applications.isEmpty()) {
            item {
                Text("No applications found.")
            }
        } else {
            items(applications) { document ->
                val userMap = document["user"] as? Map<String, Any> ?: emptyMap()
                val jobMap = document["job"] as? Map<String, Any> ?: emptyMap()
                val name = userMap["name"] as? String ?: "N/A"
                val jobTitle = jobMap["title"] as? String ?: "N/A"
                val company = jobMap["createdBy.name"] as? String ?: "N/A"
                val location = jobMap["location"] as? String ?: "N/A"
                val salary = jobMap["salary"] as? Double ?: 0.0
                val createdAt =
                    (document["createdAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date()

                JobApplicationItem(
                    applicationId = document.id,
                    name = name,
                    jobTitle = jobTitle,
                    company = company,
                    location = location,
                    salary = salary,
                    createdAt = createdAt,
                    onClick = { },
                    onCheckedChange = onApplicationSelected,
                    isChecked = checkboxStates[document.id] ?: false
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun JobApplicationItem(
    applicationId: String,
    name: String,
    jobTitle: String,
    company: String,
    location: String,
    salary: Double,
    createdAt: Date,
    onClick: () -> Unit,
    onCheckedChange: (String, Boolean) -> Unit,
    isChecked: Boolean
) {
    val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(createdAt)
    var checked by remember {
        mutableStateOf(isChecked)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = name, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = jobTitle,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(text = company, style = MaterialTheme.typography.titleMedium)
                Text(text = location, style = MaterialTheme.typography.titleSmall)
                Text(
                    text = stringResource(id = R.string.salary_format, salary),
                    style = MaterialTheme.typography.titleSmall
                )
                Text(text = formattedDate, style = MaterialTheme.typography.titleSmall)
            }
            Checkbox(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    onCheckedChange(applicationId, it)
                },
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