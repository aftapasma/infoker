package org.d3if.infoker.ui.screen.user

import AuthViewModel
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import org.d3if.infoker.navigation.Screen
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.FirestoreRepository
import org.d3if.infoker.ui.screen.component.UserBottomBar
import org.d3if.infoker.ui.theme.InfokerTheme
import org.d3if.infoker.util.AuthViewModelFactory

@Composable
fun Profile2(navController: NavHostController) {
    val authRepository = AuthRepository()
    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository, firestoreRepository))

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
                    .padding(paddingValues)
                , horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeaderSection(authViewModel, navController)
                Tab(selectedTabIndex) { selectedTabIndex = it }
                Spacer(modifier = Modifier.height(16.dp))
                when (selectedTabIndex) {
                    0 -> Personal()
                    1 -> CareerHistory()
//            2 -> Education()
                    // Tambah kalo kurang
                }
            }
        }

    )
}

@Composable
fun HeaderSection(authViewModel: AuthViewModel, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
//            shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)
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
                text = "Jawir",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(text = "jawir@email")
        }
        Spacer(modifier = Modifier.width(100.dp))
        IconButton(onClick = { authViewModel.logout(navController) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(32.dp)
            )
        }
    }
}

@Composable
fun Tab(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("Tentang Kamu", "Upload CV")

    TabRow(
        selectedTabIndex = selectedTabIndex,
//        backgroundColor = Color(0xFF121212),
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
fun Personal() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Biodata pribadi",
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
                .padding(8.dp),
//            backgroundColor = Color(0xFF1E1E1E)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
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