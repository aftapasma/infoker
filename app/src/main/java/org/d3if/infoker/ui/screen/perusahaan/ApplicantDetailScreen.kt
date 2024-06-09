package org.d3if.infoker.ui.screen.perusahaan

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
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
import com.google.firebase.storage.FirebaseStorage
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository
import org.d3if.infoker.util.ViewModelFactory
import java.text.DateFormat
import java.util.Date

const val KEY_APPLICANT_ID = "applicantId"

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ApplicantDetailScreen(navController: NavHostController, applicantId: String?) {
    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
    val authRepository = AuthRepository()
    val applicantDetailViewModel: ApplicantDetailViewModel =
        viewModel(factory = ViewModelFactory(authRepository, firestoreRepository))

    LaunchedEffect(applicantId) {
        if (applicantId != null) {
            applicantDetailViewModel.getApplicationById(applicantId)
        }
    }

    val applicantDetail by applicantDetailViewModel.applicantDetail.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Pelamar") },
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
            applicantDetail?.let { applicant ->
                ApplicantDetailContent(
                    applicant = applicant,
                    modifier = Modifier.padding(paddingValues),
                    viewModel = applicantDetailViewModel
                )
            } ?: run {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sedang Memuat...",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                content = {
                    val status = applicantDetail?.getString("status") ?: "Unknown"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (status == "Applied") {
                            Button(onClick = {
                                applicantDetail?.id?.let { id ->
                                    applicantDetailViewModel.updateApplicationStatus(
                                        id,
                                        "Accepted"
                                    )
                                }
                            }) {
                                Text(text = "Accept")
                            }
                            Button(onClick = {
                                applicantDetail?.id?.let { id ->
                                    applicantDetailViewModel.updateApplicationStatus(
                                        id,
                                        "Rejected"
                                    )
                                }
                            }) {
                                Text(text = "Reject")
                            }
                        } else {
                            Button(onClick = {}, enabled = false) {
                                Text(text = status)
                            }
                        }
                    }
                }
            )
        }
    )
}

@Composable
fun ApplicantDetailContent(
    applicant: DocumentSnapshot,
    modifier: Modifier = Modifier,
    viewModel: ApplicantDetailViewModel
) {
    val userMap = applicant["user"] as? Map<String, Any> ?: emptyMap()
    val name = userMap["name"] as? String ?: "Unknown"
    val email = userMap["email"] as? String ?: "Unknown"
    val createdAt = (applicant["createdAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date()
    val context = LocalContext.current

    var photoUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(email) {
        val url = viewModel.getUserPhotoUrl(email)
        photoUrl = url
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        ) {
            photoUrl?.let { url ->
                Image(
                    painter = rememberImagePainter(url),
                    contentDescription = "Applicant Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
            } ?: Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp)
            )
        }

        Text(text = "Name: $name", color = Color.Black, style = MaterialTheme.typography.titleLarge)
        Text(
            text = "Email: $email",
            color = Color.Black,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Applied on: ${DateFormat.getDateInstance().format(createdAt)}",
            color = Color.Black,
            style = MaterialTheme.typography.titleSmall
        )
        Button(onClick = {
            fetchFileUrl(email, context)
        }) {
            Text(text = "Download CV")
        }
    }
}

fun fetchFileUrl(userEmail: String, context: Context) {
    val storageReference = FirebaseStorage.getInstance().reference.child("cv/$userEmail")
    storageReference.downloadUrl.addOnSuccessListener { uri ->
        // Handle successful download URL retrieval
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }.addOnFailureListener { exception ->
        // Handle failure
    }
}


@Preview
@Composable
fun PreviewApplicantListScreen() {
    ApplicantDetailScreen(rememberNavController(), "")
}
