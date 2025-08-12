//package com.ppp.pegasussociety.DB

/*
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@ProvidedTypeConverter
class Converters @Inject constructor() {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val gson = Gson()

    @TypeConverter
    fun fromDateTime(value: LocalDateTime): String = value.format(formatter)

    @TypeConverter
    fun toDateTime(value: String): LocalDateTime = LocalDateTime.parse(value, formatter)

    @TypeConverter
    fun fromStringList(value: List<String>?): String = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrEmpty()) return emptyList()
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
}*/


///--------------UpTO--------------------------------------------------------------////

















/*
package com.ppp.pegasussociety.DB

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

// Type converters for Room to handle custom data types.
// Room doesn't natively know how to store LocalDateTime or Set<String>.
*/
/*class Converters {
    // Converts a LocalDateTime to a Long timestamp for storage.
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime): Long {
        return value.toInstant(ZoneOffset.UTC).toEpochMilli()
    }

    // Converts a Long timestamp from the database back to a LocalDateTime.
    @TypeConverter
    fun toLocalDateTime(value: Long): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC)
    }

    // Converts a Set<String> to a single comma-separated String for storage.
    @TypeConverter
    fun fromStringSet(set: Set<String>): String {
        return set.joinToString(",")
    }

    // Converts a comma-separated String from the database back to a Set<String>.
    @TypeConverter
    fun toStringSet(string: String): Set<String> {
        return string.split(",").toSet()
    }
}*//*


// File: data/local/Converters.kt

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {

    // --- Converter for List<String> ---
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    // --- Converter for LocalDateTime ---
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) }
    }

    @TypeConverter
    fun toTimestamp(date: LocalDateTime?): Long? {
        return date?.toEpochSecond(ZoneOffset.UTC)
    }
}*/
/*

class Converters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromDateTime(value: LocalDateTime): String = value.format(formatter)

    @TypeConverter
    fun toDateTime(value: String): LocalDateTime = LocalDateTime.parse(value, formatter)

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

}
*/
