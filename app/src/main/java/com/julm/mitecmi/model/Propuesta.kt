package com.julm.mitecmi.model

data class Propuesta(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val autorId: String = "",
    val autorNombre: String = "",
    val votos: Int = 0,
    val fecha: String = "",
    val votadaPorUsuario: Boolean = false
)
