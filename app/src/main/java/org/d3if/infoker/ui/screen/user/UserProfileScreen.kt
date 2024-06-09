package org.d3if.infoker.ui.screen.user

import AuthViewModel
import BiodataDialog
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if.infoker.R
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
                    1 -> CareerHistory()
                }
            }
        }
    )
}

@Composable
fun HeaderSection(authViewModel: AuthViewModel, navController: NavHostController) {
    val userProfile by authViewModel.userProfile.observeAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
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
fun Tab(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("Tentang Kamu", "Upload CV")

    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.DarkGray,
        contentColor = Color.White,
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