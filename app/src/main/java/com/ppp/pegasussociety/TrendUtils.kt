/*
package com.ppp.pegasussociety

import com.ppp.pegasussociety.Model.DailyScreenTime
import com.ppp.pegasussociety.Model.ScreenTimeEntry
import com.ppp.pegasussociety.Model.TrendData
import java.time.LocalDate

fun aggregateEntries(entries: List<ScreenTimeEntry>): List<DailyScreenTime> {
    return entries
        .groupBy { it.dateTime.toLocalDate() }
        .map { (date, dailyEntries) ->
            val total = dailyEntries.sumOf { it.duration }
            val activityBreakdown = dailyEntries
                .flatMap { entry ->
                    val perActivityDuration = entry.duration / entry.activities.size
                    entry.activities.map { activity ->
                        activity to perActivityDuration
                    }
                }
                .groupBy({ it.first }, { it.second })
                .mapValues { (_, values) -> values.sum() }

            DailyScreenTime(
                date = date,
                totalMinutes = total,
                activitiesBreakdown = activityBreakdown
            )
        }
        .sortedBy { it.date }
}


fun computeTrendData(entries: List<ScreenTimeEntry>): TrendData {
    val grouped = aggregateEntries(entries)

    if (grouped.isEmpty()) return TrendData()

    val allDays = grouped.map { it.totalMinutes }
    val overallAverage = allDays.average().toFloat()

    val today = LocalDate.now()
    val last7Days = grouped.filter { it.date.isAfter(today.minusDays(7)) }
    val last7Average = last7Days.map { it.totalMinutes }.average().toFloat()

    val weekBefore = grouped.filter {
        it.date.isAfter(today.minusDays(14)) && it.date.isBefore(today.minusDays(7))
    }
    val weekBeforeAverage = weekBefore.map { it.totalMinutes }.average().toFloat()

    val trendPercentage = if (weekBeforeAverage > 0f) {
        ((last7Average - weekBeforeAverage) / weekBeforeAverage) * 100
    } else null

    return TrendData(
        overallDailyAverage = overallAverage,
        last7DaysAverage = last7Average,
        weeklyTrendPercentage = trendPercentage
    )
}

*/
