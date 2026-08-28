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
fun DetalleClubScreen(
    clubId: String,
    viewModel: MiTecmiViewModel,
    onVolver: () -> Unit
) {

    val clubes by viewModel.clubes.collectAsState()

    val club = clubes.firstOrNull {
        it.id == clubId
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

        if (club == null) {

            Text(
                text = "No se encontró el club.",
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
                text = "CLUB ESTUDIANTIL",
                color = TecmiGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = club.nombre,
                color = TecmiDarkGreen,
                fontSize = 27.sp,
                lineHeight = 33.sp,
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
                        text = "Acerca del club",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TecmiDarkGreen
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Text(
                        text = club.descripcion,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )

                    Spacer(
                        modifier = Modifier.height(22.dp)
                    )

                    Text(
                        text = "Categoría",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TecmiDarkGreen
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = club.categoria,
                        fontSize = 14.sp
                    )

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    Text(
                        text = "Horario de reuniones",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TecmiDarkGreen
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = club.horario,
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
                        text = club.contacto,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}