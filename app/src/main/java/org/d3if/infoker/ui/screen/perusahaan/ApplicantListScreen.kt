package org.d3if.infoker.ui.screen.perusahaan

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if.infoker.R
import org.d3if.infoker.ui.theme.InfokerTheme
import java.text.DateFormat
import java.util.Date

data class Applicant(
    val name: String,
    val jobTitle: String,
    val company: String,
    val location: String,
    val salary: Double,
    val createdAt: Date
)

@Composable
fun ApplicantListScreen(navController: NavHostController) {
    val dummyApplicants = listOf(
        Applicant("John Doe", "Software Engineer", "Tech Corp", "San Francisco, CA", 120000.0, Date()),
        Applicant("Jane Smith", "Product Manager", "Innovate Ltd.", "New York, NY", 95000.0, Date()),
        Applicant("Robert Johnson", "UX Designer", "Creative Inc.", "Los Angeles, CA", 80000.0, Date())
    )
    Scaffold(
        content = {
            ApplicantList(applicants = dummyApplicants, onClick = {})
        }
    )
}

@Composable
fun ApplicantList(
    applicants: List<Applicant>,
    modifier: Modifier = Modifier,
    onClick: (Applicant) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(applicants) { applicant ->
            ApplicantListItem(applicant = applicant, onClick = { onClick(applicant) })
        }
    }
}

@Composable
fun ApplicantListItem(applicant: Applicant, onClick: () -> Unit) {
    val formattedDate = DateFormat.getDateInstance().format(applicant.createdAt)

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
                Text(text = applicant.name, style = MaterialTheme.typography.titleLarge)
                Text(text = applicant.jobTitle, style = MaterialTheme.typography.titleMedium)
                Text(text = applicant.company, style = MaterialTheme.typography.titleMedium)
                Text(text = applicant.location, style = MaterialTheme.typography.titleSmall)
                Text(text = stringResource(id = R.string.salary_format, applicant.salary), style = MaterialTheme.typography.titleSmall)
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
        ApplicantListScreen(rememberNavController())
    }
}
