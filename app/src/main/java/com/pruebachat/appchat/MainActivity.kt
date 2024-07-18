package com.pruebachat.appchat

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.pruebachat.appchat.presentation.ui.home.screen.HomeScreen
import com.pruebachat.appchat.presentation.ui.home.viewmodel.HomeViewModel
import com.pruebachat.appchat.presentation.ui.login.screen.LoginScreen
import com.pruebachat.appchat.presentation.ui.login.viewmodel.LoginViewModel
import com.pruebachat.appchat.presentation.ui.registro.screen.RegistroScreen
import com.pruebachat.appchat.presentation.ui.registro.viewmodel.RegistroViewModel
import com.pruebachat.appchat.ui.theme.AppChatTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Anotación [@AndroidEntryPoint] indicando que Hilt debe proporcionar dependencias a esta actividad.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val registroViewModel: RegistroViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            AppChatTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                ) {
                    val navController = rememberNavController()
                    val viewModelScope = rememberCoroutineScope()
                    val context = LocalContext.current
                    val lifecycleOwner = LocalLifecycleOwner.current

                    // Observador para el evento de navegación desde registroViewModel

                    registroViewModel.navigateToLoginScreen.observe(this) { shouldNavigate ->
                        if (shouldNavigate) {
                            navController.navigate("loginScreen") {
                                popUpTo("registroScreen") { inclusive = true }
                            }

                            registroViewModel.onNavigateToLoginScreenComplete()// Reseteo del estado
                        }
                    }


                    // Observador para el evento de navegación desde homeViewModel

                    homeViewModel.navigateToLoginScreen.observe(this) { shouldNavigate ->
                        if (shouldNavigate) {
                            navController.navigate("loginScreen") {
                                popUpTo("homeScreen") { inclusive = true }
                            }

                            registroViewModel.onNavigateToLoginScreenComplete()
                        }
                    }


                    // Observador para el evento de navegación desde loginViewModel

                    loginViewModel.navigateToHomeScreen.observe(this) { shouldNavigate ->
                        if (shouldNavigate) {
                            navController.navigate("homeScreen") {
                                popUpTo("loginScreen") { inclusive = true }
                            }

                            loginViewModel.onNavigateToHomeScreenComplete()
                        }
                    }

                    // Observador para obtener un cambio de estado de menssage y mostrar al usuario
                    registroViewModel.successMessage.observe(this) { message ->
                        message?.let {
                             Toast.makeText(this, it, Toast.LENGTH_SHORT).show()

                            registroViewModel.onShowSuccessMessageComplete()
                        }
                    }



                    // Configuración del NavHost para gestionar la navegación entre pantallas
                    NavHost(navController = navController, startDestination = "loginScreen") {
                        composable("loginScreen") {
                            LoginScreen(loginViewModel,navController)
                        }


                        composable("homeScreen") {
                            HomeScreen(


                                homeViewModel

                            )
                        }

                        composable("registroScreen") {
                            RegistroScreen(
                                registroViewModel,
                                navController
                            )
                        }




                    }

                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppChatTheme {

    }
}