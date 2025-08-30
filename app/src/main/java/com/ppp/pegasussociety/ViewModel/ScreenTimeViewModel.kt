package com.ppp.pegasussociety.ViewModel

/*import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ppp.pegasussociety.Model.ScreenTimeEntryRequest
import com.ppp.pegasussociety.Repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ScreenTimeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _submissionState = MutableStateFlow<Result<Unit>?>(null)
    val submissionState: StateFlow<Result<Unit>?> = _submissionState

    fun submitEntry(
        dateTime: String,
        duration: Int,
        activities: List<String>
    ) {
        val request = ScreenTimeEntryRequest(
            childrenId = "CHILD0001",
            dateTime = dateTime,
            duration = duration,
            activities = activities
        )

        viewModelScope.launch {
            val result = repository.postEntry(request)
            _submissionState.value = result
        }
    }


    private fun combineDateTime(date: String, time: String): String {
        return try {
            val combined = LocalDateTime.parse("$date$time")
            combined.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        } catch (e: Exception) {
            // fallback or log error
            "$date$time"
        }
    }

    fun resetState() {
        _submissionState.value = null
    }
}*/

//-------------------------------------------------------------------------

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ppp.pegasussociety.ApiInterface.AllApi
import com.ppp.pegasussociety.Model.DailyLog
import com.ppp.pegasussociety.Model.DailyScreenTime
import com.ppp.pegasussociety.Model.ScreenTimeEntryRequest
import com.ppp.pegasussociety.Model.TrendData
import com.ppp.pegasussociety.Repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScreenTimeViewModel @Inject constructor(
    private val api: AllApi,
    private val repository: Repository
) : ViewModel() {

    private val _dailyLogs = MutableStateFlow<List<DailyLog>>(emptyList())
    val dailyLogs: StateFlow<List<DailyLog>> = _dailyLogs

    private val _trendData = MutableStateFlow(TrendData())
    val trendData: StateFlow<TrendData> = _trendData

    init {
        fetchLogs()
    }

    private val _submissionState = MutableStateFlow<Result<Unit>?>(null)
    val submissionState: StateFlow<Result<Unit>?> = _submissionState

    fun submitEntry(
        dateTime: String,
        duration: Int,
        activities: List<String>
    ) {
        val request = ScreenTimeEntryRequest(
            childrenId = "USR000001CH1",
            dateTime = dateTime,
            duration = duration,
            activities = activities
        )

        viewModelScope.launch {
            val result = repository.postEntry(request)
            _submissionState.value = result
        }
    }

    fun resetState() {
        _submissionState.value = null
    }

    fun fetchLogs(childId: String = "USR000001CH1") {
        viewModelScope.launch {
            try {
                val response = api.getScreenTimeLogs(childId)
                if (response.isSuccessful && response.body() != null) {
                    val logs = response.body()!!.dailyLogs
                    _dailyLogs.value = logs
                    _trendData.value = calculateTrendData(logs)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getAggregatedDataForChart(): List<DailyScreenTime> {
        return dailyLogs.value.map {
            DailyScreenTime(
                date = it.parsedDate,
                totalMinutes = it.totalDuration,
                activitiesBreakdown = it.activitiesBreakdown
            )
        }.sortedBy { it.date }
    }

    private fun calculateTrendData(logs: List<DailyLog>): TrendData {
        if (logs.isEmpty()) return TrendData()

        val today = LocalDate.now()
        val last7Days = logs.filter { it.parsedDate >= today.minusDays(6) }

        val last7Avg = last7Days.map { it.totalDuration }.average()
        val overallAvg = logs.map { it.totalDuration }.average()

        val trendPercent = if (overallAvg != 0.0)
            ((last7Avg - overallAvg) / overallAvg) * 100 else null

        return TrendData(
            last7DaysAverage = last7Avg.toLong(),
            overallAverage = overallAvg.toLong(),
            weeklyTrendPercentage = trendPercent
        )

    }
}
