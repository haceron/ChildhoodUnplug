package com.ppp.pegasussociety.Login

data class VerifyResponse(
    val message: String,
    val data: VerifyData
)

data class VerifyData(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val email: String,

    val createdAt: String,
    val updatedAt: String,
    val countryCode: String,
)
