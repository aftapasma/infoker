package org.d3if.infoker.ui.screen.perusahaan

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if.infoker.R
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository
import org.d3if.infoker.ui.theme.InfokerTheme
import org.d3if.infoker.util.ViewModelFactory

const val KEY_EDITJOB_ID = "editJobId"

@Composable
fun AddOrEditJobScreen(navController: NavHostController, jobId: String? = null) {
    val authRepository = AuthRepository()
    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())

    val addJobViewModel: AddOrEditJobViewModel =
        viewModel(factory = ViewModelFactory(authRepository, firestoreRepository))

    if (jobId != null) {
        LaunchedEffect(jobId) {
            addJobViewModel.loadJobDetails(jobId)
        }
    }

    Scaffold { padding ->
        ScreenContent(
            title = addJobViewModel.title,
            onTitleChange = { addJobViewModel.title = it },
            location = addJobViewModel.location,
            onLocationChange = { addJobViewModel.location = it },
            salary = addJobViewModel.salary,
            onSalaryChange = { addJobViewModel.salary = it },
            description = addJobViewModel.description,
            onDescriptionChange = { addJobViewModel.description = it },
            onAddJobClick = {
                addJobViewModel.addOrEditJob(jobId)
                navController.popBackStack()
            },
            isEditMode = addJobViewModel.isEditMode,
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
    isEditMode: Boolean,
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
                imeAction = ImeAction.Done
            ),
            modifier = modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                onAddJobClick()
                      },
            modifier = modifier.fillMaxWidth()
        ) {
            Text(text = if (isEditMode) stringResource(id = R.string.edit) else stringResource(id = R.string.add))
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddOrEditJobScreenPreview() {
    InfokerTheme {
        AddOrEditJobScreen(rememberNavController())
    }
}