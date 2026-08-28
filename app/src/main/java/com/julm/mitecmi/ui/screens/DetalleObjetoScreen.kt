package com.julm.mitecmi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
fun DetalleObjetoScreen(
    objetoId: String,
    viewModel: MiTecmiViewModel,
    onVolver: () -> Unit
) {

    val objetos by viewModel.objetosPerdidos.collectAsState()

    val objeto = objetos.firstOrNull {
        it.id == objetoId
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

        if (objeto == null) {

            Text(
                text = "No se encontró la publicación.",
                modifier = Modifier.padding(20.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TecmiDarkGreen
            )

            return
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = objeto.tipo.uppercase(),
                color = TecmiGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = objeto.descripcion,
                color = TecmiDarkGreen,
                fontSize = 25.sp,
                lineHeight = 31.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(20.dp),

                colors = CardDefaults.cardColors(
                    containerColor = White
                ),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = "Información",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TecmiDarkGreen
                    )

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    Text(
                        text = "Ubicación aproximada",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TecmiDarkGreen
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = objeto.ubicacion,
                        fontSize = 14.sp
                    )

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    Text(
                        text = "Estado",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TecmiDarkGreen
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = objeto.estado,
                        fontSize = 14.sp
                    )

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    Text(
                        text = "Publicado por",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TecmiDarkGreen
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = objeto.autorNombre,
                        fontSize = 14.sp
                    )

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    Text(
                        text = "Contacto",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TecmiDarkGreen
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = objeto.contacto,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    Text(
                        text = "Fecha de publicación",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TecmiDarkGreen
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = objeto.fecha,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}