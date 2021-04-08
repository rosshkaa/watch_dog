package com.parrot.dto

data class AlertLabelDTO(
    val alertname: String,
    val instance: String,
    val job: String,
    val severity: String
)