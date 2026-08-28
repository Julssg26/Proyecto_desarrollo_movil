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
import com.julm.mitecmi.model.Evento
import com.julm.mitecmi.ui.theme.TecmiBackground
import com.julm.mitecmi.ui.theme.TecmiDarkGreen
import com.julm.mitecmi.ui.theme.TecmiGreen
import com.julm.mitecmi.ui.theme.White
import com.julm.mitecmi.viewmodel.MiTecmiViewModel

@Composable
fun EventosScreen(
    viewModel: MiTecmiViewModel
) {

    val eventos by viewModel.eventos.collectAsState()

    var filtroSeleccionado by remember {
        mutableStateOf("Todos")
    }

    val filtros = listOf(
        "Todos",
        "Académico",
        "Deportivo"
    )

    val eventosFiltrados = if (filtroSeleccionado == "Todos") {

        eventos

    } else {

        eventos.filter {
            it.categoria.equals(
                filtroSeleccionado,
                ignoreCase = true
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TecmiBackground)
    ) {

        EventosHeader()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),

            contentPadding = PaddingValues(16.dp),

            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            item {

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(filtros) { filtro ->

                        FilterChip(
                            selected =
                                filtroSeleccionado == filtro,

                            onClick = {
                                filtroSeleccionado = filtro
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

            items(eventosFiltrados) { evento ->

                EventoDetalleCard(
                    evento = evento,

                    onInteresClick = {
                        viewModel.cambiarInteresEvento(
                            evento.id
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun EventosHeader() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(20.dp)
    ) {

        Text(
            text = "Eventos",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TecmiDarkGreen
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        Text(
            text = "Descubre lo que sucede en el campus",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun EventoDetalleCard(
    evento: Evento,
    onInteresClick: () -> Unit
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
                text = evento.categoria.uppercase(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TecmiDarkGreen
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = evento.titulo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TecmiDarkGreen
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = evento.descripcion,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = "Fecha: ${evento.fecha}",
                fontSize = 13.sp
            )

            Text(
                text = "Hora: ${evento.hora}",
                fontSize = 13.sp
            )

            Text(
                text = "Lugar: ${evento.ubicacion}",
                fontSize = 13.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "${evento.interesados} estudiantes interesados",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            TextButton(
                onClick = onInteresClick
            ) {

                Text(
                    text = if (evento.usuarioInteresado) {
                        "✓ Me interesa"
                    } else {
                        "Me interesa"
                    },

                    color = TecmiGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}