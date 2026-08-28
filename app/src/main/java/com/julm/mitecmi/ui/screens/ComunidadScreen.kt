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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.julm.mitecmi.model.ObjetoPerdido
import com.julm.mitecmi.model.Propuesta
import com.julm.mitecmi.ui.theme.TecmiBackground
import com.julm.mitecmi.ui.theme.TecmiDarkGreen
import com.julm.mitecmi.ui.theme.TecmiGreen
import com.julm.mitecmi.ui.theme.White
import com.julm.mitecmi.viewmodel.MiTecmiViewModel

@Composable
fun ComunidadScreen(
    viewModel: MiTecmiViewModel,
    onCrearPropuesta: () -> Unit,
    onAbrirPropuesta: (String) -> Unit,
    onAbrirEncuesta: (String) -> Unit,
    onAbrirClub: (String) -> Unit,
    onCrearObjeto: () -> Unit,
    onAbrirObjeto: (String) -> Unit
) {

    val propuestas by viewModel.propuestas.collectAsState()
    val encuestas by viewModel.encuestas.collectAsState()
    val clubes by viewModel.clubes.collectAsState()
    val anuncios by viewModel.anuncios.collectAsState()
    val objetos by viewModel.objetosPerdidos.collectAsState()

    var categoria by remember {
        mutableStateOf("Propuestas")
    }

    val categorias = listOf(
        "Propuestas",
        "Encuestas",
        "Clubes",
        "Anuncios",
        "Objetos"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TecmiBackground)
    ) {

        ComunidadHeader()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            item {

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(categorias) { item ->

                        FilterChip(
                            selected = categoria == item,
                            onClick = {
                                categoria = item
                            },
                            label = {
                                Text(
                                    text = item
                                )
                            }
                        )
                    }
                }
            }

            if (categoria == "Propuestas") {

                item {

                    Button(
                        onClick = onCrearPropuesta,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TecmiGreen
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {

                        Text(
                            text = "+ Nueva propuesta",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (categoria == "Objetos") {

                item {

                    Button(
                        onClick = onCrearObjeto,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TecmiGreen
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {

                        Text(
                            text = "+ Publicar objeto",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            when (categoria) {

                "Propuestas" -> {

                    items(
                        items = propuestas,
                        key = {
                            it.id
                        }
                    ) { propuesta ->

                        ComunidadPropuestaCard(
                            propuesta = propuesta,

                            onVotar = {
                                viewModel.votarPropuesta(
                                    propuesta.id
                                )
                            },

                            onVerDetalle = {
                                onAbrirPropuesta(
                                    propuesta.id
                                )
                            }
                        )
                    }
                }

                "Encuestas" -> {

                    items(
                        items = encuestas,
                        key = {
                            it.id
                        }
                    ) { encuesta ->

                        ComunidadEncuestaCard(
                            encuesta = encuesta,

                            onAbrir = {
                                onAbrirEncuesta(
                                    encuesta.id
                                )
                            }
                        )
                    }
                }

                "Clubes" -> {

                    items(
                        items = clubes,
                        key = {
                            it.id
                        }
                    ) { club ->

                        ComunidadClubCard(
                            club = club,

                            onAbrir = {
                                onAbrirClub(
                                    club.id
                                )
                            }
                        )
                    }
                }

                "Anuncios" -> {

                    items(
                        items = anuncios,
                        key = {
                            it.id
                        }
                    ) { anuncio ->

                        ComunidadAnuncioCard(
                            anuncio = anuncio
                        )
                    }
                }

                "Objetos" -> {

                    items(
                        items = objetos,
                        key = {
                            it.id
                        }
                    ) { objeto ->

                        ComunidadObjetoCard(
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
}

@Composable
private fun ComunidadHeader() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(20.dp)
    ) {

        Text(
            text = "Comunidad",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TecmiDarkGreen
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        Text(
            text = "Participa y conecta con otros estudiantes",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ComunidadPropuestaCard(
    propuesta: Propuesta,
    onVotar: () -> Unit,
    onVerDetalle: () -> Unit
) {

    CommunityCard(
        tipo = "PROPUESTA",
        titulo = propuesta.titulo,
        descripcion = propuesta.descripcion,
        extra = "${propuesta.votos} votos · ${propuesta.autorNombre}"
    ) {

        Row {

            TextButton(
                onClick = onVotar
            ) {

                Text(
                    text = if (
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
                onClick = onVerDetalle
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
private fun ComunidadEncuestaCard(
    encuesta: Encuesta,
    onAbrir: () -> Unit
) {

    val votos =
        encuesta.opciones.sumOf {
            it.votos
        }

    CommunityCard(
        tipo = "ENCUESTA",
        titulo = encuesta.pregunta,
        descripcion = "Participa en esta encuesta del Consejo Estudiantil.",
        extra = "$votos respuestas · Cierra ${encuesta.fechaCierre}"
    ) {

        TextButton(
            onClick = onAbrir
        ) {

            Text(
                text = if (
                    encuesta.usuarioYaVoto
                ) {
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
private fun ComunidadClubCard(
    club: Club,
    onAbrir: () -> Unit
) {

    CommunityCard(
        tipo = "CLUB",
        titulo = club.nombre,
        descripcion = club.descripcion,
        extra = "${club.categoria} · ${club.horario}"
    ) {

        TextButton(
            onClick = onAbrir
        ) {

            Text(
                text = "Ver club",
                color = TecmiGreen,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ComunidadAnuncioCard(
    anuncio: Anuncio
) {

    CommunityCard(
        tipo = "ANUNCIO",
        titulo = anuncio.titulo,
        descripcion = anuncio.contenido,
        extra = "${anuncio.fecha} · ${anuncio.facultad}"
    )
}

@Composable
private fun ComunidadObjetoCard(
    objeto: ObjetoPerdido,
    onAbrir: () -> Unit
) {

    CommunityCard(
        tipo = objeto.tipo.uppercase(),
        titulo = objeto.descripcion,
        descripcion = "Ubicación aproximada: ${objeto.ubicacion}",
        extra = "${objeto.fecha} · ${objeto.estado}"
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
private fun CommunityCard(
    tipo: String,
    titulo: String,
    descripcion: String,
    extra: String,
    content: @Composable () -> Unit = {}
) {

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
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TecmiDarkGreen
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Text(
                text = descripcion,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = extra,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            content()
        }
    }
}