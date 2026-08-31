package com.julm.mitecmi.model

data class Evento(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val fecha: String = "",
    val hora: String = "",
    val ubicacion: String = "",
    val categoria: String = "",
    val interesados: Int = 0,
    val usuarioInteresado: Boolean = false
)
