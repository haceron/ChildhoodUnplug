package com.ppp.pegasussociety.Model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ScreenTimeEntryRequest(
    val childrenId: String,
    val dateTime: String,
    val duration: Int,
    val activities: List<String>
)

data class ScreenTimeApiResponse(
    val childrenId: String,
    val dailyLogs: List<DailyLog>
)

data class DailyScreenTime(
    val date: LocalDate,
    val totalMinutes: Long,
    val activitiesBreakdown: Map<String, Long>
)

data class TrendData(
    val last7DaysAverage: Long = 0L,
    val overallAverage: Long = 0L,
    val weeklyTrendPercentage: Double? = null
)

data class DailyLog(
    val date: String, // "2025-08-08"
    val totalDuration: Long, // total in minutes
    val activitiesBreakdown: Map<String, Long>,
    val logTimes: List<String> // ["12:27:00", ...]
) {
    val parsedDate: LocalDate
        get() = LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
}