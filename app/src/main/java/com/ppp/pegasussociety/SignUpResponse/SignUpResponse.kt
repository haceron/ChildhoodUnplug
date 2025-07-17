package com.ppp.pegasussociety.SignUpResponse

data class SignupRequest(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val countryCode: String
)

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val countryCode: String,
    val createdAt: String,
    val updatedAt: String
)

data class SignUpResponse(
    val message: String? = "",
    val user: User? = null
)
