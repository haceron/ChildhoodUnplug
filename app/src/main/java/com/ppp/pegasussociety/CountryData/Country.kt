package com.ppp.pegasussociety.CountryData

data class Country(
    val name: String,
    val isoCode: String,
    val dialCode: String,
    val flag: String,
    val tag: String // final label like 🇮🇳 India (+91)
)

