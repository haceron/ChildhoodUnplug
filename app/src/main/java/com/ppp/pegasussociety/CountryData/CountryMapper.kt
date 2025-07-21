package com.ppp.pegasussociety.CountryData

import CountryDto
import com.ppp.pegasussociety.CountryData.Country

fun CountryDto.toCountry(): Country {
    val dial = (idd.root ?: "") + (idd.suffixes?.firstOrNull() ?: "")
    val flag = flags.emoji ?: "🏳️"
    return Country(
        name = name.common,
        isoCode = cca2,
        dialCode = dial,
        flag = flag,
        tag = "$flag $name (${dial})"
    )
}
