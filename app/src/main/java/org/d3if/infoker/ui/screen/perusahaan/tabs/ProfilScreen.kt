import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if.infoker.model.UserProfile
import org.d3if.infoker.navigation.Screen
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository
import org.d3if.infoker.ui.screen.component.CompanyBottomBar
import org.d3if.infoker.ui.screen.perusahaan.tabs.ProfilViewModel
import org.d3if.infoker.ui.screen.user.UserProfileViewModel
import org.d3if.infoker.ui.screen.user.fetchUserProfileImage
import org.d3if.infoker.ui.theme.InfokerTheme
import org.d3if.infoker.util.AuthViewModelFactory
import org.d3if.infoker.util.ViewModelFactory

@Composable
fun ProfilScreen(navController: NavHostController) {
    val authRepository = AuthRepository()
    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository, firestoreRepository))
    val CompanyProfileViewModel: ProfilViewModel = viewModel(factory = ViewModelFactory(authRepository, firestoreRepository))

    val userProfile = authViewModel.userProfile.observeAsState()

    Scaffold(
        bottomBar = {
            BottomAppBar(
                content = {
                    CompanyBottomBar(navController = navController)
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeaderCompany(authViewModel, navController, userProfile.value)
//                BeforePersonalCompany()
                PersonalCompany(CompanyProfileViewModel)
            }
        }
    )
}

@Composable
fun HeaderCompany(authViewModel: AuthViewModel, navController: NavHostController, userProfile: UserProfile?) {
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
fun PersonalCompany(profilViewModel: ProfilViewModel) {
    val companyProfile by profilViewModel.userProfile.observeAsState()
    var biodata by rememberSaveable { mutableStateOf(companyProfile?.biodata ?: "") }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        BiodataDialog(
            initialBiodata = companyProfile?.biodata ?: "",
            onDismiss = { showDialog = false },
            onSave = {
                profilViewModel.saveBiodataCompany(it)
                showDialog = false
            }
        )
    }

//    if (biodata.isEmpty()) {
//        BeforePersonalUser(onAddBiodataClick = { showDialog = true })
//    } else {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Deskripsi Perusahaan",
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
            text = companyProfile?.biodata ?: "",
            modifier = Modifier.padding(8.dp)
        )
    }
}

// tampilan sebelum klik tombol tambah
@Composable
fun BeforePersonalCompany() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Deskripsi Perusahaan",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )

        }
        Text(
            text = "Beritahu Pegawai tentang perusahaan anda",
            color = Color.Gray,
            modifier = Modifier.padding( 4.dp)
        )
        OutlinedButton(
            onClick = { /*Tambah pendidikan*/ },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Masukan Deskripsi")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    InfokerTheme {
        ProfilScreen(rememberNavController())
    }
}