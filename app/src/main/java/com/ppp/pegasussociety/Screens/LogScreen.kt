package com.ppp.pegasussociety.Screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ppp.pegasussociety.ViewModel.ScreenTimeViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun LogScreen(navController: NavController? = null) {
    val viewModel: ScreenTimeViewModel = hiltViewModel()
    val context = LocalContext.current

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    var selectedDuration by remember { mutableStateOf(15) }
    var selectedActivities by remember { mutableStateOf(setOf<String>()) }

    val activityOptions = listOf("Feed", "Before Sleep", "Outdoor", "Study")
    val scrollState = rememberScrollState()

    val showToast = remember { mutableStateOf("") }
    val submissionState = viewModel.submissionState.collectAsState()

    LaunchedEffect(submissionState.value) {
        submissionState.value?.let {
            if (it.isSuccess) {
                showToast.value = "Entry submitted successfully!"
            } else {
                showToast.value = it.exceptionOrNull()?.localizedMessage ?: "Submission failed"
            }
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Text("Log Screen Time", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        // Date Picker
        OutlinedButton(onClick = {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                },
                selectedDate.year,
                selectedDate.monthValue - 1,
                selectedDate.dayOfMonth
            ).show()
        }) {
            Text("Date: ${selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}")
        }

        // Time Picker
        OutlinedButton(onClick = {
            val now = Calendar.getInstance()
            TimePickerDialog(
                context,
                { _, hour, minute ->
                    selectedTime = LocalTime.of(hour, minute)
                },
                selectedTime.hour,
                selectedTime.minute,
                true
            ).show()
        }) {
            Text("Time: ${selectedTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))}")
        }

        // Duration Picker
        Column {
            Text("Duration (minutes)", fontWeight = FontWeight.SemiBold)
            Slider(
                value = selectedDuration.toFloat(),
                onValueChange = { selectedDuration = it.toInt() },
                valueRange = 1f..180f,
                steps = 17 // every 10 minutes
            )
            Text("$selectedDuration minutes")
        }

        // Activity Selection
        Column {
            Text("Activities", fontWeight = FontWeight.SemiBold)
            activityOptions.forEach { activity ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = selectedActivities.contains(activity),
                        onCheckedChange = {
                            selectedActivities = if (it) {
                                selectedActivities + activity
                            } else {
                                selectedActivities - activity
                            }
                        }
                    )
                    Text(activity)
                }
            }
        }

        Button(
            onClick = {
                val isoDateTime = "${selectedDate}T${selectedTime}"

                viewModel.submitEntry(
                    dateTime = isoDateTime,
                    duration = selectedDuration,
                    activities = selectedActivities.toList()
                )

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }

        if (showToast.value.isNotEmpty()) {
            Text(
                text = showToast.value,
                color = if (showToast.value.contains("success", true)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}
