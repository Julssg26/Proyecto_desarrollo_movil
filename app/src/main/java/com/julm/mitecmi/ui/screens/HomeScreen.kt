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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
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
import com.julm.mitecmi.model.Anuncio
import com.julm.mitecmi.model.Club
import com.julm.mitecmi.model.Encuesta
import com.julm.mitecmi.model.Evento
import com.julm.mitecmi.model.ObjetoPerdido
import com.julm.mitecmi.model.Propuesta
import com.julm.mitecmi.ui.theme.TecmiBackground
import com.julm.mitecmi.ui.theme.TecmiDarkGreen
import com.julm.mitecmi.ui.theme.TecmiGreen
import com.julm.mitecmi.ui.theme.White
import com.julm.mitecmi.viewmodel.MiTecmiViewModel

@Composable
fun HomeScreen(
    viewModel: MiTecmiViewModel,
    onAbrirPropuesta: (String) -> Unit,
    onAbrirEncuesta: (String) -> Unit,
    onAbrirClub: (String) -> Unit,
    onAbrirObjeto: (String) -> Unit,
    onIrEventos: () -> Unit
) {

    val propuestas by viewModel.propuestas.collectAsState()
    val eventos by viewModel.eventos.collectAsState()
    val encuestas by viewModel.encuestas.collectAsState()
    val clubes by viewModel.clubes.collectAsState()
    val anuncios by viewModel.anuncios.collectAsState()
    val objetos by viewModel.objetosPerdidos.collectAsState()

    var selectedFilter by remember {
        mutableStateOf("Todo")
    }

    val filtros = listOf(
        "Todo",
        "Eventos",
        "Propuestas",
        "Encuestas",
        "Anuncios",
        "Clubes",
        "Objetos"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TecmiBackground)
    ) {

        HomeHeader()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),

            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 24.dp
            ),

            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            item {

                LazyRow(
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    items(filtros) { filtro ->

                        FilterChip(
                            selected = selectedFilter == filtro,

                            onClick = {
                                selectedFilter = filtro
                            },

                            label = {
                                Text(
                                    text = filtro
                                )
                            }
                        )
                    }
                }
            }

            if (
                selectedFilter == "Todo" ||
                selectedFilter == "Eventos"
            ) {

                items(
                    items = eventos,
                    key = { it.id }
                ) { evento ->

                    HomeEventoCard(
                        evento = evento,

                        onInteresClick = {
                            viewModel.cambiarInteresEvento(
                                evento.id
                            )
                        },

                        onVerEventos = onIrEventos
                    )
                }
            }

            if (
                selectedFilter == "Todo" ||
                selectedFilter == "Propuestas"
            ) {

                items(
                    items = propuestas,
                    key = { it.id }
                ) { propuesta ->

                    HomePropuestaCard(
                        propuesta = propuesta,

                        onVotarClick = {
                            viewModel.votarPropuesta(
                                propuesta.id
                            )
                        },

                        onAbrir = {
                            onAbrirPropuesta(
                                propuesta.id
                            )
                        }
                    )
                }
            }

            if (
                selectedFilter == "Todo" ||
                selectedFilter == "Encuestas"
            ) {

                items(
                    items = encuestas,
                    key = { it.id }
                ) { encuesta ->

                    HomeEncuestaCard(
                        encuesta = encuesta,

                        onAbrir = {
                            onAbrirEncuesta(
                                encuesta.id
                            )
                        }
                    )
                }
            }

            if (
                selectedFilter == "Todo" ||
                selectedFilter == "Anuncios"
            ) {

                items(
                    items = anuncios,
                    key = { it.id }
                ) { anuncio ->

                    HomeAnuncioCard(
                        anuncio = anuncio
                    )
                }
            }

            if (
                selectedFilter == "Todo" ||
                selectedFilter == "Clubes"
            ) {

                items(
                    items = clubes,
                    key = { it.id }
                ) { club ->

                    HomeClubCard(
                        club = club,

                        onAbrir = {
                            onAbrirClub(
                                club.id
                            )
                        }
                    )
                }
            }

            if (
                selectedFilter == "Todo" ||
                selectedFilter == "Objetos"
            ) {

                items(
                    items = objetos,
                    key = { it.id }
                ) { objeto ->

                    HomeObjetoCard(
                        objeto = objeto,

                        onAbrir = {
                            onAbrirObjeto(
                                objeto.id
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeHeader() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 20.dp,
                bottom = 16.dp
            )
    ) {

        Text(
            text = "Mi Tecmi",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TecmiDarkGreen
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        Text(
            text = "Consejo Estudiantil · Tecmilenio",
            fontSize = 13.sp,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun HomeEventoCard(
    evento: Evento,
    onInteresClick: () -> Unit,
    onVerEventos: () -> Unit
) {

    HomeCard(
        tipo = "EVENTO",
        titulo = evento.titulo,
        descripcion = evento.descripcion,
        extra =
            "${evento.fecha} · ${evento.hora} · ${evento.ubicacion}"
    ) {

        Text(
            text =
                "${evento.interesados} estudiantes interesados",
            fontSize = 13.sp,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row {

            TextButton(
                onClick = onInteresClick
            ) {

                Text(
                    text =
                        if (evento.usuarioInteresado) {
                            "♥ Me interesa"
                        } else {
                            "♡ Me interesa"
                        },

                    color = TecmiGreen,
                    fontWeight = FontWeight.Bold
                )
            }

            TextButton(
                onClick = onVerEventos
            ) {

                Text(
                    text = "Ver eventos",
                    color = TecmiDarkGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun HomePropuestaCard(
    propuesta: Propuesta,
    onVotarClick: () -> Unit,
    onAbrir: () -> Unit
) {

    HomeCard(
        tipo = "PROPUESTA",
        titulo = propuesta.titulo,
        descripcion = propuesta.descripcion,
        extra =
            "${propuesta.votos} votos · ${propuesta.autorNombre}"
    ) {

        Row {

            TextButton(
                onClick = onVotarClick
            ) {

                Text(
                    text =
                        if (
                            propuesta.votadaPorUsuario
                        ) {
                            "▲ Votado"
                        } else {
                            "▲ Votar"
                        },

                    color = TecmiGreen,
                    fontWeight = FontWeight.Bold
                )
            }

            TextButton(
                onClick = onAbrir
            ) {

                Text(
                    text = "Ver propuesta",
                    color = TecmiDarkGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun HomeEncuestaCard(
    encuesta: Encuesta,
    onAbrir: () -> Unit
) {

    val totalVotos =
        encuesta.opciones.sumOf {
            it.votos
        }

    HomeCard(
        tipo = "ENCUESTA",
        titulo = encuesta.pregunta,
        descripcion =
            "Participa y comparte tu opinión con el Consejo Estudiantil.",
        extra =
            "$totalVotos respuestas · Cierra ${encuesta.fechaCierre}"
    ) {

        TextButton(
            onClick = onAbrir
        ) {

            Text(
                text =
                    if (encuesta.usuarioYaVoto) {
                        "Ver resultados"
                    } else {
                        "Responder encuesta"
                    },

                color = TecmiGreen,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun HomeAnuncioCard(
    anuncio: Anuncio
) {

    HomeCard(
        tipo = "ANUNCIO",
        titulo = anuncio.titulo,
        descripcion = anuncio.contenido,
        extra =
            "${anuncio.fecha} · ${anuncio.facultad}"
    )
}

@Composable
private fun HomeClubCard(
    club: Club,
    onAbrir: () -> Unit
) {

    HomeCard(
        tipo = "CLUB",
        titulo = club.nombre,
        descripcion = club.descripcion,
        extra =
            "${club.categoria} · ${club.horario}"
    ) {

        TextButton(
            onClick = onAbrir
        ) {

            Text(
                text = "Conocer club",
                color = TecmiGreen,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun HomeObjetoCard(
    objeto: ObjetoPerdido,
    onAbrir: () -> Unit
) {

    HomeCard(
        tipo = objeto.tipo.uppercase(),
        titulo = objeto.descripcion,
        descripcion =
            "Ubicación aproximada: ${objeto.ubicacion}",
        extra =
            "${objeto.fecha} · ${objeto.estado}"
    ) {

        TextButton(
            onClick = onAbrir
        ) {

            Text(
                text = "Ver información",
                color = TecmiGreen,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun HomeCard(
    tipo: String,
    titulo: String,
    descripcion: String,
    extra: String,
    content: @Composable () -> Unit = {}
) {

    Card(
        modifier = Modifier.fillMaxWidth(),

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
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                text = tipo,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TecmiDarkGreen
            )

            Spacer(
                modifier = Modifier.height(9.dp)
            )

            Text(
                text = titulo,
                fontSize = 19.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TecmiDarkGreen
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = descripcion,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = extra,
                fontSize = 13.sp,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )

            content()
        }
    }
}