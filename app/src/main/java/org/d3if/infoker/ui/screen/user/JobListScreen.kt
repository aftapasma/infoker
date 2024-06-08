package org.d3if.infoker.ui.screen.user
import AuthViewModel
import android.app.Activity
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if.infoker.R
import org.d3if.infoker.navigation.Screen
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository
import org.d3if.infoker.ui.screen.component.UserBottomBar
import org.d3if.infoker.ui.theme.InfokerTheme
import org.d3if.infoker.util.AuthViewModelFactory
import org.d3if.infoker.util.ViewModelFactory
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListScreen(
    navController: NavHostController
) {
    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    val authRepository = AuthRepository()
    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository, firestoreRepository))

    val jobListViewModel: JobListViewModel = viewModel(factory = ViewModelFactory(authRepository, firestoreRepository))
    val jobs by jobListViewModel.jobs.observeAsState(initial = emptyList())

    LaunchedEffect(text) {
        jobListViewModel.searchJobs(text)
    }

    val activity = LocalContext.current as? Activity

    BackHandler {
        activity?.finish()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        },
        bottomBar = {
            UserBottomBar(navController = navController)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchBar(
                    modifier = Modifier.padding(8.dp),
                    query = text,
                    onQueryChange = { newText ->
                        text = newText
                        jobListViewModel.searchJobs(newText)
                    },
                    onSearch = { active = false },
                    active = active,
                    onActiveChange = { active = it },
                    placeholder = { Text("Search jobs") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
                ) {

                }
                JobList(jobs = jobs, modifier = Modifier.weight(1f)) {navController.navigate(Screen.JobDetail.withId(it.id))}
            }
        }
    )
}

@Composable
fun JobList(
    jobs: List<DocumentSnapshot>,
    modifier: Modifier = Modifier,
    onClick: (DocumentSnapshot) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(jobs) { job ->
            JobListItem(job = job, onClick = {onClick(job)})
        }
    }
}

@Composable
fun JobListItem(job: DocumentSnapshot, onClick: () -> Unit) {
    val company = "Afta Tunas Jaya Abadi Barokah Tbk."

    // Extracting job details from DocumentSnapshot
    val title = job.getString("title") ?: ""
    val location = job.getString("location") ?: ""
    val salary = job.getDouble("salary") ?: 0.0
    val date = job.getDate("createdAt") ?: Date()

    val formattedDate = DateFormat.getDateInstance().format(date)


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_android_24),
                contentDescription = "Company Logo",
                modifier = Modifier
                    .size(48.dp)
                    .padding(8.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = title, style = MaterialTheme.typography.titleLarge)
                Text(text = company, style = MaterialTheme.typography.titleMedium)
                Text(text = location, style = MaterialTheme.typography.titleSmall)
                Text(text = stringResource(id = R.string.salary_format, salary), style = MaterialTheme.typography.titleSmall)
                Text(text = formattedDate, style = MaterialTheme.typography.titleSmall)
            }
//            Image(
//                painter = painterResource(id = R.drawable.baseline_bookmark_border_24),
//                contentDescription = "Bookmark",
//                modifier = Modifier
//                    .size(45.dp)
//                    .padding(8.dp)
//            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewJobList() {
    InfokerTheme {
        JobListScreen(rememberNavController())
    }
}
