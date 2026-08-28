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
fun DetallePropuestaScreen(
    propuestaId: String,
    viewModel: MiTecmiViewModel,
    onVolver: () -> Unit
) {

    val propuestas by viewModel.propuestas.collectAsState()

    val propuesta = propuestas.firstOrNull {
        it.id == propuestaId
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TecmiBackground)
    ) {

        Row(
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

        if (propuesta == null) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                Text(
                    text = "No se encontró la propuesta.",
                    color = TecmiDarkGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            return
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Propuesta",
                color = TecmiGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = propuesta.titulo,
                color = TecmiDarkGreen,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Publicada por ${propuesta.autorNombre}",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = propuesta.fecha,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                        text = propuesta.descripcion,
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )

                    Spacer(
                        modifier = Modifier.height(22.dp)
                    )

                    Text(
                        text = "${propuesta.votos} votos",
                        color = TecmiDarkGreen,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Button(
                        onClick = {
                            viewModel.votarPropuesta(
                                propuesta.id
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (
                                propuesta.votadaPorUsuario
                            ) {
                                TecmiDarkGreen
                            } else {
                                TecmiGreen
                            }
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {

                        Text(
                            text = if (
                                propuesta.votadaPorUsuario
                            ) {
                                "▲ Votado"
                            } else {
                                "▲ Votar propuesta"
                            }
                        )
                    }

                    if (propuesta.votadaPorUsuario) {

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "Tu voto ha sido registrado. Puedes volver a presionar el botón para retirarlo.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}