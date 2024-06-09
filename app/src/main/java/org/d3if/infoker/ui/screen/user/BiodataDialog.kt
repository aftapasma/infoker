import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun BiodataDialog(
    initialBiodata: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var biodata by rememberSaveable { mutableStateOf(initialBiodata) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Edit Biodata") },
        text = {
            TextField(
                value = biodata,
                onValueChange = { biodata = it },
                label = { Text("Biodata") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = { onSave(biodata) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}
