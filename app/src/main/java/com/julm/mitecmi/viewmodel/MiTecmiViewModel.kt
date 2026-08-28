package com.julm.mitecmi.viewmodel

import androidx.lifecycle.ViewModel
import com.julm.mitecmi.repository.FakeMiTecmiRepository
import com.julm.mitecmi.repository.MiTecmiRepository

class MiTecmiViewModel(
    private val repository: MiTecmiRepository =
        FakeMiTecmiRepository()
) : ViewModel() {

    val propuestas = repository.propuestas

    val eventos = repository.eventos

    val encuestas = repository.encuestas

    val clubes = repository.clubes

    val anuncios = repository.anuncios

    val objetosPerdidos = repository.objetosPerdidos


    fun crearPropuesta(
        titulo: String,
        descripcion: String
    ) {

        if (
            titulo.isBlank() ||
            descripcion.isBlank()
        ) {
            return
        }

        repository.crearPropuesta(
            titulo = titulo.trim(),
            descripcion = descripcion.trim()
        )
    }


    fun votarPropuesta(
        propuestaId: String
    ) {

        repository.votarPropuesta(
            propuestaId
        )
    }


    fun cambiarInteresEvento(
        eventoId: String
    ) {

        repository.cambiarInteresEvento(
            eventoId
        )
    }


    fun votarEncuesta(
        encuestaId: String,
        opcionId: String
    ) {

        repository.votarEncuesta(
            encuestaId = encuestaId,
            opcionId = opcionId
        )
    }


    fun crearObjetoPerdido(
        tipo: String,
        descripcion: String,
        ubicacion: String,
        contacto: String
    ) {

        if (
            descripcion.isBlank() ||
            ubicacion.isBlank() ||
            contacto.isBlank()
        ) {
            return
        }

        repository.crearObjetoPerdido(
            tipo = tipo,
            descripcion = descripcion.trim(),
            ubicacion = ubicacion.trim(),
            contacto = contacto.trim()
        )
    }
}