package org.d3if.infoker.ui.screen.user

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import org.d3if.infoker.repository.AuthRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetail(navController: NavHostController, authRepository: AuthRepository) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var phone by remember { mutableStateOf(TextFieldValue("")) }
    var address by remember { mutableStateOf(TextFieldValue("")) }
    var showDialog by remember { mutableStateOf(false) }
    var pendingImageUri: Uri? by remember { mutableStateOf(null) }
    val user = authRepository.getCurrentUser()
    val userEmail = user?.email ?: "default_user"

    // Menambahkan state untuk URI gambar pengguna
    var userProfileImageUri by remember { mutableStateOf<Uri?>(null) }

    // Memuat URI gambar pengguna dari Firebase saat komponen dilakukan komposisi ulang
    LaunchedEffect(userEmail) {
        fetchUserProfileImage(userEmail,
            onSuccess = { uri ->
                // Mengatur URI gambar pengguna saat berhasil diambil
                userProfileImageUri = uri
            },
            onFailure = {
                // Menangani kasus gagal mengambil URI gambar pengguna
                Toast.makeText(context, "Failed to load profile image", Toast.LENGTH_SHORT).show()
            }
        )
    }

    val storage = Firebase.storage
    val storageRef = storage.reference
    val imageRef = storageRef.child("$userEmail.png")

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            pendingImageUri = it
            showDialog = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail profil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle save action */ }) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable { pickImageLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    // Menampilkan gambar profil pengguna dari URI yang diambil dari Firebase
                    userProfileImageUri?.let { uri ->
                        Image(
                            painter = rememberImagePainter(uri),
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } ?: run {
                        // Placeholder atau gambar default jika URI tidak tersedia
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize(),
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Lengkap") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Nomor Handphone") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Alamat") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(onClick = { /* Handle logout action */ }) {
                    Text("Keluar")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { /* Handle change account action */ }) {
                    Text("Ganti Akun")
                }
            }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Konfirmasi") },
                    text = { Text("Apakah Anda yakin ingin menyimpan gambar ini sebagai foto profil?") },
                    confirmButton = {
                        Button(onClick = {
                            pendingImageUri?.let { uri ->
                                uploadImageToFirebase(authRepository, userEmail, uri, context)
                            }
                            showDialog = false
                        }) {
                            Text("Ya")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("Tidak")
                        }
                    }
                )
            }
        }
    )
}


fun uploadImageToFirebase(authRepository: AuthRepository, userEmail: String, imageUri: Uri, context: Context) {
    val storageRef = FirebaseStorage.getInstance().reference.child("images/$userEmail")
    val firestoreRef = FirebaseFirestore.getInstance()

    storageRef.putFile(imageUri).continueWithTask { task ->
        if (!task.isSuccessful) {
            task.exception?.let { throw it }
        }
        storageRef.downloadUrl
    }.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val downloadUrl = task.result.toString()
            val userDoc = firestoreRef.collection("users").document(userEmail)
            userDoc.set(mapOf("profileImageUrl" to downloadUrl)).addOnSuccessListener {
                Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e ->
                Toast.makeText(context, "Failed to upload image: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Failed to upload image", Toast.LENGTH_LONG).show()
        }
    }
}


fun fetchUserProfileImage(userEmail: String, onSuccess: (Uri?) -> Unit, onFailure: () -> Unit) {
    val storageRef = FirebaseStorage.getInstance().reference
    val imageRef = storageRef.child("images/$userEmail")

    imageRef.downloadUrl
        .addOnSuccessListener { uri ->
            // Panggil onSuccess dan kirimkan URI gambar
            onSuccess(uri)
        }
        .addOnFailureListener {
            // Panggil onFailure jika terjadi kesalahan dalam mengambil gambar
            onFailure()
        }
}


//@Preview(showBackground = true)
//@Composable
//fun ProfileDetailPreview() {
//    InfokerTheme {
//        ProfileDetail(rememberNavController())
//    }
//}