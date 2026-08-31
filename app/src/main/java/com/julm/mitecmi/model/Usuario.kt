package com.julm.mitecmi.model

data class Usuario(
    val id: String = "",
    val nombre: String = "",
    val correo: String = "",
    val campus: String = "",
    val facultad: String = "",
    val semestre: String = "",
    val rol: String = "estudiante",
    val puntosTotales: Int = 0,
    val fechaRegistro: String = ""
)
