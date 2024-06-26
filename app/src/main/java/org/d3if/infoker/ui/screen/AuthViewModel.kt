import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import org.d3if.infoker.model.UserProfile
import org.d3if.infoker.navigation.Screen
import org.d3if.infoker.repository.AuthRepository
import org.d3if.infoker.repository.AuthResult
import org.d3if.infoker.repository.FirestoreRepository

class AuthViewModel(private val authRepository: AuthRepository, private val firestoreRepository: FirestoreRepository) : ViewModel() {
    private val _authResult = MutableLiveData<AuthResult<FirebaseUser>>()
    val authResult: LiveData<AuthResult<FirebaseUser>> get() = _authResult

    private val _userProfile = MutableLiveData<UserProfile?>()
    val userProfile: LiveData<UserProfile?> get() = _userProfile

    init {
        checkCurrentUser()
    }

    fun signIn(email: String, password: String, navController: NavHostController) {
        authRepository.signInWithEmail(email, password).observeForever { result ->
            _authResult.value = result

            if (result is AuthResult.Success) {
                updateUI(result.data, navController)
                Log.d("AuthViewModel", "User logged in")
            } else {
                updateUI(null, navController)
                Log.d("AuthViewModel", "User not logged in")
            }
        }
    }

    fun register(email: String, password: String, name: String, role: String, navController: NavHostController) {
        authRepository.registerWithEmail(email, password).observeForever { result ->
            _authResult.value = result

            if (result is AuthResult.Success) {
                viewModelScope.launch {
                    val userAdded = firestoreRepository.addUser(name, email, role)
                    if (userAdded) {
                        updateUI(result.data, navController)
                        Log.d("AuthViewModel", "User added to Firestore")
                    } else {
                        Log.e("AuthViewModel", "Failed to add user to Firestore")
                    }
                }
            } else {
                updateUI(null, navController)
            }
        }
    }

    fun logout(navController: NavHostController) {
        authRepository.logout()
        _authResult.value = AuthResult.Success(null)
        navController.navigate(Screen.Login.route) {
            popUpTo(Screen.JobList.route) { inclusive = true }
        }
        Log.d("AuthViewModel", "User logged out")
    }

    private fun checkCurrentUser() {
        val user = authRepository.getCurrentUser()
        _authResult.value = AuthResult.Success(user)
        user?.let { fetchUserProfile(it.email!!) }
    }

    fun fetchUserProfile(email: String) {
        viewModelScope.launch {
            val userDocument = firestoreRepository.getUserByEmail(email)
            _userProfile.value = userDocument?.toObject(UserProfile::class.java)
        }
    }

    private fun updateUI(user: FirebaseUser?, navController: NavHostController) {
        if (user != null) {
            val email = user.email
            if (email != null) {
                fetchUserProfile(email)
                viewModelScope.launch {
                    val role = firestoreRepository.getUserRoleByEmail(email)
                    role?.let {
                        when (it) {
                            "user" -> {
                                navController.navigate(Screen.JobList.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                                Log.d("AuthViewModel", "User moved to JobListScreen")
                            }
                            "company" -> {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                                Log.d("AuthViewModel", "User moved to MainScreen")
                            }
                            else -> {
                                // Handle unknown role
                                Log.d("AuthViewModel", "Unknown role: $role")
                            }
                        }
                    }
                }
            }
        } else {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.JobList.route) { inclusive = true }
            }
            Log.d("AuthViewModel", "User not logged in")
        }
    }
}
