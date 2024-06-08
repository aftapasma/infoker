import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository
import org.d3if.infoker.ui.screen.component.CompanyBottomBar
import org.d3if.infoker.ui.theme.InfokerTheme
import org.d3if.infoker.util.AuthViewModelFactory

@Composable
fun ProfilScreen(navController: NavHostController) {
    val authRepository = AuthRepository()
    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository, firestoreRepository))

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
                BeforePersonalCompany()
                PersonalCompany()
            }
        }
    )
}

@Composable
fun HeaderCompany(authViewModel: AuthViewModel, navController: NavHostController, userProfile: UserProfile?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(64.dp)
                .background(Color.Gray, shape = CircleShape)
        )
        Column {
            Text(
                text = userProfile?.name ?: "Loading...",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(text = userProfile?.email ?: "Loading...")
        }
        Spacer(modifier = Modifier.width(100.dp))
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
fun PersonalCompany() {
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
            IconButton(onClick = { /* Edit action */ }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
//            backgroundColor = Color(0xFF1E1E1E)
        ) {
            Text(
                text = "P",
                color = Color.Gray,
                modifier = Modifier.padding( 16.dp)
            )
        }

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