package com.example.matchpet.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.matchpet.ui.navigation.MatchPetDestinations
import com.example.matchpet.ui.screens.auth.LoginScreen
import com.example.matchpet.ui.screens.auth.RegisterScreen
import com.example.matchpet.ui.screens.home.HomeScreen
import com.example.matchpet.ui.screens.profile.EditProfileScreen
import com.example.matchpet.ui.screens.profile.ProfileScreen
import com.example.matchpet.ui.screens.auth.AuthViewModel

@Composable
fun MatchPetApp(
    startDestination: String,
    snackbarHost: @Composable () -> Unit,
    authViewModel: AuthViewModel,
    onGoogleSignIn: () -> Unit
) {
    val navController = rememberNavController()

    Surface {
        androidx.compose.material3.Scaffold(
            snackbarHost = snackbarHost,
            content = { paddingValues ->
                MatchPetNavHost(
                    navController = navController,
                    startDestination = startDestination,
                    modifier = Modifier.padding(paddingValues),
                    authViewModel = authViewModel,
                    onGoogleSignIn = onGoogleSignIn
                )
            }
        )
    }
}

@Composable
private fun MatchPetNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    onGoogleSignIn: () -> Unit
) {
    val authState by authViewModel.authState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(MatchPetDestinations.Home.route) {
            HomeScreen(
                onAdoptarClick = { navController.navigate(MatchPetDestinations.Register.route) },
                onLoginClick = { navController.navigate(MatchPetDestinations.Login.route) }
            )
        }
        composable(MatchPetDestinations.Register.route) {
            RegisterScreen(
                state = authState.register,
                onEvent = authViewModel::onRegisterEvent,
                onBackClick = { navController.popBackStack() },
                onLoginClick = {
                    navController.navigate(MatchPetDestinations.Login.route) {
                        popUpTo(MatchPetDestinations.Home.route)
                    }
                },
                onSuccessNavigate = {
                    navController.navigate(MatchPetDestinations.Profile.route) {
                        popUpTo(MatchPetDestinations.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(MatchPetDestinations.Login.route) {
            LoginScreen(
                state = authState.login,
                onEvent = authViewModel::onLoginEvent,
                onRegisterClick = {
                    navController.navigate(MatchPetDestinations.Register.route)
                },
                onBackClick = { navController.popBackStack() },
                onGoogleSignInClick = onGoogleSignIn,
                onSuccessNavigate = {
                    navController.navigate(MatchPetDestinations.Profile.route) {
                        popUpTo(MatchPetDestinations.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(MatchPetDestinations.Profile.route) {
            ProfileScreen(
                state = authState.profile,
                onEditClick = { navController.navigate(MatchPetDestinations.ProfileEdit.route) },
                onSignOut = {
                    authViewModel.signOut()
                    navController.navigate(MatchPetDestinations.Home.route) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(MatchPetDestinations.ProfileEdit.route) {
            EditProfileScreen(
                state = authState.editProfile,
                onEvent = authViewModel::onEditProfileEvent,
                onBack = { navController.popBackStack() },
                onSaveSuccess = {
                    navController.popBackStack()
                }
            )
        }
    }
}
