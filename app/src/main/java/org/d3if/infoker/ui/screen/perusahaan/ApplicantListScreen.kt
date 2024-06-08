package org.d3if.infoker.ui.screen.perusahaan

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import org.d3if.infoker.navigation.Screen
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository
import org.d3if.infoker.ui.theme.InfokerTheme
import org.d3if.infoker.util.ViewModelFactory
import java.text.DateFormat
import java.util.Date

const val KEY_COMPANYJOB_ID = "jobId"

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
        content = {
            ApplicantList(applicants = applicants, onClick = { applicant ->
                navController.navigate(Screen.ApplicantDetail.withId(applicant.id))
            })
        }
    )
}

@Composable
fun ApplicantList(
    applicants: List<DocumentSnapshot>,
    modifier: Modifier = Modifier,
    onClick: (DocumentSnapshot) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(applicants) { applicant ->
            ApplicantDetailContent(applicant = applicant, onClick = { onClick(applicant) })
        }
    }
}

@Composable
fun ApplicantDetailContent(applicant: DocumentSnapshot, onClick: () -> Unit) {
    val userMap = applicant["user"] as? Map<String, Any> ?: emptyMap()
    val name = userMap["name"] as? String ?: "Unknown"
    val email = userMap["email"] as? String ?: "Unknown"
    val createdAt = (applicant["createdAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date()

    val formattedDate = DateFormat.getDateInstance().format(createdAt)

    Box(
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
