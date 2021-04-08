package com.parrot.dto

data class RequestDTO(
    val receiver: String,
    val status: String,
    val alerts: Collection<AlertDTO>
)