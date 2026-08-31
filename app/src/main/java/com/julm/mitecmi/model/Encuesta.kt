package com.julm.mitecmi.model

data class Encuesta(
    val id: String = "",
    val pregunta: String = "",
    val opciones: List<OpcionEncuesta> = emptyList(),
    val fechaCierre: String = "",
    val usuarioYaVoto: Boolean = false
)

data class OpcionEncuesta(
    val id: String = "",
    val texto: String = "",
    val votos: Int = 0
)
