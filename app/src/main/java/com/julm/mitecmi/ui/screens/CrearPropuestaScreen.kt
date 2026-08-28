package com.julm.mitecmi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.julm.mitecmi.ui.theme.TecmiBackground
import com.julm.mitecmi.ui.theme.TecmiDarkGreen
import com.julm.mitecmi.ui.theme.TecmiGreen
import com.julm.mitecmi.ui.theme.White
import com.julm.mitecmi.viewmodel.MiTecmiViewModel

@Composable
fun CrearPropuestaScreen(
    viewModel: MiTecmiViewModel,
    onVolver: () -> Unit,
    onPropuestaCreada: () -> Unit
) {

    var titulo by remember {
        mutableStateOf("")
    }

    var descripcion by remember {
        mutableStateOf("")
    }

    var mostrarError by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TecmiBackground)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(
                    horizontal = 12.dp,
                    vertical = 12.dp
                )
        ) {

            TextButton(
                onClick = onVolver
            ) {

                Text(
                    text = "← Volver",
                    color = TecmiDarkGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "Nueva propuesta",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TecmiDarkGreen
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Comparte una idea o necesidad con la comunidad estudiantil.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            OutlinedTextField(
                value = titulo,
                onValueChange = {
                    titulo = it
                    mostrarError = false
                },
                label = {
                    Text("Título")
                },
                placeholder = {
                    Text(
                        text = "Ej. Más espacios para estudiar"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = {
                    descripcion = it
                    mostrarError = false
                },
                label = {
                    Text("Descripción")
                },
                placeholder = {
                    Text(
                        text = "Explica tu propuesta y por qué sería útil para los estudiantes."
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                minLines = 5,
                shape = RoundedCornerShape(16.dp)
            )

            if (mostrarError) {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Completa el título y la descripción antes de publicar.",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Button(
                onClick = {

                    if (
                        titulo.isBlank() ||
                        descripcion.isBlank()
                    ) {

                        mostrarError = true

                    } else {

                        viewModel.crearPropuesta(
                            titulo = titulo,
                            descripcion = descripcion
                        )

                        onPropuestaCreada()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TecmiGreen
                ),
                shape = RoundedCornerShape(16.dp)
            ) {

                Text(
                    text = "Publicar propuesta",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Tu propuesta se mostrará inmediatamente en la sección Comunidad durante esta sesión.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}