package com.julm.mitecmi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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

data class ResultadoBusqueda(
    val id: String,
    val tipo: String,
    val titulo: String,
    val descripcion: String,
    val extra: String
)

@Composable
fun BuscarScreen(
    viewModel: MiTecmiViewModel,
    onAbrirResultado: (
        tipo: String,
        id: String
    ) -> Unit
) {

    val propuestas by viewModel.propuestas.collectAsState()
    val eventos by viewModel.eventos.collectAsState()
    val encuestas by viewModel.encuestas.collectAsState()
    val clubes by viewModel.clubes.collectAsState()
    val anuncios by viewModel.anuncios.collectAsState()
    val objetos by viewModel.objetosPerdidos.collectAsState()

    var textoBusqueda by remember {
        mutableStateOf("")
    }

    val todosLosResultados = buildList {

        eventos.forEach { evento ->

            add(
                ResultadoBusqueda(
                    id = evento.id,
                    tipo = "EVENTO",
                    titulo = evento.titulo,
                    descripcion = evento.descripcion,
                    extra =
                        "${evento.fecha} · ${evento.ubicacion}"
                )
            )
        }

        propuestas.forEach { propuesta ->

            add(
                ResultadoBusqueda(
                    id = propuesta.id,
                    tipo = "PROPUESTA",
                    titulo = propuesta.titulo,
                    descripcion = propuesta.descripcion,
                    extra =
                        "${propuesta.votos} votos"
                )
            )
        }

        encuestas.forEach { encuesta ->

            add(
                ResultadoBusqueda(
                    id = encuesta.id,
                    tipo = "ENCUESTA",
                    titulo = encuesta.pregunta,
                    descripcion =
                        "Participa en esta encuesta del Consejo Estudiantil.",
                    extra =
                        "Cierra: ${encuesta.fechaCierre}"
                )
            )
        }

        clubes.forEach { club ->

            add(
                ResultadoBusqueda(
                    id = club.id,
                    tipo = "CLUB",
                    titulo = club.nombre,
                    descripcion = club.descripcion,
                    extra = club.categoria
                )
            )
        }

        anuncios.forEach { anuncio ->

            add(
                ResultadoBusqueda(
                    id = anuncio.id,
                    tipo = "ANUNCIO",
                    titulo = anuncio.titulo,
                    descripcion = anuncio.contenido,
                    extra = anuncio.fecha
                )
            )
        }

        objetos.forEach { objeto ->

            add(
                ResultadoBusqueda(
                    id = objeto.id,
                    tipo = "OBJETO",
                    titulo = objeto.descripcion,
                    descripcion =
                        "Ubicación: ${objeto.ubicacion}",
                    extra =
                        "${objeto.tipo} · ${objeto.fecha}"
                )
            )
        }
    }

    val resultadosFiltrados =
        if (textoBusqueda.isBlank()) {

            todosLosResultados

        } else {

            todosLosResultados.filter { resultado ->

                resultado.titulo.contains(
                    textoBusqueda,
                    ignoreCase = true
                ) ||

                        resultado.descripcion.contains(
                            textoBusqueda,
                            ignoreCase = true
                        ) ||

                        resultado.tipo.contains(
                            textoBusqueda,
                            ignoreCase = true
                        ) ||

                        resultado.extra.contains(
                            textoBusqueda,
                            ignoreCase = true
                        )
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TecmiBackground)
    ) {

        BuscarHeader()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),

            contentPadding = PaddingValues(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {

            item {

                OutlinedTextField(
                    value = textoBusqueda,

                    onValueChange = {
                        textoBusqueda = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    placeholder = {
                        Text(
                            text =
                                "Buscar eventos, clubes, propuestas..."
                        )
                    },

                    singleLine = true,

                    shape =
                        RoundedCornerShape(24.dp)
                )
            }

            item {

                Text(
                    text =
                        if (
                            textoBusqueda.isBlank()
                        ) {
                            "Explorar Mi Tecmi"
                        } else {
                            "Resultados"
                        },

                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TecmiDarkGreen
                )
            }

            if (
                resultadosFiltrados.isEmpty()
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
                            )
                    ) {

                        Text(
                            text =
                                "No encontramos resultados para \"$textoBusqueda\".",

                            modifier =
                                Modifier.padding(20.dp)
                        )
                    }
                }

            } else {

                items(
                    items = resultadosFiltrados,
                    key = {
                        "${it.tipo}_${it.id}"
                    }
                ) { resultado ->

                    ResultadoBusquedaCard(
                        resultado = resultado,

                        onAbrir = {
                            onAbrirResultado(
                                resultado.tipo,
                                resultado.id
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BuscarHeader() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(20.dp)
    ) {

        Text(
            text = "Buscar",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TecmiDarkGreen
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        Text(
            text =
                "Encuentra contenido dentro de Mi Tecmi",
            fontSize = 13.sp,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ResultadoBusquedaCard(
    resultado: ResultadoBusqueda,
    onAbrir: () -> Unit
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
                text = resultado.tipo,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TecmiDarkGreen
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = resultado.titulo,
                fontSize = 18.sp,
                lineHeight = 23.sp,
                fontWeight = FontWeight.Bold,
                color = TecmiDarkGreen
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = resultado.descripcion,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = resultado.extra,
                fontSize = 13.sp,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (
                resultado.tipo != "ANUNCIO"
            ) {

                TextButton(
                    onClick = onAbrir
                ) {

                    Text(
                        text =
                            when (resultado.tipo) {

                                "EVENTO" ->
                                    "Ver eventos"

                                "PROPUESTA" ->
                                    "Ver propuesta"

                                "ENCUESTA" ->
                                    "Abrir encuesta"

                                "CLUB" ->
                                    "Ver club"

                                "OBJETO" ->
                                    "Ver información"

                                else ->
                                    "Ver más"
                            },

                        color = TecmiGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}