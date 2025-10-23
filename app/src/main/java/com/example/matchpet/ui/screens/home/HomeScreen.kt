package com.example.matchpet.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchpet.ui.theme.Coral
import com.example.matchpet.ui.theme.DarkBlue
import com.example.matchpet.ui.theme.LightBlue
import com.example.matchpet.ui.theme.LightPink
import com.example.matchpet.ui.theme.TealAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAdoptarClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Logo",
                            tint = TealAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "MatchPet",
                            fontWeight = FontWeight.Bold,
                            color = DarkBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightBlue
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightBlue)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Pet Icon",
                tint = DarkBlue,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Encuentra a tu compañero ideal",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onAdoptarClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkBlue
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Adoptar", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onLoginClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Coral
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar sesión", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = DarkBlue
                    )
                ) {
                    Text("Soy Refugio", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Bienvenido a MatchPet",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Nuestra plataforma te ayuda a encontrar el compañero perfecto. Explora perfiles de mascotas en busca de hogar, conecta con refugios y dale una segunda oportunidad a un mejor amigo.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    FeatureCard(
                        icon = Icons.Default.Search,
                        title = "Búsqueda Inteligente",
                        description = "Encuentra a tu mascota ideal según tus preferencias y estilo de vida"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FeatureCard(
                        icon = Icons.Default.Verified,
                        title = "Refugios Verificados",
                        description = "Todos nuestros refugios están verificados para tu seguridad y tranquilidad"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FeatureCard(
                        icon = Icons.Default.Favorite,
                        title = "Adopción Responsable",
                        description = "Te guiamos en cada paso del proceso para una adopción exitosa"
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun FeatureCard(
    icon: ImageVector,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightPink
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = TealAccent,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkBlue,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}
