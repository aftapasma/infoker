package org.d3if.infoker.ui.screen


import AuthViewModel
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
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
fun UserRegiterScreen(navController: NavHostController) {
    val authRepository = AuthRepository()
    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
    val authViewModel: AuthViewModel =
        viewModel(factory = AuthViewModelFactory(authRepository, firestoreRepository))

    val context = LocalContext.current

    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordConfirm by rememberSaveable { mutableStateOf("") }

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                email = ""
                password = ""
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    BackHandler {
        navController.navigate(Screen.JobList.route)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
            )
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
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    ,

                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //.........................Spacer
//                Spacer(modifier = Modifier.height(30.dp))

                //.........................Text: title
                Text(
                    text = "Buat Akun",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
//                        .padding(top = 130.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(8.dp))
                UserRegisterName(name) { name = it }

                Spacer(modifier = Modifier.padding(3.dp))
                RegisterEmail(email) { email = it }

                Spacer(modifier = Modifier.padding(3.dp))
                RegisterPassword(password) { password = it }

                Spacer(modifier = Modifier.padding(3.dp))
                RegisterPasswordConfirm(passwordConfirm) { passwordConfirm = it }


                val gradientColor = listOf(Color(0xFF00696B), Color(0xFF01292B))
                val cornerRadius = 16.dp


                Spacer(modifier = Modifier.padding(10.dp))
                /* Button(
                     onClick = {},
                     modifier = Modifier
                         .fillMaxWidth(0.8f)
                         .height(50.dp)
                 ) {
                     Text(text = "Login", fontSize = 20.sp)
                 }*/
                GradientButton(
                    gradientColors = gradientColor,
                    cornerRadius = cornerRadius,
                    nameButton = "Buat Akun",
                    roundedCornerShape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 30.dp),
                    onClick = {
                        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                            Toast.makeText(context, "Semua bidang harus diisi", Toast.LENGTH_SHORT).show()
                        } else if (password != passwordConfirm) {
                            Toast.makeText(context, "Password tidak cocok", Toast.LENGTH_SHORT).show()
                        } else {
                            authViewModel.register(email, password, name, "user", navController)
                        }
                    }
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = "Sudah punya akun?",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                androidx.compose.material3.TextButton(onClick = {
                    navController.popBackStack()
                }) {
                    Text(
                        text = "Sign In",
                        letterSpacing = 1.sp,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF00A7FF)
                    )
                }
                Spacer(modifier = Modifier.padding(20.dp))

            }




    }


}

@Composable
fun CompanyRegiterScreen(navController: NavHostController) {
    val authRepository = AuthRepository()
    val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
    val authViewModel: AuthViewModel =
        viewModel(factory = AuthViewModelFactory(authRepository, firestoreRepository))

    val context = LocalContext.current

    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordConfirm by rememberSaveable { mutableStateOf("") }

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                email = ""
                password = ""
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    BackHandler {
        navController.navigate(Screen.JobList.route)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
            )
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
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //.........................Spacer
//                Spacer(modifier = Modifier.height(30.dp))

                //.........................Text: title
                Text(
                    text = "Buat Akun",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
//                        .padding(top = 130.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(8.dp))
                CompanyRegisterName(name) { name = it }

                Spacer(modifier = Modifier.padding(3.dp))
                RegisterEmail(email) { email = it }

                Spacer(modifier = Modifier.padding(3.dp))
                RegisterPassword(password) { password = it }

                Spacer(modifier = Modifier.padding(3.dp))
                RegisterPasswordConfirm(passwordConfirm) { passwordConfirm = it }


                val gradientColor = listOf(Color(0xFF00696B), Color(0xFF01292B))
                val cornerRadius = 16.dp


                Spacer(modifier = Modifier.padding(10.dp))
                /* Button(
                     onClick = {},
                     modifier = Modifier
                         .fillMaxWidth(0.8f)
                         .height(50.dp)
                 ) {
                     Text(text = "Login", fontSize = 20.sp)
                 }*/
                GradientButton(
                    gradientColors = gradientColor,
                    cornerRadius = cornerRadius,
                    nameButton = "Buat Akun",
                    roundedCornerShape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 30.dp),
                    onClick = {
                        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                            Toast.makeText(context, "Semua bidang harus diisi", Toast.LENGTH_SHORT).show()
                        } else if (password != passwordConfirm) {
                            Toast.makeText(context, "Password tidak cocok", Toast.LENGTH_SHORT).show()
                        } else {
                            authViewModel.register(email, password, name, "user", navController)
                        }
                    }
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = "Sudah punya akun?",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                androidx.compose.material3.TextButton(onClick = {
                    navController.popBackStack()
                }) {
                    Text(
                        text = "Sign In",
                        letterSpacing = 1.sp,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF00A7FF)
                    )
                }


//                Spacer(modifier = Modifier.padding(5.dp))
//                androidx.compose.material3.TextButton(onClick = {
//
////                    navController.navigate("reset_page"){
////                        popUpTo(navController.graph.startDestinationId)
////                        launchSingleTop = true
////                    }
//
//
//                }) {
////                    androidx.compose.material3.Text(
////                        text = "Reset Password",
////                        letterSpacing = 1.sp,
////                        style = MaterialTheme.typography.labelLarge,
////                    )
//                }
//                Spacer(modifier = Modifier.padding(20.dp))

            }




    }


}

//...........................................................................
@Composable
private fun GradientButton(
    gradientColors: List<Color>,
    cornerRadius: Dp,
    nameButton: String,
    roundedCornerShape: RoundedCornerShape,
    onClick: () -> Unit
) {

    androidx.compose.material3.Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp),
        onClick = onClick,

        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
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
                /*.background(
                    brush = Brush.linearGradient(colors = gradientColors),
                    shape = RoundedCornerShape(cornerRadius)
                )*/
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = nameButton,
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}


//name

@Composable
fun UserRegisterName(
    name: String,
    onNameChange: (String) -> Unit
) {
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        label = {
            Text(
                "Nama Lengkap",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        },
        placeholder = { Text(text = "Nama Lengkap") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Words
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
fun CompanyRegisterName(
    name: String,
    onNameChange: (String) -> Unit
) {
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        label = {
            Text(
                "Nama Perusahaan",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        },
        placeholder = { Text(text = "Nama Perusahaan") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Words
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(0.8f)
    )
}

//email id
@Composable
fun RegisterEmail(email: String, onEmailChange: (String) -> Unit) {
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

//password
@Composable
fun RegisterPassword(password: String, onPasswordChange: (String) -> Unit) {
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        label = {
            Text(
                "Masukan Password",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        },
        visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Password),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
        ),
        trailingIcon = {
            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                Icon(
                    painter = painterResource(id = if (passwordHidden) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24),
                    contentDescription = if (passwordHidden) "Show password" else "Hide password"
                )
            }
        },
        modifier = Modifier.fillMaxWidth(0.8f)
    )
}

//password confirm
@Composable
fun RegisterPasswordConfirm(passwordConfirm: String, onPasswordConfirmChange: (String) -> Unit) {
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    OutlinedTextField(
        value = passwordConfirm,
        onValueChange = onPasswordConfirmChange,
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        label = {
            Text(
                "Konfirmasi Password",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        },
        visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
        ),
        trailingIcon = {
            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                Icon(
                    painter = painterResource(id = if (passwordHidden) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24),
                    contentDescription = if (passwordHidden) "Show password" else "Hide password"
                )
            }
        },
        modifier = Modifier.fillMaxWidth(0.8f)
    )
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    InfokerTheme {
        UserRegiterScreen(rememberNavController())
    }
}