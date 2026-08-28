package com.julm.mitecmi.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.julm.mitecmi.navigation.Routes
import com.julm.mitecmi.navigation.navigateBottomBar
import com.julm.mitecmi.ui.theme.TecmiDarkGreen
import com.julm.mitecmi.ui.theme.White

data class BottomDestination(
    val route: String,
    val label: String
)

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {

    val destinations = listOf(
        BottomDestination(
            route = Routes.INICIO,
            label = "Inicio"
        ),

        BottomDestination(
            route = Routes.BUSCAR,
            label = "Buscar"
        ),

        BottomDestination(
            route = Routes.EVENTOS,
            label = "Eventos"
        ),

        BottomDestination(
            route = Routes.COMUNIDAD,
            label = "Comunidad"
        ),

        BottomDestination(
            route = Routes.PERFIL,
            label = "Perfil"
        )
    )

    val navBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = White,
        contentColor = TecmiDarkGreen,
        modifier = androidx.compose.ui.Modifier.navigationBarsPadding()
    ) {

        destinations.forEach { destination ->

            val selected =
                currentRoute == destination.route

            NavigationBarItem(
                selected = selected,

                onClick = {
                    navController.navigateBottomBar(
                        destination.route
                    )
                },

                icon = {
                    Text(
                        text = when (destination.route) {
                            Routes.INICIO -> "⌂"
                            Routes.BUSCAR -> "⌕"
                            Routes.EVENTOS -> "◫"
                            Routes.COMUNIDAD -> "●"
                            Routes.PERFIL -> "○"
                            else -> ""
                        },
                        fontSize = 18.sp,
                        fontWeight = if (selected) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        }
                    )
                },

                label = {
                    Text(
                        text = destination.label,
                        fontSize = 10.sp
                    )
                }
            )
        }
    }
}