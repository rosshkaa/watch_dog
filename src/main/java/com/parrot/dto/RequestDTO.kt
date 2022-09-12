package com.parrot.dto

class RequestDTO(
    val receiver: String,
    val status: String,
    val alerts: Collection<AlertDTO>?
)