package org.d3if.infoker.ui.screen.perusahaan.tabs

import android.app.Activity
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import org.d3if.infoker.ui.screen.component.CompanyBottomBar
import org.d3if.infoker.ui.theme.InfokerTheme
import org.d3if.infoker.util.ViewModelFactory
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val authRepository = AuthRepository()
    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
    val homeViewModel: HomeViewModel =
        viewModel(factory = ViewModelFactory(authRepository, firestoreRepository))

    LaunchedEffect(Unit) {
        homeViewModel.getJobs()
    }

    val activity = LocalContext.current as? Activity

    BackHandler {
        activity?.finish()
    }

    val searchText = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Daftar Lowongan")
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.AddJob.route)
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.tambah_pekerjaan),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        bottomBar = {
            CompanyBottomBar(navController = navController)
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

        SearchBar(searchText.value) {
            searchText.value = it
        }
        ScreenContent(homeViewModel, navController)
        }
    }
}

@Composable
fun SearchBar(searchText: String, onSearchTextChange: (String) -> Unit) {
    TextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        placeholder = { Text("Cari pekerjaan...") },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") }
    )
}

@Composable
fun ScreenContent(
    homeViewModel: HomeViewModel,
    navController: NavHostController,
//    modifier: Modifier
) {
    val jobs by homeViewModel.companyJobs.observeAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 84.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(jobs) { job ->
            ListItem(job, navController)
        }
    }
}

@Composable
fun ListItem(job: DocumentSnapshot, navController: NavHostController) {
    val title = job.getString("title")
    val location = job.getString("location")
    val date = job.getDate("createdAt") ?: Date()

    val formattedDate = DateFormat.getDateInstance().format(date)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(Screen.ApplicantList.withId(job.id)) }
            .padding(8.dp)
//            .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = title ?: "Unknown", style = MaterialTheme.typography.titleLarge)
                Text(text = location ?: "Unknown", style = MaterialTheme.typography.titleMedium)
                Text(text = formattedDate, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = { navController.navigate(Screen.EditJob.withId(job.id)) }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit"
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewHomeScreen() {
    InfokerTheme {
        HomeScreen(rememberNavController())
    }
}