/*package com.ppp.pegasussociety.ViewModel

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.ppp.pegasussociety.Model.DailyScreenTime
import com.ppp.pegasussociety.Model.ScreenTimeEntry
import com.ppp.pegasussociety.Model.TrendData
import com.ppp.pegasussociety.Repository.ScreenTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
///////////////

@HiltViewModel
class ScreenTimerViewModel @OptIn(UnstableApi::class)
@Inject constructor(
    private val repository: ScreenTimeRepository
) : ViewModel() {

    private val _entries = MutableStateFlow<List<ScreenTimeEntry>>(emptyList())
    val entries: StateFlow<List<ScreenTimeEntry>> = _entries.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getLocalEntries().collect { _entries.value = it }
        }
        refreshEntries()
    }

@OptIn(UnstableApi::class)
fun addEntry(entry: ScreenTimeEntry) {
        Log.d("Repo", "Inserting entry: $entry")

        viewModelScope.launch {
        try {
            Log.d("Repo1", "Inserting entry: $entry")

            repository.addEntry(entry) // Adds to Room and posts to API
            // 🔥 Don't overwrite with possibly stale remote
            repository.getLocalEntries().collect { _entries.value = it }
        } catch (e: Exception) {
            Log.e("ViewModel", "Add entry failed", e)
        }
    }
}


    @OptIn(UnstableApi::class)
    fun refreshEntries() {
        viewModelScope.launch {
            try {
                val remote = repository.fetchAllEntries()
                _entries.value = remote
            } catch (e: Exception) {
                Log.e("ViewModel1", "Fetching failed", e)
            }
        }
    }

    suspend fun getTrendData(): TrendData {
        return computeTrendData(_entries.value)
    }

    fun getAggregatedDataForChart(entries: List<ScreenTimeEntry>): List<DailyScreenTime> {
        return aggregateEntries(entries)
    }
}

fun aggregateEntries(entries: List<ScreenTimeEntry>): List<DailyScreenTime> {
    return entries
        .groupBy { it.dateTime.toLocalDate() }
        .map { (date, entriesForDate) ->
            val totalMinutes = entriesForDate.sumOf { it.duration }

            val activityBreakdown = entriesForDate
                .flatMap { entry ->
                    val perActivityDuration = if (entry.activities.isNotEmpty()) {
                        entry.duration / entry.activities.size
                    } else 0L

                    entry.activities.map { activity ->
                        activity to perActivityDuration
                    }
                }
                .groupBy({ it.first }, { it.second })
                .mapValues { (_, durations) -> durations.sum() }

            DailyScreenTime(
                date = date,
                totalMinutes = totalMinutes,
                activitiesBreakdown = activityBreakdown
            )
        }
        .sortedBy { it.date }
}


fun computeTrendData(entries: List<ScreenTimeEntry>): TrendData {
    val daily = aggregateEntries(entries)

    if (daily.isEmpty()) {
        return TrendData()
    }

    val average = daily.map { it.totalMinutes }.average().toFloat()

    val today = LocalDate.now()
    val last7Days = daily.filter { it.date.isAfter(today.minusDays(7)) }
    val last7Average = last7Days.map { it.totalMinutes }.average().toFloat()

    val previous7Days = daily.filter {
        it.date.isAfter(today.minusDays(14)) && it.date.isBefore(today.minusDays(7))
    }
    val previous7Average = previous7Days.map { it.totalMinutes }.average().toFloat()

    val trendPercentage = if (previous7Average > 0) {
        ((last7Average - previous7Average) / previous7Average) * 100
    } else null

    return TrendData(
        overallDailyAverage = average,
        last7DaysAverage = last7Average,
        weeklyTrendPercentage = trendPercentage
    )
}*/

///--------------UpTO--------------------------------------------------------------////

















/*
package com.ppp.pegasussociety.ViewModel

*/
/*

@HiltViewModel
class ScreenTimerViewModel @Inject constructor() : ViewModel() {

    private val _entries = MutableStateFlow<List<ScreenTimeEntry>>(emptyList())
    val entries: StateFlow<List<ScreenTimeEntry>> = _entries

    init {
        // Optional: preload dummy/test data
        _entries.value = listOf(
            ScreenTimeEntry("2025-07-21", 30, 60, 45),
            ScreenTimeEntry("2025-07-22", 20, 50, 40)
        )
    }

    fun addEntry(date: String, morning: Int, afternoon: Int, night: Int) {
        val currentList = _entries.value.toMutableList()
        val existingIndex = currentList.indexOfFirst { it.date == date }
        val newEntry = ScreenTimeEntry(date, morning, afternoon, night)

        if (existingIndex >= 0) {
            currentList[existingIndex] = newEntry
        } else {
            currentList.add(newEntry)
        }

        _entries.value = currentList
    }

    fun deleteEntry(date: String) {
        _entries.value = _entries.value.filterNot { it.date == date }
    }

    fun clearAll() {
        _entries.value = emptyList()
    }
}

*//*


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ppp.pegasussociety.Model.DailyScreenTime
import com.ppp.pegasussociety.Model.ScreenTimeEntry
import com.ppp.pegasussociety.Model.TrendData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import com.ppp.pegasussociety.DB.ScreenTimeDao
import com.ppp.pegasussociety.Repository.ScreenTimeRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

@HiltViewModel
class ScreenTimerViewModel @Inject constructor(
    private val screenTimeRepository: ScreenTimeRepository
) : ViewModel() {

    val entries: StateFlow<List<ScreenTimeEntry>> = screenTimeRepository.getAllEntries()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addEntry(entry: ScreenTimeEntry) {
        viewModelScope.launch {
            screenTimeRepository.addEntry(entry)
        }
    }

    fun getAggregatedDataForChart(filteredEntries: List<ScreenTimeEntry>): List<DailyScreenTime> {
        return filteredEntries
            .groupBy { it.dateTime.toLocalDate() }
            .map { (date, entries) ->
                val totalMinutes = entries.sumOf { it.duration }
                val activitiesMap = entries
                    .flatMap { entry -> entry.activities.map { activity -> activity to entry.duration } }
                    .groupBy({ it.first }, { it.second })
                    .mapValues { it.value.sum() }
                DailyScreenTime(date, totalMinutes, activitiesMap)
            }
            .sortedBy { it.date }
    }

    suspend fun getTrendData(): TrendData {
        // Use the suspended 'firstOrNull' function to get the current list from the flow.
        val allEntries = entries.firstOrNull() ?: return TrendData()

        if (allEntries.isEmpty()) return TrendData()

        val today = LocalDate.now()

        val distinctDays = allEntries.map { it.dateTime.toLocalDate() }.distinct().count().coerceAtLeast(1)
        val overallAverage = allEntries.sumOf { it.duration }.toFloat() / distinctDays

        val last7DaysStart = today.minusDays(6)
        val last7DaysEntries = allEntries.filter { !it.dateTime.toLocalDate().isBefore(last7DaysStart) }
        val last7DaysTotal = last7DaysEntries.sumOf { it.duration }
        val last7DaysAverage = last7DaysTotal.toFloat() / 7f

        val previous7DaysStart = today.minusDays(13)
        val previous7DaysEntries = allEntries.filter {
            val date = it.dateTime.toLocalDate()
            val previous7DaysEnd = today.minusDays(7)
            !date.isBefore(previous7DaysStart) && date.isBefore(previous7DaysEnd)
        }
        val previous7DaysAverage = if (previous7DaysEntries.isNotEmpty()) {
            previous7DaysEntries.sumOf { it.duration }.toFloat() / 7f
        } else {
            0f
        }

        val weeklyTrend = if (previous7DaysAverage > 0f) {
            ((last7DaysAverage - previous7DaysAverage) / previous7DaysAverage) * 100f
        } else null

        return TrendData(overallAverage, last7DaysAverage, weeklyTrend)
    }
}

*/


