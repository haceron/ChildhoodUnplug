/*package com.ppp.pegasussociety.Screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ppp.pegasussociety.Model.ScreenTimeEntry
import com.ppp.pegasussociety.ViewModel.ScreenTimerViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// A list of predefined activities for the user to choose from
*//*
val predefinedActivities = listOf(
    "Social Media", "Gaming", "Watching Videos", "Reading",
    "Productivity", "Browse", "Messaging", "Education", "Other"
)
*//*

val screenDuring = listOf(
    "Feed", "Outdoor", "Before Sleep", "Study"
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LogScreenTimeScreen(navController: NavController) {
    val viewModel: ScreenTimerViewModel = hiltViewModel()

    // State variables for all the inputs
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var startTime by remember { mutableStateOf(LocalTime.now()) }
    var durationInMinutes by remember { mutableStateOf(30) }
    var selectedActivities by remember { mutableStateOf<Set<String>>(emptySet()) }

    // State to control the visibility of the pickers
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showDurationPicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Log New Entry") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                // Section for Date, Start Time, and Duration selection
                Card(elevation = CardDefaults.cardElevation(4.dp)) {
                    Column {
                        InputSelectorRow(
                            icon = Icons.Default.CalendarMonth,
                            label = "Date",
                            value = selectedDate.format(DateTimeFormatter.ofPattern("EEEE, d MMMM")),
                            onClick = { showDatePicker = true }
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        InputSelectorRow(
                            icon = Icons.Default.Schedule,
                            label = "Start Time",
                            value = startTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
                            onClick = { showTimePicker = true }
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        InputSelectorRow(
                            icon = Icons.Default.Timer,
                            label = "Duration",
                            value = "$durationInMinutes min",
                            onClick = { showDurationPicker = true }
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Activities performed during screen watch",
                        style = MaterialTheme.typography.titleMedium
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        screenDuring.forEach { activity ->
                            FilterChip(
                                selected = activity in selectedActivities,
                                onClick = {
                                    selectedActivities = if (activity in selectedActivities) {
                                        selectedActivities - activity
                                    } else {
                                        selectedActivities + activity
                                    }
                                },
                                label = { Text(activity) }
                            )
                        }
                    }
                }


                // Section for selecting activities
               *//* Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "What were your child watched?",
                        style = MaterialTheme.typography.titleMedium
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        predefinedActivities.forEach { activity ->
                            FilterChip(
                                selected = activity in selectedActivities,
                                onClick = {
                                    selectedActivities = if (activity in selectedActivities) {
                                        selectedActivities - activity
                                    } else {
                                        selectedActivities + activity
                                    }
                                },
                                label = { Text(activity) }
                            )
                        }
                    }
                }*//*
            }

            // Save Button at the bottom
            Button(
                onClick = {
                    if (selectedActivities.isNotEmpty() && durationInMinutes > 0) {
                        val newEntry = ScreenTimeEntry(
                            dateTime = LocalDateTime.of(selectedDate, startTime),
                            // Corrected line: Convert Int to Long
                            duration = durationInMinutes.toLong(),
                            activities = selectedActivities.toList()
                        )
                        viewModel.addEntry(newEntry)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = selectedActivities.isNotEmpty() && durationInMinutes > 0
            ) {
                Text("Save Entry", style = MaterialTheme.typography.titleMedium)
            }
        }
    }

    // --- Dialogs for picking Date, Time, and Duration ---

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        selectedDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
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
        val timePickerState = rememberTimePickerState(
            initialHour = startTime.hour,
            initialMinute = startTime.minute,
            is24Hour = false
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    startTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                    showTimePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
            },
            text = { TimePicker(state = timePickerState, layoutType = TimePickerLayoutType.Vertical) },
            title = {
                Text("Select Time", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        )
    }

    if (showDurationPicker) {
        ModernDurationPicker(
            initialValue = durationInMinutes,
            onDismissRequest = { showDurationPicker = false },
            onConfirm = { selectedDuration ->
                durationInMinutes = selectedDuration
                showDurationPicker = false
            }
        )
    }
}


// --- HELPER COMPOSABLES ---

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
        Icon(imageVector = icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModernDurationPicker(
    initialValue: Int,
    onDismissRequest: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    val durationOptions = remember { (5..180 step 5).toList() }
    val listState = rememberLazyListState()
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    var selectedDuration by remember { mutableStateOf(initialValue) }

    // This effect runs when scrolling stops to update the selected duration
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isNotEmpty()) {
                val viewportCenter = layoutInfo.viewportSize.height / 2
                val centerItem = visibleItems.minByOrNull {
                    kotlin.math.abs((it.offset + it.size / 2) - viewportCenter)
                }
                if (centerItem != null) {
                    selectedDuration = durationOptions[centerItem.index] -2
                }
            }
        }
    }

    // Effect to scroll to the initial value when the dialog is first shown
    LaunchedEffect(Unit) {
        val initialIndex = durationOptions.indexOf(initialValue).coerceAtLeast(0)
        val itemSize = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 0
        listState.scrollToItem(initialIndex, scrollOffset = -((listState.layoutInfo.viewportSize.height - itemSize) / 2))
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Duration", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
        text = {
            Box(
                modifier = Modifier.height(180.dp), // Set a fixed height for the picker
                contentAlignment = Alignment.Center
            ) {
                // Background highlight for the selected item
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                )

                // The scrollable picker wheel
                LazyColumn(
                    state = listState,
                    flingBehavior = snapFlingBehavior,
                    // Add padding to allow the first and last items to be centered
                    contentPadding = PaddingValues(vertical = 65.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(durationOptions) { index, duration ->
                        val (scale, alpha) = calculateScaleAndAlpha(listState, index)
                        Text(
                            text = "$duration min",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.graphicsLayer {
                                this.scaleX = scale
                                this.scaleY = scale
                                this.alpha = alpha
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedDuration) }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

// Helper function to calculate the dynamic scale and alpha for each item
@Composable
private fun calculateScaleAndAlpha(listState: LazyListState, index: Int): Pair<Float, Float> {
    val layoutInfo = listState.layoutInfo
    val visibleItems = layoutInfo.visibleItemsInfo
    val currentItem = visibleItems.firstOrNull { it.index == index }

    return if (currentItem != null) {
        val viewportCenter = layoutInfo.viewportSize.height / 2f
        val itemCenter = currentItem.offset + currentItem.size / 2f
        val distanceFromCenter = kotlin.math.abs(viewportCenter - itemCenter)

        // Scale down items that are further from the center
        val scale = 1f - (distanceFromCenter / viewportCenter) * 0.5f

        // Fade out items that are further from the center
        val alpha = 1f - (distanceFromCenter / viewportCenter) * 0.7f

        Pair(scale.coerceIn(0.5f, 1f), alpha.coerceIn(0.3f, 1f))
    } else {
        // Default values for items not currently visible
        Pair(0.5f, 0.3f)
    }
}*/

///--------------UpTO--------------------------------------------------------------////













/*import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter



// A list of predefined activities for the user to choose from
val predefinedActivities = listOf(
    "Social Media", "Gaming", "Watching Videos", "Reading",
    "Productivity", "Browsing", "Messaging", "Education"
)

val screenDuring = listOf(
    "Feed", "Outdoor", "Before Sleep", "Study"
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LogScreenTimeScreen(navController: NavController? = null) {
    // State variables for all the inputs
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var startTime by remember { mutableStateOf(LocalTime.now()) }
    // The duration is now an Int with a default of 30
    var durationInMinutes by remember { mutableStateOf(30) }
    var selectedActivities by remember { mutableStateOf<Set<String>>(emptySet()) }

    // State to control the visibility of the date and time pickers
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Log Screen Time") },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
                // Section for Date, Start Time, and Duration selection
                Card(elevation = CardDefaults.cardElevation(4.dp)) {
                    Column {
                        InputSelectorRow(
                            icon = Icons.Default.CalendarMonth,
                            label = "Date",
                            value = selectedDate.format(DateTimeFormatter.ofPattern("EEEE, d MMMM")),
                            onClick = { showDatePicker = true }
                        )
                        Divider(modifier = Modifier.padding(horizontal = 16.dp))
                        InputSelectorRow(
                            icon = Icons.Default.Schedule,
                            label = "Start Time",
                            value = startTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
                            onClick = { showTimePicker = true }
                        )
                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        // Use the new DurationStepperRow here
                        DurationStepperRow(
                            icon = Icons.Default.Timer,
                            label = "Duration",
                            value = durationInMinutes,
                            onDecrement = {
                                // Decrease by 5, ensuring it doesn't go below 5
                                durationInMinutes = (durationInMinutes - 5).coerceAtLeast(5)
                            },
                            onIncrement = {
                                // Increase by 5
                                durationInMinutes += 5
                            }
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "What were you doing?",
                        style = MaterialTheme.typography.titleMedium
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        screenDuring.forEach { activity ->
                            FilterChip(
                                selected = activity in selectedActivities,
                                onClick = {
                                    selectedActivities = if (activity in selectedActivities) {
                                        selectedActivities - activity
                                    } else {
                                        selectedActivities + activity
                                    }
                                },
                                label = { Text(activity) }
                            )
                        }
                    }
                }

                // Section for selecting activities
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "What were you doing?",
                        style = MaterialTheme.typography.titleMedium
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        predefinedActivities.forEach { activity ->
                            FilterChip(
                                selected = activity in selectedActivities,
                                onClick = {
                                    selectedActivities = if (activity in selectedActivities) {
                                        selectedActivities - activity
                                    } else {
                                        selectedActivities + activity
                                    }
                                },
                                label = { Text(activity) }
                            )
                        }
                    }
                }
            }

            // Save Button at the bottom
            Button(
                onClick = {
                    // TODO: Handle the save logic with selectedDate, startTime, durationInMinutes, and selectedActivities
                    navController?.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Save Entry", style = MaterialTheme.typography.titleMedium)
            }
        }
    }

    // --- Dialogs for picking Date and Time ---

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        selectedDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
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
        val timePickerState = rememberTimePickerState(
            initialHour = startTime.hour,
            initialMinute = startTime.minute,
            is24Hour = false
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        startTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                        showTimePicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
            },
            text = {
                TimePicker(state = timePickerState, layoutType = TimePickerLayoutType.Vertical)
            },
            title = {
                Text(
                    "Select Time",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
}


// --- HELPER COMPOSABLES ---

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
        Icon(imageVector = icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun DurationStepperRow(
    icon: ImageVector,
    label: String,
    value: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Left side: Icon and Label
        Icon(imageVector = icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))

        // Right side: Stepper controls
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Decrement Button
            OutlinedIconButton(
                onClick = onDecrement,
                modifier = Modifier.size(40.dp),
                // Disable button if duration is 5 or less
                enabled = value > 5
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease duration by 5 minutes")
            }

            // Duration Text
            Text(
                text = "$value min",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(80.dp) // Ensures consistent width
            )

            // Increment Button
            OutlinedIconButton(
                onClick = onIncrement,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Increase duration by 5 minutes")
            }
        }
    }
}*/


