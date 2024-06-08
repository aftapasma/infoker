package org.d3if.infoker.ui.screen.perusahaan

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if.infoker.R
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository
import org.d3if.infoker.util.ViewModelFactory
import java.text.DateFormat
import java.util.Date

const val KEY_APPLICANT_ID = "applicantId"

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
        content = {
            applicantDetail?.let { applicant ->
                ApplicantDetailContent(
                    applicant = applicant
                )
            } ?: run {
                Text(text = "Loading...", modifier = Modifier.padding(16.dp))
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
    applicant: DocumentSnapshot
) {
    val userMap = applicant["user"] as? Map<String, Any> ?: emptyMap()
    val name = userMap["name"] as? String ?: "Unknown"
    val email = userMap["email"] as? String ?: "Unknown"
    val createdAt = (applicant["createdAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_android_24),
            contentDescription = "Foto pelamar",
            modifier = Modifier.size(100.dp)
        )
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
//        Text(text = "Phone Number: ${applicant.phoneNumber}", color = Color.Black,  style = MaterialTheme.typography.titleSmall,)
    }
}

@Preview
@Composable
fun PreviewApplicantListScreen() {
    ApplicantDetailScreen(rememberNavController(), "")
}
