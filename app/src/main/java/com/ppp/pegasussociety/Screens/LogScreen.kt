package com.ppp.pegasussociety.Screens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ppp.pegasussociety.ViewModel.ScreenTimeViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(navController: NavController? = null) {
    val viewModel: ScreenTimeViewModel = hiltViewModel()
    val context = LocalContext.current

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedHour by remember { mutableStateOf(LocalTime.now().hour) }
    var selectedMinute by remember { mutableStateOf(LocalTime.now().minute) }
    var selectedDuration by remember { mutableStateOf(15) }
    var selectedActivity by remember { mutableStateOf<String?>(null) }

    val activityOptions = listOf("Feed", "Before Sleep", "Outdoor", "Study", "Other")

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showDurationPicker by remember { mutableStateOf(false) }

    val showToast = remember { mutableStateOf("") }
    val submissionState = viewModel.submissionState.collectAsState()

    LaunchedEffect(submissionState.value) {
        submissionState.value?.let {
            showToast.value = if (it.isSuccess) {
                "Entry submitted successfully!"
            } else {
                it.exceptionOrNull()?.localizedMessage ?: "Submission failed"
            }
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Log Screen Time", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column {
                        InputSelectorRow(
                            icon = Icons.Default.CalendarMonth,
                            label = "Date",
                            value = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            onClick = { showDatePicker = true }
                        )
                        HorizontalDivider()
                        InputSelectorRow(
                            icon = Icons.Default.Schedule,
                            label = "Time",
                            value = String.format("%02d:%02d", selectedHour, selectedMinute),
                            onClick = { showTimePicker = true }
                        )
                        HorizontalDivider()
                        InputSelectorRow(
                            icon = Icons.Default.Timer,
                            label = "Duration",
                            value = "$selectedDuration min",
                            onClick = { showDurationPicker = true }
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Activities", style = MaterialTheme.typography.titleMedium)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        activityOptions.forEach { activity ->
                            FilterChip(
                                selected = selectedActivity == activity,
                                onClick = {
                                    selectedActivity = if (selectedActivity == activity) null else activity
                                },
                                label = { Text(activity) }
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    val isoDateTime = "${selectedDate}T${String.format("%02d:%02d", selectedHour, selectedMinute)}"
                    selectedActivity?.let {
                        viewModel.submitEntry(
                            dateTime = isoDateTime,
                            duration = selectedDuration,
                            activities = listOf(it)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = selectedActivity != null
            ) {
                Text("Submit", style = MaterialTheme.typography.titleMedium)
            }
        }
    }

    if (showToast.value.isNotEmpty()) {
        Toast.makeText(context, showToast.value, Toast.LENGTH_SHORT).show()
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
                .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        StepTimeSelector(
            initialHour = selectedHour,
            initialMinute = selectedMinute,
            onConfirm = { h, m ->
                selectedHour = h
                selectedMinute = m
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }

    if (showDurationPicker) {
        ModernDurationWheelPicker(
            initialValue = selectedDuration,
            onConfirm = {
                selectedDuration = it
                showDurationPicker = false
            },
            onDismiss = { showDurationPicker = false }
        )
    }
}

@Composable
private fun InputSelectorRow(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.weight(1f))
        Text(
            value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Icon(Icons.Default.ChevronRight, contentDescription = null)
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StepTimeSelector(
    initialHour: Int = 12,
    initialMinute: Int = 0,
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val hours = remember { (0..23).toList() }
    val minutes = remember { (0..59).toList() }

    var selectedHour by remember { mutableStateOf(initialHour) }
    var selectedMinute by remember { mutableStateOf(initialMinute) }

    val hourListState = rememberLazyListState(initialHour)
    val minuteListState = rememberLazyListState(initialMinute)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Time", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
        text = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WheelPicker(
                    list = hours,
                    selectedValue = selectedHour,
                    listState = hourListState,
                    onValueChange = { selectedHour = it }
                )
                Text(":", style = MaterialTheme.typography.headlineMedium)
                WheelPicker(
                    list = minutes,
                    selectedValue = selectedMinute,
                    listState = minuteListState,
                    onValueChange = { selectedMinute = it }
                )
            }
        },
        confirmButton = { TextButton(onClick = { onConfirm(selectedHour, selectedMinute) }) { Text("OK") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WheelPicker(
    list: List<Int>,
    selectedValue: Int,
    listState: LazyListState,
    onValueChange: (Int) -> Unit
) {
    val snapFlingBehavior = rememberSnapFlingBehavior(listState)

    Box(
        modifier = Modifier
            .width(60.dp)
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                )
        )

        LazyColumn(
            state = listState,
            flingBehavior = snapFlingBehavior,
            contentPadding = PaddingValues(vertical = 65.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(list) { _, value ->
                val isSelected = value == selectedValue
                Text(
                    String.format("%02d", value),
                    style = if (isSelected) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        LaunchedEffect(listState.firstVisibleItemIndex) {
            val centerIndex = listState.firstVisibleItemIndex + 1
            list.getOrNull(centerIndex)?.let { onValueChange(it) }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModernDurationWheelPicker(
    initialValue: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val durationOptions = remember { (1..180).toList() }
    var selectedDuration by remember { mutableStateOf(initialValue) }
    val listState = rememberLazyListState(initialValue - 1)
    val snapFlingBehavior = rememberSnapFlingBehavior(listState)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Duration", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
        text = {
            Box(
                modifier = Modifier.height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                )

                LazyColumn(
                    state = listState,
                    flingBehavior = snapFlingBehavior,
                    contentPadding = PaddingValues(vertical = 65.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(durationOptions) { _, duration ->
                        val isSelected = duration == selectedDuration
                        Text(
                            "$duration min",
                            style = if (isSelected) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                LaunchedEffect(listState.firstVisibleItemIndex) {
                    val centerIndex = listState.firstVisibleItemIndex + 1
                    durationOptions.getOrNull(centerIndex)?.let { selectedDuration = it }
                }
            }
        },
        confirmButton = { TextButton(onClick = { onConfirm(selectedDuration) }) { Text("OK") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}




