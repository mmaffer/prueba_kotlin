package com.example.matchpet.ui.screens.profile

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchpet.ui.screens.auth.EditProfileEvent
import com.example.matchpet.ui.screens.auth.EditProfileUiState
import com.example.matchpet.ui.theme.Coral
import com.example.matchpet.ui.theme.DarkBlue
import com.example.matchpet.ui.theme.LightBlue

@Composable
fun EditProfileScreen(
    state: EditProfileUiState,
    onEvent: (EditProfileEvent) -> Unit,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    LaunchedEffect(state.message) {
        if (state.message != null) {
            onSaveSuccess()
            onEvent(EditProfileEvent.MessageConsumed)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar perfil", color = DarkBlue) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atrás", tint = DarkBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LightBlue)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Actualiza tu información para mejorar tus recomendaciones",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkBlue,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = state.name,
                onValueChange = { onEvent(EditProfileEvent.NameChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombre") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.phone,
                onValueChange = { onEvent(EditProfileEvent.PhoneChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Teléfono") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.bio,
                onValueChange = { onEvent(EditProfileEvent.BioChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Biografía") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.preferences,
                onValueChange = { onEvent(EditProfileEvent.PreferencesChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Preferencias de adopción") }
            )

            if (state.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(state.error, color = Coral)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onEvent(EditProfileEvent.Submit) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
            ) {
                Text(if (state.isLoading) "Guardando..." else "Guardar cambios")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onBack) {
                Text("Cancelar", color = DarkBlue)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
