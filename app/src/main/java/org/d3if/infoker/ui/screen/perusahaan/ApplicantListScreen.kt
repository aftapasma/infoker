package org.d3if.infoker.ui.screen.perusahaan

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if.infoker.navigation.Screen
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository
import org.d3if.infoker.ui.theme.InfokerTheme
import org.d3if.infoker.util.ViewModelFactory
import java.text.DateFormat
import java.util.Date

const val KEY_COMPANYJOB_ID = "jobId"

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ApplicantListScreen(navController: NavHostController, jobId: String?) {
    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
    val authRepository = AuthRepository()
    val applicantListViewModel: ApplicantListViewModel = viewModel(factory = ViewModelFactory(authRepository, firestoreRepository))

    LaunchedEffect(jobId) {
        if (jobId != null) {
            applicantListViewModel.getApplicationsByJobId(jobId)
        }
    }

    val applicants by applicantListViewModel.applicants.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Pelamar") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            ApplicantList(
                applicants = applicants,
                onClick = { applicant ->
                    navController.navigate(Screen.ApplicantDetail.withId(applicant.id))
                },
                modifier = Modifier.padding(paddingValues),
                viewModel = applicantListViewModel
            )
        }
    )
}

@Composable
fun ApplicantList(
    applicants: List<DocumentSnapshot>,
    modifier: Modifier = Modifier,
    onClick: (DocumentSnapshot) -> Unit,
    viewModel: ApplicantListViewModel
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(applicants) { applicant ->
            ApplicantDetailContent(applicant = applicant, onClick = { onClick(applicant) }, viewModel = viewModel)
        }
    }
}

@Composable
fun ApplicantDetailContent(applicant: DocumentSnapshot, onClick: () -> Unit, viewModel: ApplicantListViewModel) {
    val userMap = applicant["user"] as? Map<String, Any> ?: emptyMap()
    val name = userMap["name"] as? String ?: "Unknown"
    val email = userMap["email"] as? String ?: "Unknown"
    val createdAt = (applicant["createdAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date()

    val formattedDate = DateFormat.getDateInstance().format(createdAt)

    var photoUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(email) {
        val url = viewModel.getUserPhotoUrl(email)
        photoUrl = url
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {
                photoUrl?.let { url ->
                    Image(
                        painter = rememberImagePainter(url),
                        contentDescription = "Applicant Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                } ?: Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(8.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = name, style = MaterialTheme.typography.titleLarge)
                Text(text = email, style = MaterialTheme.typography.titleMedium)
                Text(text = formattedDate, style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewApplicantList() {
    InfokerTheme {
        ApplicantListScreen(rememberNavController(), jobId = "dummyJobId")
    }
}
