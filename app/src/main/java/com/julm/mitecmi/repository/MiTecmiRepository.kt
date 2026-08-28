package com.julm.mitecmi.repository

import com.julm.mitecmi.model.Anuncio
import com.julm.mitecmi.model.Club
import com.julm.mitecmi.model.Encuesta
import com.julm.mitecmi.model.Evento
import com.julm.mitecmi.model.ObjetoPerdido
import com.julm.mitecmi.model.Propuesta
import kotlinx.coroutines.flow.StateFlow

interface MiTecmiRepository {

    val propuestas: StateFlow<List<Propuesta>>

    val eventos: StateFlow<List<Evento>>

    val encuestas: StateFlow<List<Encuesta>>

    val clubes: StateFlow<List<Club>>

    val anuncios: StateFlow<List<Anuncio>>

    val objetosPerdidos: StateFlow<List<ObjetoPerdido>>


    fun crearPropuesta(
        titulo: String,
        descripcion: String
    )


    fun votarPropuesta(
        propuestaId: String
    )


    fun cambiarInteresEvento(
        eventoId: String
    )


    fun votarEncuesta(
        encuestaId: String,
        opcionId: String
    )


    fun crearObjetoPerdido(
        tipo: String,
        descripcion: String,
        ubicacion: String,
        contacto: String
    )
}