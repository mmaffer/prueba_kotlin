package com.example.matchpet.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchpet.ui.theme.Coral
import com.example.matchpet.ui.theme.DarkBlue
import com.example.matchpet.ui.theme.LightBlue

@Composable
fun LoginScreen(
    state: LoginUiState,
    onEvent: (LoginEvent) -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
    onGoogleSignInClick: () -> Unit,
    onSuccessNavigate: () -> Unit
) {
    LaunchedEffect(state.success) {
        if (state.success) {
            onSuccessNavigate()
            onEvent(LoginEvent.SuccessConsumed)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Iniciar sesión", color = DarkBlue) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atrás", tint = DarkBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightBlue
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bienvenido de nuevo",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.email,
                onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Correo electrónico") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation()
            )

            if (state.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(state.error, color = Coral)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onEvent(LoginEvent.Submit) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
            ) {
                Text(if (state.isLoading) "Ingresando..." else "Iniciar sesión")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Divider()

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onGoogleSignInClick,
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Coral)
            ) {
                Text("Continuar con Google")
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onRegisterClick) {
                Text("¿No tienes cuenta? Regístrate", color = DarkBlue)
            }
        }
    }
}
