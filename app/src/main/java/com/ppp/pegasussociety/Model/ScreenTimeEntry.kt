/*
package com.ppp.pegasussociety.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "screen_time_entries")
data class ScreenTimeEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateTime: String,
    val duration: Long, // Duration in minutes
    val activities: List<String>
)

data class DailyScreenTime(
    val date: LocalDate,
    val totalMinutes: Long,
    val activitiesBreakdown: Map<String, Long>
)

data class TrendData(
    val overallDailyAverage: Float = 0f,
    val last7DaysAverage: Float = 0f,
    val weeklyTrendPercentage: Float? = null
)



*/
