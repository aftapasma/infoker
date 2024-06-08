package org.d3if.infoker.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
import org.d3if.infoker.ui.theme.InfokerTheme
import org.d3if.infoker.util.AuthViewModelFactory

@Composable
fun LoginScreen(navController: NavHostController) {
    val authRepository = AuthRepository()
    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
    val authViewModel: AuthViewModel =
        viewModel(factory = AuthViewModelFactory(authRepository, firestoreRepository))
    val context = LocalContext.current

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

//    val lifecycleOwner = LocalLifecycleOwner.current

//    DisposableEffect(Unit) {
//        val observer = LifecycleEventObserver { _, event ->
//            if (event == Lifecycle.Event.ON_RESUME) {
//                email = ""
//                password = ""
//            }
//        }
//        lifecycleOwner.lifecycle.addObserver(observer)
//        onDispose {
//            lifecycleOwner.lifecycle.removeObserver(observer)
//        }
//    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_infoker),
                contentDescription = stringResource(id = R.string.logo_infoker),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(180.dp)
                    .padding(24.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
            Text(
                text = "Infoker",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
//                modifier = Modifier
//                    .padding(130.dp)
//                    .fillMaxWidth(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Box(
            modifier = Modifier
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(25.dp, 25.dp,)
                )
                .align(Alignment.BottomCenter)
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    text = "Masuk",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 130.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(8.dp))
                LoginTextField(email) { email = it }
                Spacer(modifier = Modifier.padding(3.dp))
                PasswordTextField(password) { password = it }

                val gradientColor = listOf(Color(0xFF00696B), Color(0xFF01292B))
                val cornerRadius = 16.dp

                Spacer(modifier = Modifier.padding(10.dp))
                GradientButton(
                    gradientColors = gradientColor,
                    cornerRadius = cornerRadius,
                    roundedCornerShape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 30.dp),
                    onClick = {
                        authViewModel.signIn(email, password, navController)
                        Toast.makeText(context, "Attempting to sign in...", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "Atau",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.padding(10.dp))
                SosialMediaLogin()
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = "Belum Punya Akun ?",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                TextButton(onClick = {
                    navController.navigate(Screen.Register.route)
                }) {
                    Text(
                        text = "Buat Akun",
                        letterSpacing = 1.sp,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF00A7FF)
                    )
                }

                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "Ingin Merekrut Kandidat ?",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                TextButton(onClick = {
                    navController.navigate(Screen.Register.route)
                }) {
                    Text(
                        text = "Masuk Sebagai Perusahaan",
                        letterSpacing = 1.sp,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF00A7FF)
                    )
                }
                Spacer(modifier = Modifier.padding(20.dp))
            }
        }
    }
}

@Composable
private fun GradientButton(
    gradientColors: List<Color>,
    cornerRadius: Dp,
    roundedCornerShape: RoundedCornerShape,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp),
        onClick = onClick,
        contentPadding = PaddingValues(),
        colors = buttonColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(cornerRadius)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(colors = gradientColors),
                    shape = roundedCornerShape
                )
                .clip(roundedCornerShape)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Masuk", fontSize = 20.sp, color = Color.White)
        }
    }
}

@Composable
fun LoginTextField(email: String, onEmailChange: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        label = {
            Text(
                "Alamat Email",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        },
        placeholder = { Text(text = "Alamat Email") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(0.8f)
    )
}

@Composable
fun PasswordTextField(password: String, onPasswordChange: (String) -> Unit) {
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        label = {
            Text(
                "Massukan Password",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        },
        visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
        ),
        trailingIcon = {
            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                Icon(
                    painter = painterResource(id = if (passwordHidden) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24),
                    contentDescription = if (passwordHidden) "Lihat password" else "Sembunyikan password"
                )
            }
        },
        modifier = Modifier.fillMaxWidth(0.8f)
    )
}

@Composable
fun SosialMediaLogin() {
    Card(
        onClick = {}
    ) {

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color = Color.White)
            .padding(8.dp)
        ,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.google),
            contentDescription = null,
            modifier = Modifier.size(16.dp)

        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = "Masuk Menggunakan Google",
            style = MaterialTheme.typography.labelMedium.copy(
                color = Color(0xFF64748B)
            ),
        )

    }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    InfokerTheme {
        LoginScreen(rememberNavController())
    }
}