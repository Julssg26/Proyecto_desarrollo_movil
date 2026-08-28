package com.julm.mitecmi.repository

import com.julm.mitecmi.model.Anuncio
import com.julm.mitecmi.model.Club
import com.julm.mitecmi.model.Encuesta
import com.julm.mitecmi.model.Evento
import com.julm.mitecmi.model.ObjetoPerdido
import com.julm.mitecmi.model.OpcionEncuesta
import com.julm.mitecmi.model.Propuesta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class FakeMiTecmiRepository : MiTecmiRepository {

    private val _propuestas = MutableStateFlow(
        listOf(
            Propuesta(
                id = "propuesta_1",
                titulo = "Más estaciones para cargar laptops",
                descripcion = "Sería útil contar con más contactos y estaciones de carga en las áreas comunes del campus.",
                autorId = "usuario_1",
                autorNombre = "Andrea Martínez",
                votos = 34,
                fecha = "27 de agosto de 2026"
            ),

            Propuesta(
                id = "propuesta_2",
                titulo = "Más espacios de descanso",
                descripcion = "Propongo agregar más espacios con mesas y lugares para descansar entre clases.",
                autorId = "usuario_2",
                autorNombre = "Carlos Hernández",
                votos = 21,
                fecha = "25 de agosto de 2026"
            )
        )
    )

    override val propuestas: StateFlow<List<Propuesta>> =
        _propuestas.asStateFlow()


    private val _eventos = MutableStateFlow(
        listOf(
            Evento(
                id = "evento_1",
                titulo = "Hackathon Tecmilenio 2026",
                descripcion = "Participa con tu equipo y desarrolla una solución tecnológica durante el fin de semana.",
                fecha = "28 de agosto de 2026",
                hora = "10:00 AM",
                ubicacion = "Auditorio principal",
                categoria = "Académico",
                interesados = 45
            ),

            Evento(
                id = "evento_2",
                titulo = "Torneo estudiantil",
                descripcion = "Participa en el torneo organizado para la comunidad Tecmilenio.",
                fecha = "30 de agosto de 2026",
                hora = "4:00 PM",
                ubicacion = "Cancha principal",
                categoria = "Deportivo",
                interesados = 68
            ),

            Evento(
                id = "evento_3",
                titulo = "Taller de empleabilidad",
                descripcion = "Aprende a mejorar tu CV y prepararte para tus próximas entrevistas.",
                fecha = "2 de septiembre de 2026",
                hora = "12:00 PM",
                ubicacion = "Sala de conferencias",
                categoria = "Académico",
                interesados = 31
            )
        )
    )

    override val eventos: StateFlow<List<Evento>> =
        _eventos.asStateFlow()


    private val _encuestas = MutableStateFlow(
        listOf(
            Encuesta(
                id = "encuesta_1",
                pregunta = "¿Qué actividad te gustaría para el próximo evento estudiantil?",
                opciones = listOf(
                    OpcionEncuesta(
                        id = "opcion_1",
                        texto = "Torneo deportivo",
                        votos = 32
                    ),

                    OpcionEncuesta(
                        id = "opcion_2",
                        texto = "Noche de juegos",
                        votos = 45
                    ),

                    OpcionEncuesta(
                        id = "opcion_3",
                        texto = "Hackathon",
                        votos = 28
                    ),

                    OpcionEncuesta(
                        id = "opcion_4",
                        texto = "Festival cultural",
                        votos = 23
                    )
                ),
                fechaCierre = "5 de septiembre de 2026"
            )
        )
    )

    override val encuestas: StateFlow<List<Encuesta>> =
        _encuestas.asStateFlow()


    private val _clubes = MutableStateFlow(
        listOf(
            Club(
                id = "club_1",
                nombre = "Club de Programación",
                descripcion = "Comunidad para estudiantes interesados en programación, tecnología y desarrollo de software.",
                contacto = "programacion@mitecmi.mx",
                categoria = "Tecnología",
                horario = "Viernes · 4:00 PM"
            ),

            Club(
                id = "club_2",
                nombre = "Club de Fotografía",
                descripcion = "Espacio para compartir técnicas, proyectos y experiencias relacionadas con fotografía.",
                contacto = "fotografia@mitecmi.mx",
                categoria = "Arte",
                horario = "Miércoles · 3:00 PM"
            ),

            Club(
                id = "club_3",
                nombre = "Hawkstrike Racing",
                descripcion = "Equipo universitario enfocado en el desarrollo de un vehículo para competencias Baja SAE.",
                contacto = "hawkstrikeracing@gmail.com",
                categoria = "Ingeniería",
                horario = "Viernes · 5:00 PM"
            )
        )
    )

    override val clubes: StateFlow<List<Club>> =
        _clubes.asStateFlow()


    private val _anuncios = MutableStateFlow(
        listOf(
            Anuncio(
                id = "anuncio_1",
                titulo = "Semana de actividades estudiantiles",
                contenido = "Consulta las actividades disponibles durante esta semana y participa con la comunidad.",
                facultad = "Todas",
                fecha = "27 de agosto de 2026",
                autorId = "consejo_estudiantil"
            ),

            Anuncio(
                id = "anuncio_2",
                titulo = "Actualización de horario de biblioteca",
                contenido = "Durante periodo de evaluaciones la biblioteca extenderá su horario de atención.",
                facultad = "Todas",
                fecha = "26 de agosto de 2026",
                autorId = "consejo_estudiantil"
            )
        )
    )

    override val anuncios: StateFlow<List<Anuncio>> =
        _anuncios.asStateFlow()


    private val _objetosPerdidos = MutableStateFlow(
        listOf(
            ObjetoPerdido(
                id = "objeto_1",
                tipo = "Encontrado",
                descripcion = "Termo negro encontrado cerca de la biblioteca.",
                ubicacion = "Biblioteca",
                autorId = "usuario_3",
                autorNombre = "Sofía López",
                contacto = "sofia@mitecmi.mx",
                fecha = "27 de agosto de 2026",
                estado = "Disponible"
            ),

            ObjetoPerdido(
                id = "objeto_2",
                tipo = "Perdido",
                descripcion = "Mochila gris con cuadernos y cargador de laptop.",
                ubicacion = "Edificio principal",
                autorId = "usuario_4",
                autorNombre = "Daniel Ruiz",
                contacto = "daniel@mitecmi.mx",
                fecha = "26 de agosto de 2026",
                estado = "Buscando"
            )
        )
    )

    override val objetosPerdidos: StateFlow<List<ObjetoPerdido>> =
        _objetosPerdidos.asStateFlow()


    override fun crearPropuesta(
        titulo: String,
        descripcion: String
    ) {

        val nuevaPropuesta = Propuesta(
            id = UUID.randomUUID().toString(),
            titulo = titulo,
            descripcion = descripcion,
            autorId = "usuario_actual",
            autorNombre = "Estudiante Tecmilenio",
            votos = 0,
            fecha = "Hoy"
        )

        _propuestas.value =
            listOf(nuevaPropuesta) + _propuestas.value
    }


    override fun votarPropuesta(
        propuestaId: String
    ) {

        _propuestas.value = _propuestas.value.map { propuesta ->

            if (propuesta.id == propuestaId) {

                if (propuesta.votadaPorUsuario) {

                    propuesta.copy(
                        votos = (propuesta.votos - 1).coerceAtLeast(0),
                        votadaPorUsuario = false
                    )

                } else {

                    propuesta.copy(
                        votos = propuesta.votos + 1,
                        votadaPorUsuario = true
                    )
                }

            } else {

                propuesta
            }
        }
    }


    override fun cambiarInteresEvento(
        eventoId: String
    ) {

        _eventos.value = _eventos.value.map { evento ->

            if (evento.id == eventoId) {

                if (evento.usuarioInteresado) {

                    evento.copy(
                        interesados = (evento.interesados - 1)
                            .coerceAtLeast(0),

                        usuarioInteresado = false
                    )

                } else {

                    evento.copy(
                        interesados = evento.interesados + 1,
                        usuarioInteresado = true
                    )
                }

            } else {

                evento
            }
        }
    }


    override fun votarEncuesta(
        encuestaId: String,
        opcionId: String
    ) {

        _encuestas.value = _encuestas.value.map { encuesta ->

            if (
                encuesta.id == encuestaId &&
                !encuesta.usuarioYaVoto
            ) {

                val nuevasOpciones = encuesta.opciones.map { opcion ->

                    if (opcion.id == opcionId) {

                        opcion.copy(
                            votos = opcion.votos + 1
                        )

                    } else {

                        opcion
                    }
                }

                encuesta.copy(
                    opciones = nuevasOpciones,
                    usuarioYaVoto = true
                )

            } else {

                encuesta
            }
        }
    }


    override fun crearObjetoPerdido(
        tipo: String,
        descripcion: String,
        ubicacion: String,
        contacto: String
    ) {

        val nuevoObjeto = ObjetoPerdido(
            id = UUID.randomUUID().toString(),
            tipo = tipo,
            descripcion = descripcion,
            ubicacion = ubicacion,
            autorId = "usuario_actual",
            autorNombre = "Estudiante Tecmilenio",
            contacto = contacto,
            fecha = "Hoy",

            estado = if (tipo == "Perdido") {
                "Buscando"
            } else {
                "Disponible"
            }
        )

        _objetosPerdidos.value =
            listOf(nuevoObjeto) + _objetosPerdidos.value
    }
}