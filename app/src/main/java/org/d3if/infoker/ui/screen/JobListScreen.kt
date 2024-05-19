package org.d3if.infoker.ui.screen
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if.infoker.FirestoreRepository
import org.d3if.infoker.R
import org.d3if.infoker.model.Job
import org.d3if.infoker.ui.theme.InfokerTheme
import org.d3if.infoker.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListScreen(
    jobListViewModel: JobListViewModel = viewModel(
        factory = ViewModelFactory(
            FirestoreRepository(FirebaseFirestore.getInstance())
        )
    )
) {
    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    val jobs by jobListViewModel.jobs.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.app_name),
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
                },
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Info"
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
            Column(
                modifier = Modifier.padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchBar(
                    modifier = Modifier.padding(top = 8.dp),
                    query = text,
                    onQueryChange = { text = it },
                    onSearch = { active = false },
                    active = active,
                    onActiveChange = { active = it },
                    placeholder = { Text("Search jobs") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = "More") },
                ) {
                    repeat(4) { idx ->
                        val resultText = "Suggestion $idx"
                        ListItem(
                            headlineContent = { Text(resultText) },
                            supportingContent = { Text("Additional info") },
                            leadingContent = { Icon(Icons.Filled.Star, contentDescription = "Star") },
                            modifier = Modifier
                                .clickable {
                                    text = resultText
                                    active = false
                                }
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }
                JobList(jobs = jobs, modifier = Modifier.weight(1f))
            }
        }
    )
}

@Composable
fun JobList(jobs: List<Job>, modifier: Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(jobs) { job ->
            JobListItem(job)
        }
    }
}

@Composable
fun JobListItem(job: Job) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
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
                Text(text = job.title, style = MaterialTheme.typography.titleLarge)
                Text(text = job.company, style = MaterialTheme.typography.titleMedium)
                Text(text = job.location, style = MaterialTheme.typography.titleSmall)
                Text(text = stringResource(id = R.string.salary_format, job.salary), style = MaterialTheme.typography.titleSmall)
            }
            Image(
                painter = painterResource(id = R.drawable.baseline_bookmark_border_24),
                contentDescription = "Bookmark",
                modifier = Modifier
                    .size(45.dp)
                    .padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewJobList() {
    InfokerTheme {
        JobListScreen()
    }
}
