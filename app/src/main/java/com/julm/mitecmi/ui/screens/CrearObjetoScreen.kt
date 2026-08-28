package com.julm.mitecmi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
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
fun CrearObjetoScreen(
    viewModel: MiTecmiViewModel,
    onVolver: () -> Unit,
    onObjetoCreado: () -> Unit
) {

    var tipo by remember {
        mutableStateOf("Perdido")
    }

    var descripcion by remember {
        mutableStateOf("")
    }

    var ubicacion by remember {
        mutableStateOf("")
    }

    var contacto by remember {
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
                    vertical = 10.dp
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Text(
                text = "Publicar objeto",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TecmiDarkGreen
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Ayuda a encontrar o devolver objetos dentro del campus.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "¿Qué deseas publicar?",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TecmiDarkGreen
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                FilterChip(
                    selected = tipo == "Perdido",

                    onClick = {
                        tipo = "Perdido"
                    },

                    label = {
                        Text("Objeto perdido")
                    }
                )

                FilterChip(
                    selected = tipo == "Encontrado",

                    onClick = {
                        tipo = "Encontrado"
                    },

                    label = {
                        Text("Objeto encontrado")
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
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
                        text = "Ej. Mochila gris con cargador y cuadernos"
                    )
                },

                modifier = Modifier.fillMaxWidth(),

                minLines = 3,

                shape = RoundedCornerShape(16.dp)
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            OutlinedTextField(
                value = ubicacion,

                onValueChange = {
                    ubicacion = it
                    mostrarError = false
                },

                label = {
                    Text("Ubicación aproximada")
                },

                placeholder = {
                    Text(
                        text = "Ej. Biblioteca"
                    )
                },

                modifier = Modifier.fillMaxWidth(),

                singleLine = true,

                shape = RoundedCornerShape(16.dp)
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            OutlinedTextField(
                value = contacto,

                onValueChange = {
                    contacto = it
                    mostrarError = false
                },

                label = {
                    Text("Contacto")
                },

                placeholder = {
                    Text(
                        text = "Correo o teléfono"
                    )
                },

                modifier = Modifier.fillMaxWidth(),

                singleLine = true,

                shape = RoundedCornerShape(16.dp)
            )

            if (mostrarError) {

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = "Completa la descripción, ubicación y contacto.",
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
                        descripcion.isBlank() ||
                        ubicacion.isBlank() ||
                        contacto.isBlank()
                    ) {

                        mostrarError = true

                    } else {

                        viewModel.crearObjetoPerdido(
                            tipo = tipo,
                            descripcion = descripcion,
                            ubicacion = ubicacion,
                            contacto = contacto
                        )

                        onObjetoCreado()
                    }
                },

                modifier = Modifier.fillMaxWidth(),

                colors = ButtonDefaults.buttonColors(
                    containerColor = TecmiGreen
                ),

                shape = RoundedCornerShape(16.dp)
            ) {

                Text(
                    text = "Publicar",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "La publicación aparecerá inmediatamente en la sección Objetos durante esta sesión.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}