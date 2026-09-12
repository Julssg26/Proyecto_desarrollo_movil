package com.julm.mitecmi.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.julm.mitecmi.ui.components.BottomNavigationBar
import com.julm.mitecmi.ui.screens.BuscarScreen
import com.julm.mitecmi.ui.screens.ComunidadScreen
import com.julm.mitecmi.ui.screens.CrearObjetoScreen
import com.julm.mitecmi.ui.screens.CrearPropuestaScreen
import com.julm.mitecmi.ui.screens.DetalleClubScreen
import com.julm.mitecmi.ui.screens.DetalleObjetoScreen
import com.julm.mitecmi.ui.screens.DetallePropuestaScreen
import com.julm.mitecmi.ui.screens.EncuestaScreen
import com.julm.mitecmi.ui.screens.EventosScreen
import com.julm.mitecmi.ui.screens.HomeScreen
import com.julm.mitecmi.ui.screens.LoginScreen
import com.julm.mitecmi.ui.screens.PerfilScreen
import com.julm.mitecmi.viewmodel.AuthViewModel
import com.julm.mitecmi.viewmodel.MiTecmiViewModel

object Routes {

    const val INICIO = "inicio"
    const val BUSCAR = "buscar"
    const val EVENTOS = "eventos"
    const val COMUNIDAD = "comunidad"
    const val PERFIL = "perfil"

    const val CREAR_PROPUESTA =
        "crear_propuesta"

    const val CREAR_OBJETO =
        "crear_objeto"

    const val DETALLE_PROPUESTA =
        "detalle_propuesta/{propuestaId}"

    const val ENCUESTA =
        "encuesta/{encuestaId}"

    const val DETALLE_CLUB =
        "detalle_club/{clubId}"

    const val DETALLE_OBJETO =
        "detalle_objeto/{objetoId}"

    fun detallePropuesta(
        propuestaId: String
    ): String {

        return "detalle_propuesta/$propuestaId"
    }

    fun encuesta(
        encuestaId: String
    ): String {

        return "encuesta/$encuestaId"
    }

    fun detalleClub(
        clubId: String
    ): String {

        return "detalle_club/$clubId"
    }

    fun detalleObjeto(
        objetoId: String
    ): String {

        return "detalle_objeto/$objetoId"
    }
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = viewModel(),
    viewModel: MiTecmiViewModel = viewModel()
) {

    val authState by
    authViewModel.uiState.collectAsState()

    if (!authState.isAuthenticated) {
        LoginScreen(
            authState = authState,
            onLogin = authViewModel::login,
            onRegister = authViewModel::register,
            onResetPassword = authViewModel::resetPassword,
            onResendVerification = authViewModel::resendVerificationEmail,
            onCheckEmailVerification = authViewModel::checkEmailVerification,
            onSignOut = authViewModel::signOut,
            onClearError = authViewModel::clearError,
            onClearPasswordResetState = authViewModel::clearPasswordResetState
        )
        return
    }

    val navController =
        rememberNavController()

    val navBackStackEntry by
    navController
        .currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry
            ?.destination
            ?.route

    val rutasPrincipales = listOf(
        Routes.INICIO,
        Routes.BUSCAR,
        Routes.EVENTOS,
        Routes.COMUNIDAD,
        Routes.PERFIL
    )

    val mostrarBottomBar =
        currentRoute in rutasPrincipales

    Scaffold(
        bottomBar = {

            if (mostrarBottomBar) {

                BottomNavigationBar(
                    navController =
                        navController
                )
            }
        }
    ) { innerPadding ->

        AppNavHost(
            navController = navController,
            innerPadding = innerPadding,
            viewModel = viewModel,
            userName = authState.userName,
            userEmail = authState.userEmail,
            onSignOut = authViewModel::signOut
        )
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    viewModel: MiTecmiViewModel,
    userName: String,
    userEmail: String,
    onSignOut: () -> Unit
) {

    NavHost(
        navController = navController,

        startDestination =
            Routes.INICIO,

        modifier =
            Modifier.padding(innerPadding)
    ) {

        composable(
            route = Routes.INICIO
        ) {

            HomeScreen(
                viewModel = viewModel,

                onAbrirPropuesta = {
                        propuestaId ->

                    navController.navigate(
                        Routes.detallePropuesta(
                            propuestaId
                        )
                    )
                },

                onAbrirEncuesta = {
                        encuestaId ->

                    navController.navigate(
                        Routes.encuesta(
                            encuestaId
                        )
                    )
                },

                onAbrirClub = {
                        clubId ->

                    navController.navigate(
                        Routes.detalleClub(
                            clubId
                        )
                    )
                },

                onAbrirObjeto = {
                        objetoId ->

                    navController.navigate(
                        Routes.detalleObjeto(
                            objetoId
                        )
                    )
                },

                onIrEventos = {

                    navController.navigateBottomBar(
                        Routes.EVENTOS
                    )
                }
            )
        }

        composable(
            route = Routes.BUSCAR
        ) {

            BuscarScreen(
                viewModel = viewModel,

                onAbrirResultado = {
                        tipo,
                        id ->

                    when (tipo) {

                        "EVENTO" -> {

                            navController.navigateBottomBar(
                                Routes.EVENTOS
                            )
                        }

                        "PROPUESTA" -> {

                            navController.navigate(
                                Routes.detallePropuesta(
                                    id
                                )
                            )
                        }

                        "ENCUESTA" -> {

                            navController.navigate(
                                Routes.encuesta(
                                    id
                                )
                            )
                        }

                        "CLUB" -> {

                            navController.navigate(
                                Routes.detalleClub(
                                    id
                                )
                            )
                        }

                        "OBJETO" -> {

                            navController.navigate(
                                Routes.detalleObjeto(
                                    id
                                )
                            )
                        }
                    }
                }
            )
        }

        composable(
            route = Routes.EVENTOS
        ) {

            EventosScreen(
                viewModel = viewModel
            )
        }

        composable(
            route = Routes.COMUNIDAD
        ) {

            ComunidadScreen(
                viewModel = viewModel,

                onCrearPropuesta = {

                    navController.navigate(
                        Routes.CREAR_PROPUESTA
                    )
                },

                onAbrirPropuesta = {
                        propuestaId ->

                    navController.navigate(
                        Routes.detallePropuesta(
                            propuestaId
                        )
                    )
                },

                onAbrirEncuesta = {
                        encuestaId ->

                    navController.navigate(
                        Routes.encuesta(
                            encuestaId
                        )
                    )
                },

                onAbrirClub = {
                        clubId ->

                    navController.navigate(
                        Routes.detalleClub(
                            clubId
                        )
                    )
                },

                onCrearObjeto = {

                    navController.navigate(
                        Routes.CREAR_OBJETO
                    )
                },

                onAbrirObjeto = {
                        objetoId ->

                    navController.navigate(
                        Routes.detalleObjeto(
                            objetoId
                        )
                    )
                }
            )
        }

        composable(
            route = Routes.PERFIL
        ) {

                PerfilScreen(
                    viewModel = viewModel,
                    userName = userName,
                    userEmail = userEmail,
                    onSignOut = onSignOut
                )
            }

        composable(
            route =
                Routes.CREAR_PROPUESTA
        ) {

            CrearPropuestaScreen(
                viewModel = viewModel,

                onVolver = {
                    navController.popBackStack()
                },

                onPropuestaCreada = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route =
                Routes.DETALLE_PROPUESTA
        ) { backStackEntry ->

            val propuestaId =
                backStackEntry
                    .arguments
                    ?.getString(
                        "propuestaId"
                    )
                    .orEmpty()

            DetallePropuestaScreen(
                propuestaId =
                    propuestaId,

                viewModel =
                    viewModel,

                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route =
                Routes.ENCUESTA
        ) { backStackEntry ->

            val encuestaId =
                backStackEntry
                    .arguments
                    ?.getString(
                        "encuestaId"
                    )
                    .orEmpty()

            EncuestaScreen(
                encuestaId =
                    encuestaId,

                viewModel =
                    viewModel,

                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route =
                Routes.DETALLE_CLUB
        ) { backStackEntry ->

            val clubId =
                backStackEntry
                    .arguments
                    ?.getString(
                        "clubId"
                    )
                    .orEmpty()

            DetalleClubScreen(
                clubId = clubId,

                viewModel =
                    viewModel,

                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route =
                Routes.CREAR_OBJETO
        ) {

            CrearObjetoScreen(
                viewModel = viewModel,

                onVolver = {
                    navController.popBackStack()
                },

                onObjetoCreado = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route =
                Routes.DETALLE_OBJETO
        ) { backStackEntry ->

            val objetoId =
                backStackEntry
                    .arguments
                    ?.getString(
                        "objetoId"
                    )
                    .orEmpty()

            DetalleObjetoScreen(
                objetoId =
                    objetoId,

                viewModel =
                    viewModel,

                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}

fun NavHostController.navigateBottomBar(
    route: String
) {

    navigate(route) {

        popUpTo(
            graph
                .findStartDestination()
                .id
        ) {

            saveState = true
        }

        launchSingleTop = true
        restoreState = true
    }
}
