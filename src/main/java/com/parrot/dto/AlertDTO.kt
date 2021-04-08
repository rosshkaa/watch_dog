package com.parrot.dto

data class AlertDTO(
    val status: String,
    val labels: AlertLabelDTO,
    val annotations: AlertAnnotationDTO,
    val startsAt: String,
    val endsAt: String,
    val generatorURL: String,
    val fingerprint: String
)