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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
fun EncuestaScreen(
    encuestaId: String,
    viewModel: MiTecmiViewModel,
    onVolver: () -> Unit
) {

    val encuestas by viewModel.encuestas.collectAsState()

    val encuesta = encuestas.firstOrNull {
        it.id == encuestaId
    }

    var opcionSeleccionada by remember {
        mutableStateOf<String?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TecmiBackground)
    ) {

        TextButton(
            onClick = onVolver,
            modifier = Modifier.padding(
                start = 8.dp,
                top = 8.dp
            )
        ) {
            Text(
                text = "← Volver",
                color = TecmiDarkGreen,
                fontWeight = FontWeight.Bold
            )
        }

        if (encuesta == null) {

            Text(
                text = "No se encontró la encuesta.",
                modifier = Modifier.padding(20.dp),
                color = TecmiDarkGreen,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            return
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Encuesta",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TecmiGreen
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = encuesta.pregunta,
                fontSize = 25.sp,
                lineHeight = 31.sp,
                fontWeight = FontWeight.Bold,
                color = TecmiDarkGreen
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Cierre: ${encuesta.fechaCierre}",
                fontSize = 13.sp,
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
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    if (!encuesta.usuarioYaVoto) {

                        encuesta.opciones.forEach { opcion ->

                            androidx.compose.foundation.layout.Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = 5.dp
                                    )
                            ) {

                                RadioButton(
                                    selected =
                                        opcionSeleccionada == opcion.id,

                                    onClick = {
                                        opcionSeleccionada = opcion.id
                                    }
                                )

                                Text(
                                    text = opcion.texto,
                                    modifier = Modifier.padding(
                                        top = 12.dp
                                    ),
                                    fontSize = 15.sp
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        Button(
                            onClick = {

                                opcionSeleccionada?.let {
                                        opcionId ->

                                    viewModel.votarEncuesta(
                                        encuestaId = encuesta.id,
                                        opcionId = opcionId
                                    )
                                }
                            },

                            enabled =
                                opcionSeleccionada != null,

                            modifier =
                                Modifier.fillMaxWidth(),

                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = TecmiGreen
                                ),

                            shape = RoundedCornerShape(16.dp)
                        ) {

                            Text(
                                text = "Enviar voto",
                                fontWeight = FontWeight.Bold
                            )
                        }

                    } else {

                        Text(
                            text = "Resultados",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TecmiDarkGreen
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        val totalVotos =
                            encuesta.opciones.sumOf {
                                it.votos
                            }

                        encuesta.opciones.forEach { opcion ->

                            val porcentaje =
                                if (totalVotos == 0) {
                                    0
                                } else {
                                    (
                                            opcion.votos * 100
                                                    / totalVotos
                                            )
                                }

                            Text(
                                text =
                                    "${opcion.texto}: $porcentaje% (${opcion.votos} votos)",

                                modifier = Modifier.padding(
                                    vertical = 6.dp
                                ),

                                fontSize = 14.sp
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = "Tu voto ya fue registrado.",
                            fontSize = 13.sp,
                            color = TecmiGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}