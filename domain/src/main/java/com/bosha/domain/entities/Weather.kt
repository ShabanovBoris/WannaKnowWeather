package com.bosha.domain.entities

data class Weather(
    val icon: String,
    val description: String,
    val main: String,
    val id: Int
)