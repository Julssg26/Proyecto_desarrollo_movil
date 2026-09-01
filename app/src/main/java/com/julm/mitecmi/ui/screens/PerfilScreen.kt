package com.julm.mitecmi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
fun PerfilScreen(
    viewModel: MiTecmiViewModel,
    userName: String,
    userEmail: String,
    onSignOut: () -> Unit
) {

    val propuestas by
    viewModel.propuestas.collectAsState()

    val eventos by
    viewModel.eventos.collectAsState()

    val encuestas by
    viewModel.encuestas.collectAsState()

    val objetos by
    viewModel.objetosPerdidos.collectAsState()

    val propuestasUsuario =
        propuestas.count {
            it.autorId == "usuario_actual"
        }

    val eventosInteresados =
        eventos.count {
            it.usuarioInteresado
        }

    val encuestasRespondidas =
        encuestas.count {
            it.usuarioYaVoto
        }

    val objetosPublicados =
        objetos.count {
            it.autorId == "usuario_actual"
        }

    val votosEmitidos =
        propuestas.count {
            it.votadaPorUsuario
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TecmiBackground)
    ) {

        PerfilHeader()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),

            contentPadding =
                PaddingValues(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {

            item {

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(20.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor = White
                        ),

                    elevation =
                        CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                ) {

                    Column(
                        modifier =
                            Modifier.padding(20.dp)
                    ) {

                        Text(
                            text =
                                userName.ifBlank {
                                    "Estudiante Tecmilenio"
                                },

                            fontSize = 22.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color = TecmiDarkGreen
                        )

                        Spacer(
                            modifier =
                                Modifier.height(6.dp)
                        )

                        Text(
                            text =
                                "Ingeniería en Desarrollo de Software",

                            fontSize = 14.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.height(3.dp)
                        )

                        Text(
                            text = "Campus Veracruz",

                            fontSize = 13.sp,

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onSurfaceVariant
                        )

                        if (userEmail.isNotBlank()) {
                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            Text(
                                text = userEmail,
                                fontSize = 13.sp,
                                color =
                                    MaterialTheme
                                        .colorScheme
                                        .onSurfaceVariant
                            )
                        }
                    }
                }
            }

            item {

                Text(
                    text = "Mi actividad",

                    fontSize = 19.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color = TecmiDarkGreen
                )
            }

            item {

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    PerfilMiniCard(
                        titulo = "Eventos",
                        valor =
                            eventosInteresados.toString(),
                        modifier =
                            Modifier.weight(1f)
                    )

                    PerfilMiniCard(
                        titulo = "Encuestas",
                        valor =
                            encuestasRespondidas.toString(),
                        modifier =
                            Modifier.weight(1f)
                    )
                }
            }

            item {

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    PerfilMiniCard(
                        titulo = "Propuestas",
                        valor =
                            propuestasUsuario.toString(),
                        modifier =
                            Modifier.weight(1f)
                    )

                    PerfilMiniCard(
                        titulo = "Objetos",
                        valor =
                            objetosPublicados.toString(),
                        modifier =
                            Modifier.weight(1f)
                    )
                }
            }

            item {

                PerfilDato(
                    titulo =
                        "Propuestas que has apoyado",

                    valor =
                        votosEmitidos.toString()
                )
            }

            item {

                PerfilDato(
                    titulo = "Puntos",

                    valor = "320"
                )
            }

            item {

                PerfilDato(
                    titulo = "Insignias",

                    valor = "5"
                )
            }

            item {

                OutlinedButton(
                    onClick = onSignOut,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "Cerrar sesion",
                        color = TecmiDarkGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(18.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor = White
                        )
                ) {

                    Column(
                        modifier =
                            Modifier.padding(18.dp)
                    ) {

                        Text(
                            text = "Insignias",

                            fontSize = 17.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color = TecmiDarkGreen
                        )

                        Spacer(
                            modifier =
                                Modifier.height(10.dp)
                        )

                        Text(
                            text =
                                "★ Participación estudiantil",

                            color = TecmiGreen,
                            fontWeight =
                                FontWeight.Medium
                        )

                        Spacer(
                            modifier =
                                Modifier.height(6.dp)
                        )

                        Text(
                            text =
                                "★ Comunidad activa",

                            color = TecmiGreen,
                            fontWeight =
                                FontWeight.Medium
                        )

                        Spacer(
                            modifier =
                                Modifier.height(6.dp)
                        )

                        Text(
                            text =
                                "★ Asistente frecuente",

                            color = TecmiGreen,
                            fontWeight =
                                FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PerfilHeader() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(20.dp)
    ) {

        Text(
            text = "Perfil",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TecmiDarkGreen
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        Text(
            text =
                "Tu actividad dentro de Mi Tecmi",

            fontSize = 13.sp,

            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PerfilMiniCard(
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier,

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor = White
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                text = valor,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = TecmiDarkGreen
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = titulo,
                fontSize = 12.sp,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PerfilDato(
    titulo: String,
    valor: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(16.dp),

        colors =
            CardDefaults.cardColors(
                containerColor = White
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),

            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text(
                text = titulo,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = valor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TecmiDarkGreen
            )
        }
    }
}
