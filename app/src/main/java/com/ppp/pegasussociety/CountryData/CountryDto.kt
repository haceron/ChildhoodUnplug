
data class CountryDto(
    val name: Name,
    val cca2: String,
    val idd: Idd,
    val flags: Flags
)

data class Name(val common: String)
data class Idd(val root: String?, val suffixes: List<String>?)
data class Flags(val png: String?, val emoji: String?)

