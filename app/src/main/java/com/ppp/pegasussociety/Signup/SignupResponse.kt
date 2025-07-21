

data class createUser(
    val id: String,
    val role: String,
    val parent_name: String,
    val email: String,
    val mobileno: String,
    val city: String,
    val countryCode: String,
    val kids: Int,
    val updatedAt: String,
    val createdAt: String
)

data class SignupResponse(
    val message: String? = "",
    val createUser: createUser? = null
)