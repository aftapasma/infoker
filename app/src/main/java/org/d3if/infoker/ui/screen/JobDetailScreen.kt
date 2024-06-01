package org.d3if.infoker.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if.infoker.R
import org.d3if.infoker.navigation.Screen
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository
import org.d3if.infoker.util.JobViewModelFactory

const val KEY_JOB_ID = "jobId"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailScreen(navController: NavHostController, id: String?) {
    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
    val authRepository = AuthRepository()

    val jobDetailViewModel: JobDetailViewModel = viewModel(factory = JobViewModelFactory(authRepository, firestoreRepository))
    LaunchedEffect(id) {
        id?.let { jobDetailViewModel.getJobById(it) }
    }

    val jobDetail by jobDetailViewModel.jobDetail.observeAsState()
    val isApplied by jobDetailViewModel.isApplied.observeAsState(false)
    val isBookmarked by jobDetailViewModel.isBookmarked.observeAsState(false)
    val currentUser = authRepository.getCurrentUser()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Infoker",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (currentUser == null) {
                            navController.navigate(Screen.Login.route)
                        } else {
                            jobDetail?.let { job ->
                                jobDetailViewModel.toggleBookmark(job)
                            }
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = if (!isBookmarked) R.drawable.baseline_bookmark_border_24 else R.drawable.baseline_bookmark_24),
                            contentDescription = "Bookmark"
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
                            onClick = {  },
                            modifier = Modifier
                                .size(48.dp)
                                .weight(1f)
                        ) {
                            Button(
                                onClick = {
                                    if (currentUser == null) {
                                        navController.navigate(Screen.Login.route)
                                    } else {
                                        jobDetail?.let { job ->
                                            jobDetailViewModel.toggleApplication(job)
                                        }
                                    }
                                }
                            ) {
                                Text(text = if (!isApplied) "Apply" else "Cancel")
                            }
                        }
                    }
                }
            )
        },
        content = {
            jobDetail?.let { job ->
                JobDetail(
                    title = job.getString("title") ?: "",
                    company = "Afta Tunas Jaya Abadi Barokah Tbk.",
                    location = job.getString("location") ?: "",
                    salary = job.getDouble("salary")?.toFloat() ?: 0.0f,
                    description = job.getString("description") ?: "",
                    modifier = Modifier.padding(top = 70.dp)
                )
            } ?: run {
                // Handle case where job is null (e.g., show a loading indicator or an error message)
                Text(
                    text = "Job Not Found",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    )
}

@Composable
fun JobDetail(
    title: String,
    company: String,
    location: String,
    salary: Float,
    description: String,
    modifier: Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_android_24),
                contentDescription = "Logo Perusahaan",
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Company: $company",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Location: $location",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Salary: $salary",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Description:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
//            Text(
//                text = "Requirements:",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//            LazyColumn {
//                items(jobDetail.requirements) { requirement ->
//                    Text(
//                        text = "• $requirement",
//                        style = MaterialTheme.typography.titleLarge,
//                        fontSize = 16.sp,
//                        modifier = Modifier.padding(bottom = 8.dp)
//                    )
//                }
//            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewJobDetailScreen() {
    JobDetail(
        title = "Budak Hitam",
        company = "Afta Tunas Jaya Abadi Tbk.",
        location = "Merangin, Jambi",
        salary = 0f,
        description = "Dicari orang-orang hitam.",
        modifier = Modifier
    )
}