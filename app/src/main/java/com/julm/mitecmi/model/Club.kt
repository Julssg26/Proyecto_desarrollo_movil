package com.julm.mitecmi.model

data class Club(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val contacto: String,
    val categoria: String,
    val horario: String,
    val imagenUrl: String = ""
)