package com.julm.mitecmi.model

data class Asistencia(
    val id: String = "",
    val usuarioId: String = "",
    val eventoId: String = "",
    val fechaCheckIn: String = "",
    val metodo: String = "qr",
    val puntosGenerados: Int = 0
)
