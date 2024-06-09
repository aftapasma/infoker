package org.d3if.infoker.ui.screen.user

import AuthViewModel
import BiodataDialog
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.d3if.infoker.navigation.Screen
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository
import org.d3if.infoker.ui.screen.component.UserBottomBar
import org.d3if.infoker.ui.theme.InfokerTheme
import org.d3if.infoker.util.AuthViewModelFactory
import org.d3if.infoker.util.ViewModelFactory

@Composable
fun Profile2(navController: NavHostController) {
    val authRepository = AuthRepository()
    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository, firestoreRepository))
    val userProfileViewModel: UserProfileViewModel = viewModel(factory = ViewModelFactory(authRepository, firestoreRepository))

    val currentUser = authRepository.getCurrentUser()

    if (currentUser == null) {
        navController.navigate(Screen.Login.route)
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    BackHandler {
        navController.navigate(Screen.JobList.route)
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                content = {
                    UserBottomBar(navController = navController)
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeaderSection(authViewModel, navController)
                Tab(selectedTabIndex) { selectedTabIndex = it }
                Spacer(modifier = Modifier.height(16.dp))
                when (selectedTabIndex) {
                    0 -> Personal(userProfileViewModel)
                    1 -> Cv(currentUser?.email ?: "")
                }
            }
        }
    )
}

@Composable
fun Cv(userEmail: String) {
    val context = LocalContext.current
    var fileUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cv",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }
        if (fileUri == null) {
            // Text to display if CV is not uploaded
            Text(
                text = "Upload CV anda",
                color = Color.Gray,
                modifier = Modifier.padding(12.dp)
            )
            fileUri?.let {
                // Memanggil FilePickerButton dengan callback untuk menampilkan dialog
                FilePickerButton(onFilePicked = { uri -> fileUri = uri }, onUploadClicked = { showDialog = true })
            } ?: run {
                FilePickerButton(onFilePicked = { uri -> fileUri = uri }, onUploadClicked = { showDialog = true })
            }
        } else {
            // Text to display if CV is uploaded
            Text(
                text = "CV telah diupload",
                color = Color.Gray,
                modifier = Modifier.padding(12.dp)
            )
        }

        // Tampilkan dialog jika showDialog true
        if (showDialog) {
            CvUploadDialog(
                onDismiss = { showDialog = false },
                onSave = {
                    uploadFileToFirebase(fileUri!!, context, userEmail, onSuccess = {
                        // Handle success
                    }, onFailure = {
                        // Handle failure
                    })
                    showDialog = false
                }
            )
        }
    }
}

fun uploadFileToFirebase(uri: Uri, context: Context, userEmail: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    val storageReference = FirebaseStorage.getInstance().reference.child("cv/$userEmail")
    storageReference.putFile(uri)
        .addOnSuccessListener {
            Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT).show()
            onSuccess()
        }
        .addOnFailureListener { exception ->
            Toast.makeText(context, "Upload Failed: ${exception.message}", Toast.LENGTH_SHORT).show()
            onFailure(exception)
        }
}

@Composable
fun FilePickerButton(onFilePicked: (Uri) -> Unit, onUploadClicked: () -> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            onFilePicked(it)
            onUploadClicked()
        }
    }

    OutlinedButton(onClick = { launcher.launch("*/*") }) {
        Text("Upload")
    }
}

@Composable
fun HeaderSection(authViewModel: AuthViewModel, navController: NavHostController) {
    val userProfile by authViewModel.userProfile.observeAsState()

    var userProfileImageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(userProfile?.email) {
        fetchUserProfileImage(userProfile?.email ?: "",
            onSuccess = { uri ->
                userProfileImageUri = uri
            },
            onFailure = {

            }
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        ) {
            userProfileImageUri?.let { uri ->
                Image(
                    painter = rememberImagePainter(uri),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } ?: run {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { navController.navigate(Screen.ProfilDetail.route) }
                .padding(start = 16.dp)
        ) {
            Text(
                text = userProfile?.name ?: "Loading...",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(text = userProfile?.email ?: "Loading...")
        }
        IconButton(onClick = { authViewModel.logout(navController) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = "Logout",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}


@Composable
fun Tab(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("Tentang Kamu", "Upload CV")

    TabRow(
        selectedTabIndex = selectedTabIndex
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else Color.Black
                    )
                }
            )
        }
    }
}

@Composable
fun Personal(userProfileViewModel: UserProfileViewModel) {
    val userProfile by userProfileViewModel.userProfile.observeAsState()
    var biodata by rememberSaveable { mutableStateOf(userProfile?.biodata ?: "") }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        BiodataDialog(
            initialBiodata = biodata,
            onDismiss = { showDialog = false },
            onSave = {
                biodata = it
                userProfileViewModel.saveBiodata(it)
                showDialog = false
            }
        )
    }

    if (biodata.isEmpty()) {
        BeforePersonalUser(onAddBiodataClick = { showDialog = true })
    } else {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Biodata Pribadi",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Biodata",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Text(
                text = biodata,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun BeforePersonalUser(onAddBiodataClick: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Biodata",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }
        Text(
            text = "Beritahu perusahaan tentang diri anda",
            color = Color.Gray,
            modifier = Modifier.padding(12.dp)
        )
        OutlinedButton(
            onClick = onAddBiodataClick,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Masukan Biodata")
        }
    }
}

@Composable
fun CareerHistory() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Upload CV",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
            IconButton(onClick = { /* tambah action */ }) {
                Icon(
                    imageVector = Icons.Default.AddCircleOutline,
                    contentDescription = "Edit",
                    tint = Color.White
                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "Nama Pekerjaan", color = Color.White, style = MaterialTheme.typography.titleLarge)
                    Text(text = "nama perusahaan", color = Color.Gray, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Jun 2024 - nganggur",
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                IconButton(onClick = { /* Edit action */ }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
// tampilan sebelum klik tombol tambah
//@Composable
//fun Education() {
//    Column {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "Education",
//                fontWeight = FontWeight.Bold,
//                fontSize = 16.sp,
//                color = Color.White
//            )
//
//        }
//        Text(
//            text = "Beritahu Perusahaan tentang pendidikan anda",
//            color = Color.Gray,
//            modifier = Modifier.padding( 4.dp)
//        )
//        OutlinedButton(
//            onClick = { /*Tambah pendidikan*/ },
//            modifier = Modifier.padding(top = 16.dp)
//        ) {
//            Text(text = "Tambah Pendidikan", color = Color.White)
//        }
//    }
//}
@Preview(showBackground = true)
@Composable
fun Profile2Preview() {
    InfokerTheme {
        Profile2(rememberNavController())
    }
}