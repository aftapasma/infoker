package org.d3if.infoker.ui.screen

import androidx.compose.foundation.Image
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
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.d3if.infoker.R

data class ApplicantDetail(
    val name: String,
    val age: Int,
    val email: String,
    val phoneNumber: String
)

val dummyApplicantDetails = listOf(
    ApplicantDetail("John Doe", 30, "john.doe@example.com", "1234567890")
)

@Composable
fun ApplicantListScreen() {
    Scaffold(
        content = {
            LazyColumn {
                items(dummyApplicantDetails) { applicant ->
                    ApplicantListItem(applicant = applicant)
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = { /* Accept action */ }) {
                            Text(text = "Accept")
                        }
                        Button(onClick = { /* Reject action */ }) {
                            Text(text = "Reject")
                        }
                    }
                }
            )
        }
    )
}

@Composable
fun ApplicantListItem(applicant: ApplicantDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navigate to detail screen */ }
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_android_24),
            contentDescription = "Foto pelamar",
            modifier = Modifier.size(100.dp)
        )
        Text(text = "Name: ${applicant.name}", color = Color.Black, style = MaterialTheme.typography.titleLarge,)
        Text(text = "Age: ${applicant.age}", color = Color.Black,  style = MaterialTheme.typography.titleMedium,)
        Text(text = "Email: ${applicant.email}", color = Color.Black,  style = MaterialTheme.typography.titleSmall,)
        Text(text = "Phone Number: ${applicant.phoneNumber}", color = Color.Black,  style = MaterialTheme.typography.titleSmall,)
    }
}

@Preview
@Composable
fun PreviewApplicantListScreen() {
    ApplicantListScreen()
}
