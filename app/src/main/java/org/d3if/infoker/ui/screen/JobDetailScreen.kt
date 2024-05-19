package org.d3if.infoker.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if.infoker.FirestoreRepository
import org.d3if.infoker.R
import org.d3if.infoker.ui.theme.InfokerTheme
import org.d3if.infoker.util.ViewModelFactory

@Composable
fun JobDetailScreen(
    jobDetailViewModel: JobDetailViewModel = viewModel(
        factory = ViewModelFactory(
            FirestoreRepository(FirebaseFirestore.getInstance())
        )
    )
) {
    var title by remember {
        mutableStateOf("")
    }

    val company = "Afta Tunas Jaya Abadi Tbk."

    var location by remember {
        mutableStateOf("")
    }

    var salary by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    Scaffold { padding ->
        ScreenContent(
            title = title,
            onTitleChange = { title = it },
            location = location,
            onLocationChange = { location = it },
            salary = salary,
            onSalaryChange = { salary = it },
            description = description,
            onDescriptionChange = { description = it },
            onAddJobClick = {
                jobDetailViewModel.addJob(title, company, location, salary.toFloat(), description)
            },
            modifier = Modifier.padding(padding)
        )
    }


}

@Composable
fun ScreenContent(
    title: String,
    onTitleChange: (String) -> Unit,
    location: String,
    onLocationChange: (String) -> Unit,
    salary: String,
    onSalaryChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onAddJobClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { onTitleChange(it) },
            label = { Text(text = stringResource(id = R.string.title)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = location,
            onValueChange = { onLocationChange(it) },
            label = { Text(text = stringResource(id = R.string.location)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = salary,
            onValueChange = { onSalaryChange(it) },
            label = { Text(text = stringResource(id = R.string.salary)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { onDescriptionChange(it) },
            label = { Text(text = stringResource(id = R.string.description)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            modifier = modifier.fillMaxWidth()
        )
        Button(
            onClick = { onAddJobClick() },
            modifier = modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.add))
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun JobDetailScreenPreview() {
    InfokerTheme {
        JobDetailScreen()
    }
}