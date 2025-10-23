package com.example.matchpet

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.matchpet.core.AppContainer
import com.example.matchpet.ui.MatchPetApp
import com.example.matchpet.ui.navigation.MatchPetDestinations
import com.example.matchpet.ui.screens.auth.AuthViewModel
import com.example.matchpet.ui.screens.auth.AuthViewModelFactory
import com.example.matchpet.ui.theme.MatchPetTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appContainer = (application as MatchPetApplication).container
        setContent {
            MatchPetTheme {
                MatchPetRoot(appContainer)
            }
        }
    }
}

@Composable
private fun MatchPetRoot(appContainer: AppContainer) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .build()
    }

    val googleSignInClient: GoogleSignInClient = remember {
        GoogleSignIn.getClient(context, gso)
    }

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            authRepository = appContainer.authRepository,
            observeSessionUseCase = { appContainer.sessionManager.observeToken() },
            userRepository = appContainer.userRepository
        )
    )

    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    val googleSignInLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(Exception::class.java)
            val idToken = account.idToken
            if (idToken != null) {
                authViewModel.onGoogleTokenReceived(idToken)
            } else {
                scope.launch { snackbarHostState.showSnackbar("No se pudo obtener el token de Google") }
            }
        } catch (e: Exception) {
            scope.launch { snackbarHostState.showSnackbar("Error al iniciar sesión con Google") }
        }
    }

    LaunchedEffect(authViewModel.uiEvent) {
        authViewModel.uiEvent.collect { event ->
            when (event) {
                is AuthViewModel.UiEvent.ShowMessage -> scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    MatchPetApp(
        startDestination = if (isLoggedIn) {
            MatchPetDestinations.Profile.route
        } else {
            MatchPetDestinations.Home.route
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        authViewModel = authViewModel,
        onGoogleSignIn = {
            val signInIntent: Intent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }
    )
}
