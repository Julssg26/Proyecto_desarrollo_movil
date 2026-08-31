package com.julm.mitecmi.model

data class RespuestaEncuesta(
    val id: String = "",
    val usuarioId: String = "",
    val encuestaId: String = "",
    val opcionElegida: String = "",
    val fecha: String = ""
)
