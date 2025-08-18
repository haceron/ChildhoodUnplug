package com.ppp.pegasussociety.Screens

/*import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ppp.pegasussociety.Model.DailyScreenTime
import com.ppp.pegasussociety.Model.TrendData
import com.ppp.pegasussociety.ViewModel.ScreenTimeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.roundToLong*/

// Using a richer color palette by uncommenting your original colors
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.with
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.roundToLong


// Data classes and sample data from your code
val activityColors = mapOf(
    "Feed" to Color(0xFF00ACC1),
    "Outdoor" to Color(0xFF7CB342),
    "Before Sleep" to Color(0xFF5E35B1),
    "Study" to Color(0xFFFDD835),
    "Other" to Color(0xFFDB4437)
)

// Assume these data classes are defined elsewhere in your project
data class ScreenTimeLog(
    val date: String, // "yyyy-MM-dd"
    val totalDuration: Long,
    val activitiesBreakdown: Map<String, Long>
) {
    val parsedDate: LocalDate by lazy { LocalDate.parse(date) }
}
data class DailyScreenTime(
    val date: LocalDate,
    val totalMinutes: Long,
    val activitiesBreakdown: Map<String, Long>
)
data class TrendData(val weeklyTrendPercentage: Double?)

// Mock ViewModel for standalone preview if needed
// class ScreenTimeViewModel: ViewModel() { ... }


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ScreenTimerScreen(navController: NavController? = null) {
    // This part remains unchanged, using your ViewModel
    // val viewModel: ScreenTimeViewModel = hiltViewModel()
    // val logs by viewModel.dailyLogs.collectAsState()
    // val trendData by viewModel.trendData.collectAsState()

    // Using sample data for a complete, runnable example
    val logs = remember {
        listOf(
            ScreenTimeLog("2025-08-14", 150, mapOf("Feed" to 60, "Study" to 90)),
            ScreenTimeLog("2025-08-13", 95, mapOf("Outdoor" to 45, "Other" to 50)),
            ScreenTimeLog("2025-08-12", 210, mapOf("Feed" to 120, "Before Sleep" to 30, "Study" to 60)),
            ScreenTimeLog("2025-08-11", 45, mapOf("Study" to 45)),
            ScreenTimeLog("2025-08-10", 180, mapOf("Outdoor" to 180)),
        )
    }
    val trendData = TrendData(weeklyTrendPercentage = -15.5)


    var selectedRange by remember { mutableStateOf("10 Days") }
    var selectedDailyData by remember { mutableStateOf<DailyScreenTime?>(null) }

    val filteredLogs = remember(logs, selectedRange) {
        val now = LocalDate.now()
        when (selectedRange) {
            "10 Days" -> logs.filter { it.parsedDate >= now.minusDays(10) }
            "Last Month" -> logs.filter { it.parsedDate >= now.minusMonths(1) }
            else -> logs
        }
    }

    val aggregatedData = remember(filteredLogs) {
        filteredLogs.map {
            DailyScreenTime(
                date = it.parsedDate,
                totalMinutes = it.totalDuration,
                activitiesBreakdown = it.activitiesBreakdown
            )
        }.sortedBy { it.date }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Entry") },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Screen Time Entry") },
                onClick = {  navController?.navigate("logscreen")  },
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            )
        }
    ) { padding ->
        if (logs.isEmpty()) {
            EmptyState(modifier = Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            "Your Digital Habit Tracker",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        FilterButtons(selectedRange) { newRange ->
                            selectedRange = newRange
                            selectedDailyData = null
                        }
                    }
                }

                item {
                    ScreenTimeSummaryCard(
                        data = aggregatedData,
                        selectedRange = selectedRange
                    )
                }

                item {
                    MultiSegmentActivityChart(
                        data = aggregatedData,
                        selectedDate = selectedDailyData?.date,
                        onBarClick = { dailyData ->
                            selectedDailyData = if (selectedDailyData?.date == dailyData.date) null else dailyData
                        }
                    )
                }

                item {
                    AnimatedContent(
                        targetState = selectedDailyData,
                        transitionSpec = {
                            fadeIn() with fadeOut() using SizeTransform(clip = false)
                        }, label = "detailsCardAnimation"
                    ) { targetData ->
                        if (targetData != null) {
                            DailyDetailsCard(dailyData = targetData) {
                                selectedDailyData = null
                            }
                        }
                    }
                }

                item {
                    InsightsCard(trendData = trendData, aggregatedData = aggregatedData)
                }
            }
        }
    }
}

// --- THIS IS THE CORRECTED COMPOSABLE ---
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MultiSegmentActivityChart(
    data: List<DailyScreenTime>,
    selectedDate: LocalDate?,
    onBarClick: (DailyScreenTime) -> Unit
) {
    // Max time for Y-axis scaling, ensuring it's at least 60 minutes for a sensible scale
    val maxTime = remember(data) { (data.maxOfOrNull { it.totalMinutes }?.toFloat() ?: 1f).coerceAtLeast(60f) }
    val yAxisLabelCount = 4

    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)) {
            Text("Daily Breakdown", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(24.dp))
            Row(Modifier.height(250.dp)) {
                // Y-Axis
                YAxis(maxTime = maxTime, labelCount = yAxisLabelCount)
                Spacer(Modifier.width(8.dp))

                // Chart Bars
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(data, key = { it.date }) { dailyData ->
                        val isSelected = dailyData.date == selectedDate
                        val scale by animateFloatAsState(targetValue = if (isSelected) 1.05f else 1f, label = "barScale")
                        val barAlpha by animateFloatAsState(targetValue = if (selectedDate == null || isSelected) 1f else 0.7f, label = "barAlpha")

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxHeight()
                                .clickable { onBarClick(dailyData) }
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                    alpha = barAlpha
                                }
                        ) {
                            Text(
                                text = formatDuration(dailyData.totalMinutes),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            // ### THE FIX IS HERE ###
                            // 1. A Box is used to fill the available weighted space.
                            // 2. The bar's height is now a fraction of this Box's height,
                            //    and it's aligned to the bottom.
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                val barHeightRatio = (dailyData.totalMinutes.toFloat() / maxTime).coerceIn(0f, 1f)
                                Column(
                                    modifier = Modifier
                                        .width(40.dp)
                                        .fillMaxHeight(fraction = barHeightRatio) // Proportional height
                                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .border(
                                            width = if (isSelected) 2.dp else 0.dp,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                        ),
                                    verticalArrangement = Arrangement.Bottom
                                ) {
                                    val totalMinutes = dailyData.totalMinutes.toFloat().coerceAtLeast(1f)
                                    // Segment logic remains the same, it will correctly fill the proportionally-sized parent
                                    dailyData.activitiesBreakdown.entries.sortedBy { it.key }.forEach { (activity, duration) ->
                                        val heightRatio by animateFloatAsState(
                                            targetValue = duration.toFloat() / totalMinutes,
                                            animationSpec = tween(durationMillis = 800), label = "segmentHeight"
                                        )
                                        val color = activityColors[activity] ?: activityColors["Other"]!!
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(fraction = heightRatio)
                                                .background(color)
                                        )
                                    }
                                }
                            }

                            Text(
                                text = dailyData.date.format(DateTimeFormatter.ofPattern("d MMM")),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            // Legend
            Text("Legend", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                activityColors.forEach { (name, color) -> LegendItem(name, color) }
            }
        }
    }
}


// --- All other composables remain unchanged ---
// (EmptyState, FilterButtons, ScreenTimeSummaryCard, YAxis, DailyDetailsCard, InsightsCard, etc.)
// The following are included for completeness.

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Analytics,
                contentDescription = "Analytics Icon",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                "No screen time data yet.",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium
            )
            Text(
                "Tap the '+' button to log your first entry and start tracking your digital habits.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterButtons(selected: String, onSelect: (String) -> Unit) {
    val options = listOf("10 Days", "Last Month", "All")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { label ->
            FilterChip(
                selected = selected == label,
                onClick = { onSelect(label) },
                label = { Text(label, fontWeight = FontWeight.Medium) },
                leadingIcon = if (selected == label) {
                    { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize)) }
                } else null,
                elevation = FilterChipDefaults.filterChipElevation(if (selected == label) 2.dp else 0.dp)
            )
        }
    }
}

@Composable
fun ScreenTimeSummaryCard(data: List<DailyScreenTime>, selectedRange: String) {
    val totalMinutes = remember(data) { data.sumOf { it.totalMinutes } }
    val averageMinutes = remember(data) { if (data.isNotEmpty()) totalMinutes / data.size else 0 }

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text(text = formatDuration(averageMinutes), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text(text = "Daily Average", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Divider(modifier = Modifier.fillMaxHeight().width(1.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text(text = formatDuration(totalMinutes), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text(text = "Total in $selectedRange", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun YAxis(maxTime: Float, labelCount: Int, modifier: Modifier = Modifier) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    Box(modifier = modifier.fillMaxHeight().width(35.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val step = size.height / labelCount
            for (i in 0..labelCount) {
                val y = size.height - (i * step)
                if (i > 0) {
                    drawLine(
                        color = onSurfaceVariant.copy(alpha = 0.3f),
                        start = Offset(x = 0f, y = y),
                        end = Offset(x = size.width + 10000, y = y),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = pathEffect
                    )
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            for (i in labelCount downTo 0) {
                val minutes = (maxTime / labelCount) * i
                Text(
                    text = formatDuration(minutes.roundToLong()),
                    style = MaterialTheme.typography.labelSmall,
                    color = onSurfaceVariant,
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
        }
    }
}


@Composable
fun DailyDetailsCard(dailyData: DailyScreenTime, onClose: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Box {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Details for ${dailyData.date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Total Screen Time: ${formatDuration(dailyData.totalMinutes)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val sortedActivities = dailyData.activitiesBreakdown.entries.sortedByDescending { it.value }
                    val totalMinutes = dailyData.totalMinutes.coerceAtLeast(1).toFloat()

                    sortedActivities.forEach { (activity, duration) ->
                        val percentage = (duration / totalMinutes) * 100
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Box(modifier = Modifier.size(10.dp).background(activityColors[activity] ?: Color.Gray, CircleShape))
                                Text(text = activity, style = MaterialTheme.typography.bodyMedium)
                            }
                            Text(
                                text = "${formatDuration(duration)} (${"%.0f".format(percentage)}%)",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopEnd)) {
                Icon(Icons.Default.Close, contentDescription = "Close details")
            }
        }
    }
}


@Composable
fun InsightsCard(trendData: TrendData, aggregatedData: List<DailyScreenTime>) {
    val trend = trendData.weeklyTrendPercentage
    val isReduction = trend != null && trend < 0

    val busiestDay = remember(aggregatedData) {
        aggregatedData.maxByOrNull { it.totalMinutes }
    }
    val topActivity = remember(aggregatedData) {
        aggregatedData
            .flatMap { it.activitiesBreakdown.entries }
            .groupBy({ it.key }, { it.value })
            .mapValues { it.value.sum() }
            .maxByOrNull { it.value }
    }

    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Insights & Trends", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(16.dp))

            if (isReduction) {
                AchievementUnlocked(percentage = abs(trend!!))
                HorizontalDivider(Modifier.padding(vertical = 16.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                busiestDay?.let {
                    InsightItem(
                        icon = Icons.Outlined.CalendarToday,
                        label = "Busiest Day",
                        value = "${it.date.dayOfMonth} ${it.date.month.name.take(3)}",
                        modifier = Modifier.weight(1f)
                    )
                }
                topActivity?.let {
                    InsightItem(
                        icon = Icons.Outlined.Category,
                        label = "Top Activity",
                        value = it.key,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))

        }
    }
}

@Composable
fun AchievementUnlocked(percentage: Double) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                Icons.Default.WorkspacePremium,
                contentDescription = "Achievement",
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(40.dp)
            )
            Column {
                Text(
                    "Great Progress!",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "You've cut your weekly average by ${"%.1f".format(percentage)}%. Keep up the mindful usage!",
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun InsightItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                .padding(8.dp)
        )
        Column {
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
    }
}


@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(modifier = Modifier.size(14.dp).background(color = color, shape = CircleShape))
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}

fun formatDuration(totalMinutes: Long): String {
    if (totalMinutes < 0) return "0m"
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        else -> "${minutes}m"
    }
}
/*
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.roundToLong

// Using a richer color palette by uncommenting your original colors
val activityColors = mapOf(
    "Social Media" to Color(0xFF4285F4),
    "Gaming" to Color(0xFFDB4437),
    "Watching Videos" to Color(0xFFF4B400),
    "Reading" to Color(0xFF0F9D58),
    "Productivity" to Color(0xFF673AB7),
    "Browse" to Color(0xFFFF9800),
    "Messaging" to Color(0xFF2196F3),
    "Education" to Color(0xFF9C27B0),
    "Feed" to Color(0xFF00ACC1),
    "Outdoor" to Color(0xFF7CB342),
    "Before Sleep" to Color(0xFF5E35B1),
    "Study" to Color(0xFFFDD835),
    "Other" to Color(0xFFB0B0B0) // A more neutral 'Other' color
)

@Composable
fun ScreenTimerScreen(navController: NavController? = null) {
    // Assuming the same ViewModel and data classes as in your original code
    // val viewModel: ScreenTimeViewModel = hiltViewModel()
    // val logs by viewModel.dailyLogs.collectAsState()
    // val trendData by viewModel.trendData.collectAsState()

    // Using sample data for a complete, runnable example
    val (logs, trendData) = produceSampleData()

    var selectedRange by remember { mutableStateOf("10 Days") }
    var selectedDailyData by remember { mutableStateOf<DailyScreenTime?>(null) }

    val filteredLogs = remember(logs, selectedRange) {
        val now = LocalDate.now()
        when (selectedRange) {
            "10 Days" -> logs.filter { it.parsedDate >= now.minusDays(10) }
            "Last Month" -> logs.filter { it.parsedDate >= now.minusMonths(1) }
            else -> logs
        }
    }

    val aggregatedData = remember(filteredLogs) {
        filteredLogs.map {
            DailyScreenTime(
                date = it.parsedDate,
                totalMinutes = it.totalDuration,
                activitiesBreakdown = it.activitiesBreakdown
            )
        }.sortedBy { it.date }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Entry") },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Screen Time Entry") },
                onClick = { navController?.navigate("logscreen") },
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            )
        }
    ) { padding ->
        if (logs.isEmpty()) {
            EmptyState(modifier = Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            "Your Digital Habit Tracker",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        FilterButtons(selectedRange) { newRange ->
                            selectedRange = newRange
                            selectedDailyData = null
                        }
                    }
                }

                item {
                    ScreenTimeSummaryCard(
                        data = aggregatedData,
                        selectedRange = selectedRange
                    )
                }

                item {
                    MultiSegmentActivityChart(
                        data = aggregatedData,
                        selectedDate = selectedDailyData?.date,
                        onBarClick = { dailyData ->
                            selectedDailyData = if (selectedDailyData?.date == dailyData.date) null else dailyData
                        }
                    )
                }

                item {
                    AnimatedContent(
                        targetState = selectedDailyData,
                        transitionSpec = {
                            fadeIn() with fadeOut() using SizeTransform(clip = false)
                        }, label = "detailsCardAnimation"
                    ) { targetData ->
                        if (targetData != null) {
                            DailyDetailsCard(dailyData = targetData) {
                                selectedDailyData = null
                            }
                        }
                    }
                }

                item {
                    InsightsCard(trendData = trendData, aggregatedData = aggregatedData)
                }
            }
        }
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Analytics,
                contentDescription = "Analytics Icon",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                "No screen time data yet.",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium
            )
            Text(
                "Tap the '+' button to log your first entry and start tracking your digital habits.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterButtons(selected: String, onSelect: (String) -> Unit) {
    val options = listOf("10 Days", "Last Month", "All")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { label ->
            FilterChip(
                selected = selected == label,
                onClick = { onSelect(label) },
                label = { Text(label, fontWeight = FontWeight.Medium) },
                leadingIcon = if (selected == label) {
                    { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize)) }
                } else null,
                elevation = FilterChipDefaults.filterChipElevation(if (selected == label) 2.dp else 0.dp)
            )
        }
    }
}

@Composable
fun ScreenTimeSummaryCard(data: List<DailyScreenTime>, selectedRange: String) {
    val totalMinutes = remember(data) { data.sumOf { it.totalMinutes } }
    val averageMinutes = remember(data) { if (data.isNotEmpty()) totalMinutes / data.size else 0 }

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text(text = formatDuration(averageMinutes), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text(text = "Daily Average", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Divider(modifier = Modifier.fillMaxHeight().width(1.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text(text = formatDuration(totalMinutes), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text(text = "Total in $selectedRange", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}


@Composable
fun MultiSegmentActivityChart(
    data: List<DailyScreenTime>,
    selectedDate: LocalDate?,
    onBarClick: (DailyScreenTime) -> Unit
) {
    val maxTime = remember(data) { (data.maxOfOrNull { it.totalMinutes }?.toFloat() ?: 1f).coerceAtLeast(60f) }
    val yAxisLabelCount = 4
    val density = LocalDensity.current

    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)) {
            Text("Daily Breakdown", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(24.dp))
            Row(Modifier.height(250.dp)) {
                // Y-Axis
                YAxis(maxTime = maxTime, labelCount = yAxisLabelCount)
                Spacer(Modifier.width(8.dp))

                // Chart Bars
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(data, key = { it.date }) { dailyData ->
                        val isSelected = dailyData.date == selectedDate
                        val scale by animateFloatAsState(targetValue = if (isSelected) 1.05f else 1f, label = "barScale")
                        val barAlpha by animateFloatAsState(targetValue = if (selectedDate == null || isSelected) 1f else 0.7f, label = "barAlpha")

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxHeight()
                                .clickable { onBarClick(dailyData) }
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                    alpha = barAlpha
                                }
                        ) {
                            Text(
                                text = formatDuration(dailyData.totalMinutes),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            // Animated Bar Segments
                            Column(
                                modifier = Modifier
                                    .width(40.dp)
                                    .weight(1f)
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .border(
                                        width = if (isSelected) 2.dp else 0.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                    ),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                val totalMinutes = dailyData.totalMinutes.toFloat().coerceAtLeast(1f)
                                dailyData.activitiesBreakdown.entries.sortedBy { it.key }.forEach { (activity, duration) ->
                                    val heightRatio by animateFloatAsState(
                                        targetValue = duration.toFloat() / totalMinutes,
                                        animationSpec = androidx.compose.animation.core.tween(durationMillis = 800), label = "segmentHeight"
                                    )
                                    val color = activityColors[activity] ?: activityColors["Other"]!!
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(fraction = heightRatio)
                                            .background(color)
                                    )
                                }
                            }
                            Text(
                                text = dailyData.date.format(DateTimeFormatter.ofPattern("d MMM")),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            // Legend
            Text("Legend", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(12.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                activityColors.forEach { (name, color) -> LegendItem(name, color) }
            }
        }
    }
}

@Composable
fun YAxis(maxTime: Float, labelCount: Int, modifier: Modifier = Modifier) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    Box(modifier = modifier.fillMaxHeight().width(35.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val step = size.height / labelCount
            for (i in 0..labelCount) {
                val y = size.height - (i * step)
                if (i > 0) { // Don't draw line at the bottom
                    drawLine(
                        color = onSurfaceVariant.copy(alpha = 0.3f),
                        start = Offset(x = 0f, y = y),
                        end = Offset(x = size.width + 10000, y = y), // Draw a very long line to extend behind the bars
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = pathEffect
                    )
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            for (i in labelCount downTo 0) {
                val minutes = (maxTime / labelCount) * i
                Text(
                    text = formatDuration(minutes.roundToLong()),
                    style = MaterialTheme.typography.labelSmall,
                    color = onSurfaceVariant,
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
        }
    }
}


@Composable
fun DailyDetailsCard(dailyData: DailyScreenTime, onClose: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Box {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Details for ${dailyData.date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Total Screen Time: ${formatDuration(dailyData.totalMinutes)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                // Activity Breakdown with Percentages
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val sortedActivities = dailyData.activitiesBreakdown.entries.sortedByDescending { it.value }
                    val totalMinutes = dailyData.totalMinutes.coerceAtLeast(1).toFloat()

                    sortedActivities.forEach { (activity, duration) ->
                        val percentage = (duration / totalMinutes) * 100
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Box(modifier = Modifier.size(10.dp).background(activityColors[activity] ?: Color.Gray, CircleShape))
                                Text(text = activity, style = MaterialTheme.typography.bodyMedium)
                            }
                            Text(
                                text = "${formatDuration(duration)} (${"%.0f".format(percentage)}%)",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopEnd)) {
                Icon(Icons.Default.Close, contentDescription = "Close details")
            }
        }
    }
}


@Composable
fun InsightsCard(trendData: TrendData, aggregatedData: List<DailyScreenTime>) {
    val trend = trendData.weeklyTrendPercentage
    val isReduction = trend != null && trend < 0

    val busiestDay = remember(aggregatedData) {
        aggregatedData.maxByOrNull { it.totalMinutes }
    }
    val topActivity = remember(aggregatedData) {
        aggregatedData
            .flatMap { it.activitiesBreakdown.entries }
            .groupBy({ it.key }, { it.value })
            .mapValues { it.value.sum() }
            .maxByOrNull { it.value }
    }

    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Insights & Trends", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(16.dp))

            if (isReduction) {
                AchievementUnlocked(percentage = abs(trend!!))
                HorizontalDivider(Modifier.padding(vertical = 16.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                busiestDay?.let {
                    InsightItem(
                        icon = Icons.Outlined.CalendarToday,
                        label = "Busiest Day",
                        value = "${it.date.dayOfMonth} ${it.date.month.name.take(3)}",
                        modifier = Modifier.weight(1f)
                    )
                }
                topActivity?.let {
                    InsightItem(
                        icon = Icons.Outlined.Category,
                        label = "Top Activity",
                        value = it.key,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun AchievementUnlocked(percentage: Double) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                Icons.Default.WorkspacePremium,
                contentDescription = "Achievement",
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(40.dp)
            )
            Column {
                Text(
                    "Great Progress!",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "You've cut your weekly average by ${"%.1f".format(percentage)}%. Keep up the mindful usage!",
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun InsightItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                .padding(8.dp)
        )
        Column {
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
    }
}


@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(modifier = Modifier.size(14.dp).background(color = color, shape = CircleShape))
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}

fun formatDuration(totalMinutes: Long): String {
    if (totalMinutes < 0) return "0m"
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        else -> "${minutes}m"
    }
}


//region Sample Data (for demonstration)
// You can remove this section and use your actual ViewModel
@Composable
fun produceSampleData(): Pair<List<ScreenTimeLog>, TrendData> {
    val sampleLogs = remember {
        List(20) { i ->
            val date = LocalDate.now().minusDays(i.toLong())
            val activities = activityColors.keys.shuffled().take(kotlin.random.Random.nextInt(2, 5))
            val breakdown = activities.associateWith { kotlin.random.Random.nextLong(15, 90) }
            ScreenTimeLog(
                date = date.toString(),
                parsedDate = date,
                activitiesBreakdown = breakdown,
                totalDuration = breakdown.values.sum()
            )
        }
    }
    val trendData = remember {
        TrendData(
            last7DaysAverage = 185.0,
            weeklyTrendPercentage = -12.5 // Negative for reduction achievement
        )
    }
    return Pair(sampleLogs, trendData)
}

// Data classes matching the ones inferred from your code
data class ScreenTimeLog(
    val date: String,
    val parsedDate: LocalDate,
    val totalDuration: Long,
    val activitiesBreakdown: Map<String, Long>
)

data class DailyScreenTime(
    val date: LocalDate,
    val totalMinutes: Long,
    val activitiesBreakdown: Map<String, Long>
)

data class TrendData(
    val last7DaysAverage: Double,
    val weeklyTrendPercentage: Double?
)
//endregion
*/



/*
package com.ppp.pegasussociety.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ppp.pegasussociety.Model.DailyScreenTime
import com.ppp.pegasussociety.Model.TrendData
import com.ppp.pegasussociety.ViewModel.ScreenTimeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.abs

val activityColors = mapOf(
    */
/*"Social Media" to Color(0xFF4285F4),
    "Gaming" to Color(0xFFDB4437),
    "Watching Videos" to Color(0xFFF4B400),
    "Reading" to Color(0xFF0F9D58),
    "Productivity" to Color(0xFF673AB7),
    "Browse" to Color(0xFFFF9800),
    "Messaging" to Color(0xFF2196F3),
    "Education" to Color(0xFF9C27B0),*//*

    "Feed" to Color(0xFF00ACC1),
    "Outdoor" to Color(0xFF7CB342),
    "Before Sleep" to Color(0xFF5E35B1),
    "Study" to Color(0xFFFDD835),
    "Other" to Color(0xFFDB4437)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTimerScreen(navController: NavController? = null) {
    val viewModel: ScreenTimeViewModel = hiltViewModel()

    val logs by viewModel.dailyLogs.collectAsState()
    val trendData by viewModel.trendData.collectAsState()

    var selectedRange by remember { mutableStateOf("10 Days") }
    var selectedDailyData by remember { mutableStateOf<DailyScreenTime?>(null) }

    val filteredLogs = remember(logs, selectedRange) {
        when (selectedRange) {
            "10 Days" -> logs.filter { it.parsedDate >= LocalDate.now().minusDays(10) }
            "Last Month" -> logs.filter { it.parsedDate >= LocalDate.now().minusMonths(1) }
            else -> logs
        }
    }

    val aggregatedData = remember(filteredLogs) {
        filteredLogs.map {
            DailyScreenTime(
                date = it.parsedDate,
                totalMinutes = it.totalDuration,
                activitiesBreakdown = it.activitiesBreakdown
            )
        }.sortedBy { it.date }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Entry") },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Screen Time Entry") },
                onClick = { navController?.navigate("logscreen") }
            )
        }
    ) { padding ->
        if (logs.isEmpty()) {   //  && !isTrendDataLoading
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No data yet.\nTap '+' to add your first entry.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    FilterButtons(selectedRange) { newRange ->
                        selectedRange = newRange
                        selectedDailyData = null
                    }
                }

                item {
                    AnimatedVisibility(
                        visible = selectedDailyData != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        selectedDailyData?.let { data ->
                            DailyDetailsCard(dailyData = data) {
                                selectedDailyData = null
                            }
                        }
                    }
                }

                item {
                    if (logs.isNotEmpty()) {
                        TrendsAndAchievementsCard(trendData = trendData)
                    }
                }

                item {
                    MultiSegmentActivityChart(
                        data = aggregatedData,
                        selectedDate = selectedDailyData?.date,
                        onBarClick = { dailyData ->
                            selectedDailyData = if (selectedDailyData == dailyData) null else dailyData
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterButtons(selected: String, onSelect: (String) -> Unit) {
    val options = listOf("10 Days", "Last Month", "All")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { label ->
            FilterChip(
                selected = selected == label,
                onClick = { onSelect(label) },
                label = { Text(label) }
            )
        }
    }
}

fun formatDuration(totalMinutes: Long): String {
    if (totalMinutes < 0) return "0m"
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        else -> "${minutes}m"
    }
}

@Composable
fun DailyDetailsCard(dailyData: DailyScreenTime, onClose: () -> Unit) {
    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Box {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Details for ${dailyData.date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Total: ${formatDuration(dailyData.totalMinutes)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Divider()
                dailyData.activitiesBreakdown.entries.sortedByDescending { it.value }.forEach { (activity, duration) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(8.dp).background(activityColors[activity] ?: Color.Gray, CircleShape))
                            Text(text = activity, style = MaterialTheme.typography.bodyMedium)
                        }
                        Text(
                            text = formatDuration(duration),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopEnd)) {
                Icon(Icons.Default.Close, contentDescription = "Close details")
            }
        }
    }
}

@Composable
fun TrendsAndAchievementsCard(trendData: TrendData) {
    val trend = trendData.weeklyTrendPercentage
    val isReduction = trend != null && trend < 0

    val cardColors = if (isReduction) {
        CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
    } else {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    }

    Card(elevation = CardDefaults.cardElevation(4.dp), colors = cardColors) {
        Column(Modifier.padding(16.dp)) {
            if (isReduction) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Achievement", tint = Color(0xFF2E7D32))
                    Text("Achievement Unlocked!", style = MaterialTheme.typography.titleMedium, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                }
                Text("You've reduced your weekly average by ${"%.1f".format(abs(trend!!))}%! Keep it up.", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
                Divider(Modifier.padding(vertical = 12.dp))
            } else {
                Text("Screen Time Trends", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                TrendStat("Last 7 Days Avg.", "${"%.1f".format(trendData.last7DaysAverage / 60f)}h / day")
            }
        }
    }
}

@Composable
fun RowScope.TrendStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
        Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(text = label, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
    }
}

@Composable
fun MultiSegmentActivityChart(
    data: List<DailyScreenTime>,
    selectedDate: LocalDate?,
    onBarClick: (DailyScreenTime) -> Unit
) {
    val maxTime = data.maxOfOrNull { it.totalMinutes }?.toFloat() ?: 1f

    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text("Daily Activity Breakdown", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))
            Row(Modifier.height(220.dp)) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(data, key = { it.date }) { dailyData ->
                        val isSelected = dailyData.date == selectedDate
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .clickable { onBarClick(dailyData) }
                                .padding(top = 4.dp)
                        ) {
                            Text(
                                text = "%.1fh".format(dailyData.totalMinutes / 60f),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current
                            )
                            Column(
                                modifier = Modifier
                                    .width(40.dp)
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        width = if (isSelected) 2.dp else 0.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                if (dailyData.totalMinutes == 0L) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(MaterialTheme.colorScheme.surfaceVariant)
                                    )
                                } else {
                                    dailyData.activitiesBreakdown.entries.sortedBy { it.key }.forEach { (activity, duration) ->
                                        val color = activityColors[activity] ?: activityColors["Other"]!!
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(((duration.toFloat() / maxTime) * 180).dp)
                                                .background(color)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = dailyData.date.format(DateTimeFormatter.ofPattern("d MMM")),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                activityColors.forEach { (name, color) ->
                    LegendItem(name, color)
                }
            }
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(modifier = Modifier.size(12.dp).background(color = color, shape = CircleShape))
        Text(text = label, fontSize = 12.sp)
    }
}
*/

/*package com.ppp.pegasussociety.Screens


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import com.ppp.pegasussociety.Model.DailyScreenTime
import com.ppp.pegasussociety.Model.TrendData
import com.ppp.pegasussociety.ViewModel.ScreenTimerViewModel
import okhttp3.internal.concurrent.formatDuration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.abs

// --- DATA & COLORS ---
val activityColors = mapOf(
    "Social Media" to Color(0xFF4285F4),
    "Gaming" to Color(0xFFDB4437),
    "Watching Videos" to Color(0xFFF4B400),
    "Reading" to Color(0xFF0F9D58),
    "Productivity" to Color(0xFF673AB7),
    "Browse" to Color(0xFFFF9800),
    "Messaging" to Color(0xFF2196F3),
    "Education" to Color(0xFF9C27B0),
    "Feed" to Color(0xFF00ACC1),
    "Outdoor" to Color(0xFF7CB342),
    "Before Sleep" to Color(0xFF5E35B1),
    "Study" to Color(0xFFFDD835),
    "Other" to Color.Gray
)

// --- MAIN SCREEN COMPOSABLE ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTimerScreen(navController: NavController) {
    val viewModel: ScreenTimerViewModel = hiltViewModel()
    val entries by viewModel.entries.collectAsState()
    var selectedRange by remember { mutableStateOf("10 Days") }
    var selectedDailyData by remember { mutableStateOf<DailyScreenTime?>(null) }
    var trendData by remember { mutableStateOf(TrendData()) }
    var isTrendDataLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = entries) { // Re-fetch trend data if entries change
        try {
            isTrendDataLoading = true
            trendData = viewModel.getTrendData()
        } catch (e: Exception) {
            Log.e("ScreenTimerScreen", "Failed to fetch trend data", e)
        } finally {
            isTrendDataLoading = false
        }
    }

    val filteredEntries = when (selectedRange) {
        "10 Days" -> entries.filter { it.dateTime.toLocalDate() >= LocalDate.now().minusDays(10) }
        "Last Month" -> entries.filter { it.dateTime.toLocalDate() >= LocalDate.now().minusMonths(1) }
        else -> entries
    }

    val aggregatedData = viewModel.getAggregatedDataForChart(filteredEntries)
    Log.d("entries", entries.toString())


    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Entry") },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Screen Time Entry") },
                onClick = { navController.navigate("log_screen_time") }
            )
        }
    ) { padding ->
        if (entries.isEmpty() ) {   //  && !isTrendDataLoading
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(
                    "No data yet.\nTap '+' to add your first entry.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    FilterButtons(selectedRange) { newRange ->
                        selectedRange = newRange
                        selectedDailyData = null
                    }
                }

                item {
                    AnimatedVisibility(
                        visible = selectedDailyData != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        selectedDailyData?.let { data ->
                            DailyDetailsCard(dailyData = data) {
                                selectedDailyData = null
                            }
                        }
                    }
                }

                item {
                    if (isTrendDataLoading) {
                        Text("Loading trends...")
                    } else if (entries.isNotEmpty()) {
                        TrendsAndAchievementsCard(trendData = trendData)
                    }
                }

                item {
                    MultiSegmentActivityChart(
                        data = aggregatedData,
                        selectedDate = selectedDailyData?.date,
                        onBarClick = { dailyData ->
                            selectedDailyData = if (selectedDailyData == dailyData) null else dailyData
                        }
                    )
                }
            }
        }
    }
}

// --- CHILD COMPOSABLES & HELPERS ---

@Composable
fun FilterButtons(selected: String, onSelect: (String) -> Unit) {
    val options = listOf("10 Days", "Last Month", "All")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { label ->
            FilterChip(
                selected = selected == label,
                onClick = { onSelect(label) },
                label = { Text(label) }
            )
        }
    }
}

// CORRECTED: Function now accepts a Long to match the data model.
fun formatDuration(totalMinutes: Long): String {
    if (totalMinutes < 0) return "0m"
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        else -> "${minutes}m"
    }
}

@Composable
fun DailyDetailsCard(dailyData: DailyScreenTime, onClose: () -> Unit) {
    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Box {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Details for ${dailyData.date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Total: ${formatDuration(dailyData.totalMinutes)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Divider()
                // CORRECTED: Use 'activitiesBreakdown' to match the data class.
                dailyData.activitiesBreakdown.entries.sortedByDescending { it.value }.forEach { (activity, duration) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(8.dp).background(activityColors[activity] ?: Color.Gray, CircleShape))
                            Text(text = activity, style = MaterialTheme.typography.bodyMedium)
                        }
                        Text(
                            text = formatDuration(duration),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopEnd)) {
                Icon(Icons.Default.Close, contentDescription = "Close details")
            }
        }
    }
}

@Composable
fun TrendsAndAchievementsCard(trendData: TrendData) {
    val trend = trendData.weeklyTrendPercentage
    val isReduction = trend != null && trend < 0

    val cardColors = if (isReduction) {
        CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
    } else {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    }

    Card(elevation = CardDefaults.cardElevation(4.dp), colors = cardColors) {
        Column(Modifier.padding(16.dp)) {
            if (isReduction) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Achievement", tint = Color(0xFF2E7D32))
                    Text("Achievement Unlocked!", style = MaterialTheme.typography.titleMedium, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                }
                Text("You've reduced your weekly average by ${"%.1f".format(abs(trend!!))}%! Keep it up.", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
                Divider(Modifier.padding(vertical = 12.dp))
            } else {
                Text("Screen Time Trends", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                TrendStat("Last 7 Days Avg.", "${"%.1f".format(trendData.last7DaysAverage / 60f)}h / day")
              //  TrendStat("Overall Avg.", "${"%.1f".format(trendData.overallAverage / 60f)}h / day")
            }
        }
    }
}

@Composable
fun RowScope.TrendStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
        Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(text = label, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
    }
}

@Composable
fun MultiSegmentActivityChart(data: List<DailyScreenTime>, selectedDate: LocalDate?, onBarClick: (DailyScreenTime) -> Unit) {
    val maxTime = data.maxOfOrNull { it.totalMinutes }?.toFloat() ?: 1f

    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text("Daily Activity Breakdown", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))
            Row(Modifier.height(220.dp)) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(data, key = { it.date }) { dailyData ->
                        val isSelected = dailyData.date == selectedDate
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.clickable { onBarClick(dailyData) }.padding(top = 4.dp)
                        ) {
                            Text(text = "%.1fh".format(dailyData.totalMinutes / 60f), style = MaterialTheme.typography.labelSmall, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current)
                            Column(
                                modifier = Modifier.width(40.dp).weight(1f).clip(RoundedCornerShape(8.dp)).border(width = if (isSelected) 2.dp else 0.dp, color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent, shape = RoundedCornerShape(8.dp)),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                if (dailyData.totalMinutes == 0L) {
                                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant))
                                } else {
                                    // CORRECTED: Use 'activitiesBreakdown' to match the data class.
                                    dailyData.activitiesBreakdown.entries.sortedBy { it.key }.forEach { (activity, duration) ->
                                        val color = activityColors[activity] ?: activityColors["Other"]!!
                                        Box(modifier = Modifier.fillMaxWidth().height(((duration.toFloat() / maxTime) * 180).dp).background(color))
                                    }
                                }
                            }
                            Text(text = dailyData.date.format(DateTimeFormatter.ofPattern("d MMM")), style = MaterialTheme.typography.bodySmall, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current)
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                activityColors.forEach { (name, color) ->
                    LegendItem(name, color)
                }
            }
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(modifier = Modifier.size(12.dp).background(color = color, shape = CircleShape))
        Text(text = label, fontSize = 12.sp)
    }
}*/
/*

///--------------UpTO--------------------------------------------------------------////
















val activityColors = mapOf(
    "Social Media" to Color(0xFF4285F4),
    "Gaming" to Color(0xFFDB4437),
    "Watching Videos" to Color(0xFFF4B400),
    "Reading" to Color(0xFF0F9D58),
    "Productivity" to Color(0xFF673AB7),
    "Browse" to Color(0xFFFF9800),
    "Messaging" to Color(0xFF2196F3),
    "Education" to Color(0xFF9C27B0),
    "Other" to Color.Gray
)

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTimerScreen(navController: NavController) {
    val viewModel: ScreenTimerViewModel = hiltViewModel()
    val entries by viewModel.entries.collectAsState()
    var selectedRange by remember { mutableStateOf("10 Days") }

    // State to hold the data for the selected bar
    var selectedDailyData by remember { mutableStateOf<DailyScreenTime?>(null) }

    // State to hold the TrendData fetched from the suspend function
    var trendData by remember { mutableStateOf(TrendData()) }
    var isTrendDataLoading by remember { mutableStateOf(true) }

    // Use LaunchedEffect to safely call the suspend function
    // The key ensures that this block only runs when the screen is first composed
    // or when the key changes. Since it's Unit, it runs only once.
    LaunchedEffect(key1 = Unit) {
        try {
            isTrendDataLoading = true
            trendData = viewModel.getTrendData()
        } catch (e: Exception) {
            Log.e("ScreenTimerScreen", "Failed to fetch trend data", e)
        } finally {
            isTrendDataLoading = false
        }
    }

    val filteredEntries = when (selectedRange) {
        "10 Days" -> entries.filter { it.dateTime.toLocalDate() >= LocalDate.now().minusDays(10) }
        "Last Month" -> entries.filter { it.dateTime.toLocalDate() >= LocalDate.now().minusMonths(1) }
        else -> entries
    }

    val aggregatedData = viewModel.getAggregatedDataForChart(filteredEntries)

    Log.d("entries", entries.toString())

    Scaffold(
        */
/* topBar = {
             TopAppBar(
                 title = { Text("📊 Screen Time Dashboard") },
                 colors = TopAppBarDefaults.topAppBarColors(
                     containerColor = MaterialTheme.colorScheme.primaryContainer
                 )
             )
         },*//*

        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Entry") },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Screen Time Entry") },
                onClick = { navController.navigate("log_screen_time") }
            )
        }
    ) { padding ->
        if (entries.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(
                    "No data yet.\nTap '+' to add your first entry. ${entries}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
                Log.d("entriesemp", entries.toString())
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Log.d("entriessuc", entries.toString())

                item {
                    FilterButtons(selectedRange) { newRange ->
                        selectedRange = newRange
                        selectedDailyData = null // Deselect bar when range changes
                    }
                }

                item {
                    AnimatedVisibility(
                        visible = selectedDailyData != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        selectedDailyData?.let { data ->
                            DailyDetailsCard(dailyData = data) {
                                selectedDailyData = null // Deselect on close
                            }
                        }
                    }
                }

                // Show the Trends card only after the data has loaded
                item {
                    if (!isTrendDataLoading) {
                        TrendsAndAchievementsCard(trendData = trendData)
                    } else {
                        // You can add a loading indicator here if you want
                        Text("Loading trends...")
                    }
                }

                item {
                    MultiSegmentActivityChart(
                        data = aggregatedData,
                        selectedDate = selectedDailyData?.date, // Pass selected date for highlighting
                        onBarClick = { dailyData ->
                            // Set or unset the selected data on bar click
                            selectedDailyData = if (selectedDailyData == dailyData) null else dailyData
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterButtons(selected: String, onSelect: (String) -> Unit) {
    val options = listOf("10 Days", "Last Month", "All")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { label ->
            FilterChip(
                selected = selected == label,
                onClick = { onSelect(label) },
                label = { Text(label) }
            )
        }
    }
}

// Helper function to format minutes into "1h 30m"
fun formatDuration(totalMinutes: Int): String {
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        else -> "${minutes}m"
    }
}

@Composable
fun DailyDetailsCard(dailyData: DailyScreenTime, onClose: () -> Unit) {
    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Box {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Details for ${dailyData.date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Total: ${formatDuration(dailyData.totalMinutes)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Divider()
                // Corrected line: Use 'activitiesBreakdown'
                dailyData.activitiesBreakdown.entries.sortedByDescending { it.value }.forEach { (activity, duration) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(activityColors[activity] ?: Color.Gray, CircleShape)
                            )
                            Text(text = activity, style = MaterialTheme.typography.bodyMedium)
                        }
                        Text(
                            text = formatDuration(duration),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopEnd)) {
                Icon(Icons.Default.Close, contentDescription = "Close details")
            }
        }
    }
}
@Composable
fun TrendsAndAchievementsCard(trendData: TrendData) {
    val trend = trendData.weeklyTrendPercentage
    val isReduction = trend != null && trend < 0

    val cardColors = if (isReduction) {
        CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)) // Light green for achievement
    } else {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    }

    Card(elevation = CardDefaults.cardElevation(4.dp), colors = cardColors) {
        Column(Modifier.padding(16.dp)) {
            if (isReduction) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Achievement",
                        tint = Color(0xFF2E7D32) // Dark green
                    )
                    Text(
                        "Achievement Unlocked!",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    "You've reduced your weekly average by ${"%.1f".format(kotlin.math.abs(trend!!))}%! Keep it up.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Divider(Modifier.padding(vertical = 12.dp))
            } else {
                Text("Screen Time Trends", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }



            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TrendStat("Last 7 Days Avg.", "${"%.1f".format(trendData.last7DaysAverage / 60f)}h / day")
                TrendStat("Overall Avg.", "${"%.1f".format(trendData.overallAverage / 60f)}h / day")
            }
        }
    }
}

@Composable
fun RowScope.TrendStat(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MultiSegmentActivityChart(
    data: List<DailyScreenTime>,
    selectedDate: LocalDate?,
    onBarClick: (DailyScreenTime) -> Unit
) {
    val maxTime = data.maxOfOrNull { it.totalMinutes }?.toFloat() ?: 1f

    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text("Daily Activity Breakdown", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))
            Row(Modifier.height(220.dp)) { // Increased height for total time text
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(data, key = { it.date }) { dailyData ->
                        val isSelected = dailyData.date == selectedDate
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .clickable { onBarClick(dailyData) }
                                .padding(top = 4.dp)
                        ) {
                            Text(
                                text = "%.1fh".format(dailyData.totalMinutes / 60f),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current
                            )
                            Column(
                                modifier = Modifier
                                    .width(40.dp)
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        width = if (isSelected) 2.dp else 0.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                // Background for empty bar
                                if (dailyData.totalMinutes == 0) {
                                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant))
                                } else {
                                    dailyData.activities.entries.sortedBy { it.key }.forEach { (activity, duration) ->
                                        val color = activityColors[activity] ?: activityColors["Other"]!!
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(((duration / maxTime) * 180).dp) // Adjusted height
                                                .background(color)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = dailyData.date.format(DateTimeFormatter.ofPattern("d MMM")),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                activityColors.forEach { (name, color) ->
                    LegendItem(name, color)
                }
            }
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color = color, shape = CircleShape)
        )
        Text(text = label, fontSize = 12.sp)
    }
}
*/

/*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import com.ppp.pegasussociety.Model.DailyScreenTime
import com.ppp.pegasussociety.Model.TrendData
import com.ppp.pegasussociety.ViewModel.ScreenTimerViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
*/

// A map of colors for chart segments
/*val activityColors = mapOf(
    "Social Media" to Color(0xFF4285F4),
    "Gaming" to Color(0xFFDB4437),
    "Watching Videos" to Color(0xFFF4B400),
    "Reading" to Color(0xFF0F9D58),
    "Productivity" to Color(0xFF673AB7),
    "Browse" to Color(0xFFFF9800),
    "Messaging" to Color(0xFF2196F3),
    "Education" to Color(0xFF9C27B0),
    "Other" to Color.Gray
)

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTimerScreen(navController: NavController) {
    val viewModel: ScreenTimerViewModel = hiltViewModel()
    val entries by viewModel.entries.collectAsState()
    var selectedRange by remember { mutableStateOf("10 Days") }

    // State to hold the data for the selected bar
    var selectedDailyData by remember { mutableStateOf<DailyScreenTime?>(null) }

    val filteredEntries = when (selectedRange) {
        "10 Days" -> entries.filter { it.dateTime.toLocalDate() >= LocalDate.now().minusDays(10) }
        "Last Month" -> entries.filter { it.dateTime.toLocalDate() >= LocalDate.now().minusMonths(1) }
        else -> entries
    }

    val aggregatedData = viewModel.getAggregatedDataForChart(filteredEntries)
    val trendData = viewModel.getTrendData()

    Log.d("entries", entries.toString())

    Scaffold(
*//*        topBar = {
            TopAppBar(
                title = { Text("📊 Screen Time Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },*//*
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Entry") },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Screen Time Entry") },
                onClick = { navController.navigate("log_screen_time") }
            )
        }
    ) { padding ->
        if (entries.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(
                    "No data yet.\nTap '+' to add your first entry. ${entries}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
                Log.d("entriesemp", entries.toString())

            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Log.d("entriessuc", entries.toString())

                item {
                    FilterButtons(selectedRange) { newRange ->
                        selectedRange = newRange
                        selectedDailyData = null // Deselect bar when range changes
                    }
                }

                item {
                    AnimatedVisibility(
                        visible = selectedDailyData != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        selectedDailyData?.let { data ->
                            DailyDetailsCard(dailyData = data) {
                                selectedDailyData = null // Deselect on close
                            }
                        }
                    }
                }

                item {
                    TrendsAndAchievementsCard(trendData = trendData)
                }

                item {
                    MultiSegmentActivityChart(
                        data = aggregatedData,
                        selectedDate = selectedDailyData?.date, // Pass selected date for highlighting
                        onBarClick = { dailyData ->
                            // Set or unset the selected data on bar click
                            selectedDailyData = if (selectedDailyData == dailyData) null else dailyData
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterButtons(selected: String, onSelect: (String) -> Unit) {
    val options = listOf("10 Days", "Last Month", "All")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { label ->
            FilterChip(
                selected = selected == label,
                onClick = { onSelect(label) },
                label = { Text(label) }
            )
        }
    }
}

// Helper function to format minutes into "1h 30m"
fun formatDuration(totalMinutes: Int): String {
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        else -> "${minutes}m"
    }
}

@Composable
fun DailyDetailsCard(dailyData: DailyScreenTime, onClose: () -> Unit) {
    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Box {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Details for ${dailyData.date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Total: ${formatDuration(dailyData.totalMinutes)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Divider()
                dailyData.activities.entries.sortedByDescending { it.value }.forEach { (activity, duration) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(activityColors[activity] ?: Color.Gray, CircleShape)
                            )
                            Text(text = activity, style = MaterialTheme.typography.bodyMedium)
                        }
                        Text(
                            text = formatDuration(duration),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopEnd)) {
                Icon(Icons.Default.Close, contentDescription = "Close details")
            }
        }
    }
}

@Composable
fun TrendsAndAchievementsCard(trendData: TrendData) {
    val trend = trendData.weeklyTrendPercentage
    val isReduction = trend != null && trend < 0

    val cardColors = if (isReduction) {
        CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)) // Light green for achievement
    } else {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    }

    Card(elevation = CardDefaults.cardElevation(4.dp), colors = cardColors) {
        Column(Modifier.padding(16.dp)) {
            if (isReduction) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Achievement",
                        tint = Color(0xFF2E7D32) // Dark green
                    )
                    Text(
                        "Achievement Unlocked!",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    "You've reduced your weekly average by ${"%.1f".format(kotlin.math.abs(trend!!))}%! Keep it up.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Divider(Modifier.padding(vertical = 12.dp))
            } else {
                Text("Screen Time Trends", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TrendStat("Last 7 Days Avg.", "${"%.1f".format(trendData.last7DaysAverage / 60f)}h / day")
                TrendStat("Overall Avg.", "${"%.1f".format(trendData.overallAverage / 60f)}h / day")
            }
        }
    }
}

@Composable
fun RowScope.TrendStat(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MultiSegmentActivityChart(
    data: List<DailyScreenTime>,
    selectedDate: LocalDate?,
    onBarClick: (DailyScreenTime) -> Unit
) {
    val maxTime = data.maxOfOrNull { it.totalMinutes }?.toFloat() ?: 1f

    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text("Daily Activity Breakdown", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))
            Row(Modifier.height(220.dp)) { // Increased height for total time text
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(data, key = { it.date }) { dailyData ->
                        val isSelected = dailyData.date == selectedDate
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .clickable { onBarClick(dailyData) }
                                .padding(top = 4.dp)
                        ) {
                            Text(
                                text = "%.1fh".format(dailyData.totalMinutes / 60f),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current
                            )
                            Column(
                                modifier = Modifier
                                    .width(40.dp)
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        width = if (isSelected) 2.dp else 0.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                // Background for empty bar
                                if (dailyData.totalMinutes == 0) {
                                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant))
                                } else {
                                    dailyData.activities.entries.sortedBy { it.key }.forEach { (activity, duration) ->
                                        val color = activityColors[activity] ?: activityColors["Other"]!!
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(((duration / maxTime) * 180).dp) // Adjusted height
                                                .background(color)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = dailyData.date.format(DateTimeFormatter.ofPattern("d MMM")),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                activityColors.forEach { (name, color) ->
                    LegendItem(name, color)
                }
            }
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color = color, shape = CircleShape)
        )
        Text(text = label, fontSize = 12.sp)
    }
}*/
/*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ppp.pegasussociety.Model.DailyScreenTime
import com.ppp.pegasussociety.Model.TrendData
import com.ppp.pegasussociety.ViewModel.ScreenTimerViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// A map of colors for chart segments
val activityColors = mapOf(
    "Social Media" to Color(0xFF4285F4),
    "Gaming" to Color(0xFFDB4437),
    "Watching Videos" to Color(0xFFF4B400),
    "Reading" to Color(0xFF0F9D58),
    "Productivity" to Color(0xFF673AB7),
    "Browse" to Color(0xFFFF9800),
    "Messaging" to Color(0xFF2196F3),
    "Education" to Color(0xFF9C27B0),
    "Other" to Color.Gray
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTimerScreen(navController: NavController?) {
    val viewModel: ScreenTimerViewModel = hiltViewModel()
    val entries by viewModel.entries.collectAsState()
    var selectedRange by remember { mutableStateOf("10 Days") }

    val filteredEntries = when (selectedRange) {
        "10 Days" -> entries.filter { it.dateTime.toLocalDate() >= LocalDate.now().minusDays(10) }
        "Last Month" -> entries.filter { it.dateTime.toLocalDate() >= LocalDate.now().minusMonths(1) }
        else -> entries
    }

    val aggregatedData = viewModel.getAggregatedDataForChart(filteredEntries)
    val trendData = viewModel.getTrendData()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📊 Screen Time Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Entry") },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Screen Time Entry") },
                onClick = { navController?.navigate("log_screen_time") } // Navigates to the entry screen
            )
        }
    ) { padding ->
        if (entries.isEmpty()) {
            Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No data yet.\nTap '+' to add your first entry.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    FilterButtons(selectedRange) { selectedRange = it }
                }
                item {
                    TrendsAndAchievementsCard(trendData = trendData)
                }
                item {
                    MultiSegmentActivityChart(data = aggregatedData)
                }
            }
        }
    }
}

@Composable
fun FilterButtons(selected: String, onSelect: (String) -> Unit) {
    val options = listOf("10 Days", "Last Month", "All")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { label ->
            FilterChip(
                selected = selected == label,
                onClick = { onSelect(label) },
                label = { Text(label) }
            )
        }
    }
}

@Composable
fun TrendsAndAchievementsCard(trendData: TrendData) {
    val trend = trendData.weeklyTrendPercentage
    val isReduction = trend != null && trend < 0

    val cardColors = if (isReduction) {
        CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)) // Light green for achievement
    } else {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    }

    Card(elevation = CardDefaults.cardElevation(4.dp), colors = cardColors) {
        Column(Modifier.padding(16.dp)) {
            if (isReduction) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Achievement",
                        tint = Color(0xFF2E7D32) // Dark green
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Achievement Unlocked!",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    "You've reduced your weekly average by ${"%.1f".format(kotlin.math.abs(trend!!))}%! Keep it up.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Divider(Modifier.padding(vertical = 12.dp))
            } else {
                Text("Screen Time Trends", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TrendStat("Last 7 Days Avg.", "${"%.1f".format(trendData.last7DaysAverage / 60f)}h / day")
                TrendStat("Overall Avg.", "${"%.1f".format(trendData.overallAverage / 60f)}h / day")
            }
        }
    }
}

@Composable
fun RowScope.TrendStat(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MultiSegmentActivityChart(data: List<DailyScreenTime>) {
    val maxTime = data.maxOfOrNull { it.totalMinutes }?.toFloat() ?: 1f

    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text("Daily Activity Breakdown", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))
            Row(Modifier.height(200.dp)) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(data, key = { it.date }) { dailyData ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .width(40.dp)
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp)),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                dailyData.activities.entries.sortedBy { it.key }.forEach { (activity, duration) ->
                                    val color = activityColors[activity] ?: activityColors["Other"]!!
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(((duration / maxTime) * 200).dp)
                                            .background(color)
                                    )
                                }
                            }
                            Text(
                                text = dailyData.date.format(DateTimeFormatter.ofPattern("d MMM")),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                activityColors.forEach { (name, color) ->
                    LegendItem(name, color)
                }
            }
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color = color, shape = CircleShape)
        )
        Text(text = label, fontSize = 12.sp)
    }
}*/
// Compose
/*import android.graphics.Paint
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType


import androidx.hilt.navigation.compose.hiltViewModel

import com.ppp.pegasussociety.Model.ScreenTimeEntry
import com.ppp.pegasussociety.ViewModel.ScreenTimerViewModel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.autofill.ContentDataType.Companion.Date
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

*//*


package com.ppp.pegasussociety.Screens

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ppp.pegasussociety.Model.ScreenTimeEntry
import com.ppp.pegasussociety.ViewModel.ScreenTimerViewModel
*//*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTimerScreen(navController: NavController? = null) {
    val viewModel: ScreenTimerViewModel = hiltViewModel()
    val entries by viewModel.entries.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedRange by remember { mutableStateOf("All") }

    val filteredEntries = when (selectedRange) {
        "Last 7 Days" -> entries.takeLast(7)
        "Last 10 Days" -> entries.takeLast(10)
        "Last Month" -> entries.takeLast(30)
        else -> entries
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("\uD83D\uDCF1 Screen Time Tracker", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFF7FDE2)
                    //MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Entry") },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (entries.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No data yet.\nTap '+' to track screen time.", textAlign = TextAlign.Center)
                }
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    FilterButtons(selectedRange) { selectedRange = it }
                    Spacer(modifier = Modifier.height(8.dp))
                    MultiSegmentBarChart(filteredEntries)
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn(modifier = Modifier.fillMaxHeight()) {
                        items(filteredEntries) { entry ->
                            ScreenTimeCard(entry)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }


            if (showDialog) {
                AddScreenTimeBottomSheet(
                    onDismiss = { showDialog = false },
                    onSave = { date, morning, afternoon, night ->
                        viewModel.addEntry(date, morning, afternoon, night)
                        showDialog = false
                    }
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreenTimeBottomSheet(
    onDismiss: () -> Unit,
    onSave: (String, Int, Int, Int) -> Unit
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now().toString()) }
    var morning by remember { mutableStateOf(30) }
    var afternoon by remember { mutableStateOf(45) }
    var night by remember { mutableStateOf(60) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Add Screen Time", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            DatePickerDialog(
                initialDate = selectedDate,
                onDateChange = { selectedDate = it }
            )

            TimeSlider("Morning", morning) { morning = it }
            TimeSlider("Afternoon", afternoon) { afternoon = it }
            TimeSlider("Night", night) { night = it }

            Button(
                onClick = {
                    onSave(selectedDate, morning, afternoon, night)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Entry")
            }
        }
    }
}

@Composable
fun TimeSlider(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Column {
        Text("$label: $value min")
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 0f..180f,
            steps = 18
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(initialDate: String, onDateChange: (String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Column {
        Text("Selected Date: $initialDate")
        Button(onClick = { showDialog = true }) {
            Text("Pick Date")
        }

        if (showDialog) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val formatted = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .format(Date(millis))
                            onDateChange(formatted)
                        }
                        showDialog = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Composable
fun ScreenTimeCard(entry: ScreenTimeEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("📅 ${entry.date}", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("🌅 Morning: ${entry.morning} min")
                Text("🌞 Afternoon: ${entry.afternoon} min")
                Text("🌃 Night: ${entry.night} min")
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("🕒 Total: ${entry.total()} min", fontWeight = FontWeight.SemiBold)
        }
    }
}

// Make sure to import necessary Compose packages and dependencies
// Updated MultiSegmentBarChart with improvements
*//*
@Composable
fun MultiSegmentBarChart(data: List<ScreenTimeEntry>) {
    val sorted = data.sortedBy { it.date }
    val maxTime = (sorted.maxOfOrNull { it.total() } ?: 1).toFloat()
    val avgTime = if (sorted.isNotEmpty()) sorted.sumOf { it.total() } / sorted.size.toFloat() else 0f

    val morningColor = Color(0xFF90CAF9)
    val afternoonColor = Color(0xFFFFF59D)
    val nightColor = Color(0xFFCE93D8)
    val emptyColor = Color.LightGray

    val chartHeight = 180.dp
    val barWidthDp = 36.dp
    val yAxisLabelCount = 5

    val animatedProgress = remember { androidx.compose.animation.core.Animatable(0f) }
    LaunchedEffect(data) {
        animatedProgress.animateTo(1f, tween(durationMillis = 800, easing = LinearOutSlowInEasing))
    }

    var selectedEntry by remember { mutableStateOf<ScreenTimeEntry?>(null) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {

        Text(
            text = "\uD83D\uDCCA Weekly Screen Time",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LegendItem("\uD83C\uDF05 Morning", morningColor)
            LegendItem("\u2600\uFE0F Afternoon", afternoonColor)
            LegendItem("\uD83C\uDF19 Night", nightColor)
        }

        if (selectedEntry != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("\uD83D\uDCC5 ${selectedEntry!!.date}", fontWeight = FontWeight.Bold)
                    Text("\uD83C\uDF05 Morning: ${selectedEntry!!.morning} min")
                    Text("\u2600\uFE0F Afternoon: ${selectedEntry!!.afternoon} min")
                    Text("\uD83C\uDF19 Night: ${selectedEntry!!.night} min")
                    Text("\uD83D\uDD52 Total: ${selectedEntry!!.total()} min", fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight + 40.dp)
        ) {
            Column(
                modifier = Modifier
                    .width(40.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                for (i in yAxisLabelCount downTo 0) {
                    val value = (maxTime * i / yAxisLabelCount).toInt()
                    Text("${value / 60}h", fontSize = 10.sp, color = Color.Gray)
                }
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sorted) { entry ->
                    val total = entry.total().toFloat()
                    val barRatio = (total / maxTime).coerceAtMost(1f) * animatedProgress.value

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .width(barWidthDp)
                                .height(chartHeight)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable { selectedEntry = if (selectedEntry == entry) null else entry },
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            if (total == 0f) {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(chartHeight * 0.1f)
                                        .background(emptyColor)
                                )
                            } else {
                                Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
                                    Box(
                                        Modifier
                                            .fillMaxWidth()
                                            .height(chartHeight * (entry.night / maxTime) * barRatio)
                                            .background(nightColor)
                                    )
                                    Box(
                                        Modifier
                                            .fillMaxWidth()
                                            .height(chartHeight * (entry.afternoon / maxTime) * barRatio)
                                            .background(afternoonColor)
                                    )
                                    Box(
                                        Modifier
                                            .fillMaxWidth()
                                            .height(chartHeight * (entry.morning / maxTime) * barRatio)
                                            .background(morningColor)
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(entry.date.takeLast(5), fontSize = 10.sp)
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "\uD83D\uDD22 Average Daily Screen Time: ${"%.1f".format(avgTime / 60f)} hours",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray,
            modifier = Modifier.padding(top = 8.dp)
        )
        LinearProgressIndicator(
            progress = (avgTime / maxTime).coerceIn(0f, 1f),
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            color = MaterialTheme.colorScheme.primary
        )
    }
}*//*

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color = color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}


@Composable
fun FilterButtons(selected: String, onSelect: (String) -> Unit) {
    val options = listOf("10 Days", "Last Month", "All")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        options.forEach { label ->
            OutlinedButton(
                onClick = { onSelect(label) },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selected == label) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent
                )
            ) {
                Text(label)
            }
        }
    }
}

@Composable
fun MultiSegmentBarChart(data: List<ScreenTimeEntry>) {
    val sorted = data.sortedBy { it.date }
    val maxTime = (sorted.maxOfOrNull { it.total() } ?: 1).toFloat()
    val avgTime = sorted.sumOf { it.total() } / sorted.size.toFloat()

    val morningColor = Color(0xFF90CAF9)
    val afternoonColor = Color(0xFFFFF59D)
    val nightColor = Color(0xFFCE93D8)

    val labelTextSizePx = with(LocalDensity.current) { 10.sp.toPx() }
    val chartHeight = 180.dp
    val barWidthDp = 32.dp
    val yAxisLabelCount = 5

    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    val animatedProgress = remember { androidx.compose.animation.core.Animatable(0f) }
    LaunchedEffect(data) {
        animatedProgress.animateTo(1f, animationSpec = tween(durationMillis = 1000))
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Weekly Screen Time",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(modifier = Modifier.padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LegendItem("Morning", morningColor)
            LegendItem("Afternoon", afternoonColor)
            LegendItem("Night", nightColor)
        }

        val canvasHeightPx = with(LocalDensity.current) { chartHeight.toPx() }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight + 40.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(40.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                for (i in yAxisLabelCount downTo 0) {
                    val labelTime = (maxTime * i / yAxisLabelCount)
                    Text(
                        text = String.format("%.1f h", labelTime / 60f),
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }

            LazyRow(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(sorted) { index, entry ->
                    Box(
                        modifier = Modifier
                            .width(barWidthDp)
                            .fillMaxHeight()
                            .padding(horizontal = 4.dp)
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    selectedIndex = if (selectedIndex == index) null else index
                                }
                            }
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val totalHeight = size.height
                            val barWidth = size.width
                            var yOffset = totalHeight

                            fun drawSegment(height: Float, color: Color) {
                                yOffset -= height
                                drawRect(
                                    color = color,
                                    topLeft = Offset(0f, yOffset),
                                    size = Size(barWidth, height)
                                )
                            }

                            val morningHeight = (entry.morning / maxTime) * totalHeight * animatedProgress.value
                            val afternoonHeight = (entry.afternoon / maxTime) * totalHeight * animatedProgress.value
                            val nightHeight = (entry.night / maxTime) * totalHeight * animatedProgress.value

                            drawSegment(nightHeight, nightColor)
                            drawSegment(afternoonHeight, afternoonColor)
                            drawSegment(morningHeight, morningColor)

                            val avgY = totalHeight - (avgTime / maxTime) * totalHeight
                            drawLine(
                                color = Color.Red,
                                start = Offset(0f, avgY),
                                end = Offset(barWidth, avgY),
                                strokeWidth = 2.dp.toPx()
                            )

                            if (selectedIndex == index) {
                                val text = String.format("%.1f h", entry.total() / 60f)
                                drawContext.canvas.nativeCanvas.drawText(
                                    text,
                                    barWidth / 2f,
                                    yOffset - 12.dp.toPx(),
                                    Paint().apply {
                                        textSize = 12.sp.toPx()
                                        color = android.graphics.Color.BLACK
                                        textAlign = Paint.Align.CENTER
                                    }
                                )
                            }

                            drawContext.canvas.nativeCanvas.drawText(
                                entry.date.takeLast(5),
                                barWidth / 4f,
                                totalHeight + 20f,
                                Paint().apply {
                                    textSize = labelTextSizePx
                                    color = android.graphics.Color.DKGRAY
                                    textAlign = Paint.Align.LEFT
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}*/




/*

@Composable
fun MultiSegmentBarChart(data: List<ScreenTimeEntry>) {
    val sorted = data.sortedBy { it.date }
    val maxTime = (sorted.maxOfOrNull { it.total() } ?: 1).toFloat()

    val morningColor = Color(0xFF90CAF9)
    val afternoonColor = Color(0xFFFFF59D)
    val nightColor = Color(0xFFCE93D8)

    val labelTextSizePx = with(LocalDensity.current) { 10.sp.toPx() }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(horizontal = 16.dp)
    ) {
        val barWidth = size.width / (sorted.size * 1.5f)

        sorted.forEachIndexed { index, entry ->

            val x = index * barWidth * 1.5f + barWidth * 0.25f
            var yOffset = size.height

            fun drawSegment(height: Float, color: Color){
                yOffset -= height
                drawRect(
                    color = color,
                    topLeft = Offset(x, yOffset),
                    size = Size(barWidth, height)
                )
            }

            val morningHeight = (entry.morning / maxTime) * size.height
            val afternoonHeight = (entry.afternoon / maxTime) * size.height
            val nightHeight = (entry.night / maxTime) * size.height

            drawSegment(nightHeight, nightColor)
            drawSegment(afternoonHeight, afternoonColor)
            drawSegment(morningHeight, morningColor)

            // Draw date label below bar
            drawContext.canvas.nativeCanvas.drawText(
                entry.date.takeLast(5),
                x,
                size.height + 20f,
                Paint().apply {
                    textSize = labelTextSizePx
                    color = android.graphics.Color.DKGRAY
                    textAlign = Paint.Align.LEFT
                }
            )
        }
    }
}
*/

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTimerScreen() {
    val viewModel: ScreenTimerViewModel = hiltViewModel()
    val entries = viewModel.entries
    val avg = viewModel.averageTime()

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Screen Time Tracker") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Entry")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            StatsSummaryCard(entries)

            Spacer(Modifier.height(16.dp))

            if (entries.isNotEmpty()) {
                BarChartView(entries)
            } else {
                Text(
                    "No data yet. Add today's screen time.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (showDialog) {
            AddScreenTimeDialog(
                onDismiss = { showDialog = false },
                onSave = { date, time ->
                    viewModel.addEntry(date, time)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun StatsSummaryCard(data: List<ScreenTimeEntry>) {
    if (data.isEmpty()) return

    val avg = data.sumOf { it.minutes } / data.size
    val max = data.maxOf { it.minutes }
    val min = data.minOf { it.minutes }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Average", style = MaterialTheme.typography.labelMedium)
                Text("$avg min", style = MaterialTheme.typography.titleSmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Max", style = MaterialTheme.typography.labelMedium)
                Text("$max min", style = MaterialTheme.typography.titleSmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Min", style = MaterialTheme.typography.labelMedium)
                Text("$min min", style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}

@Composable
fun AddScreenTimeDialog(
    onDismiss: () -> Unit,
    onSave: (String, Int) -> Unit
) {
    var date by remember { mutableStateOf("") }
    var minutes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    if (date.isNotBlank() && minutes.toIntOrNull() != null) {
                        onSave(date, minutes.toInt())
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Add Screen Time") },
        text = {
            Column {
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (yyyy-mm-dd)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = minutes,
                    onValueChange = { minutes = it },
                    label = { Text("Minutes") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
@Composable
fun BarChartView(data: List<ScreenTimeEntry>) {
    val sorted = data.sortedBy { it.date }
    val maxTime = (sorted.maxOfOrNull { it.minutes } ?: 1).toFloat()
    val avgTime = sorted.sumOf { it.minutes }.toFloat() / sorted.size

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(260.dp)
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        val barWidth = size.width / (sorted.size * 1.5f)

        sorted.forEachIndexed { index, item ->
            val barHeight = (item.minutes / maxTime) * size.height * 0.8f
            val x = index * barWidth * 1.5f + barWidth * 0.25f
            val y = size.height - barHeight

            // Draw bar
            drawRect(
                color = if (item.minutes.toFloat() > avgTime)
                    Color.Red
                else
                    Color.Green,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight)
            )

            // Draw value above bar
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "${item.minutes}",
                    x + barWidth / 4,
                    y - 12f,
                    android.graphics.Paint().apply {
                        textSize = 32f
                        color = android.graphics.Color.BLACK
                        textAlign = android.graphics.Paint.Align.LEFT
                    }
                )
            }

            // X-axis label
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    item.date.takeLast(5),
                    x,
                    size.height + 20f,
                    android.graphics.Paint().apply {
                        textSize = 28f
                        color = android.graphics.Color.DKGRAY
                        textAlign = android.graphics.Paint.Align.LEFT
                    }
                )
            }
        }

        // Draw average line
        val avgY = size.height - (avgTime / maxTime) * size.height * 0.8f
        drawLine(
            color = Color.Gray,
            start = Offset(0f, avgY),
            end = Offset(size.width, avgY),
            strokeWidth = 2f
        )
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                "Avg ${avgTime.toInt()}",
                size.width - 100f,
                avgY - 8f,
                android.graphics.Paint().apply {
                    textSize = 28f
                    color = android.graphics.Color.GRAY
                    textAlign = android.graphics.Paint.Align.LEFT
                }
            )
        }
    }
}

@Composable
fun MultiSegmentBarChart(data: List<ScreenTimeEntry>) {
    val sorted = data.sortedBy { it.date }
    val maxTime = (sorted.maxOfOrNull { it.total() } ?: 1).toFloat()

    // Colors for segments
    val morningColor = Color(0xFF90CAF9)     // Light Blue
    val afternoonColor = Color(0xFFFFF59D)   // Yellow
    val nightColor = Color(0xFFCE93D8)       // Purple

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(horizontal = 16.dp)
    ) {
        val barWidth = size.width / (sorted.size * 1.5f)

        sorted.forEachIndexed { index, entry ->
            val x = index * barWidth * 1.5f + barWidth * 0.25f

            var yOffset = size.height

            fun drawSegment(height: Float, color: Color) {
                yOffset -= height
                drawRect(
                    color = color,
                    topLeft = Offset(x, yOffset),
                    size = Size(barWidth, height)
                )
            }

            val total = entry.total().toFloat()
            if (total > 0) {
                val morningHeight = (entry.morning / maxTime) * size.height
                val afternoonHeight = (entry.afternoon / maxTime) * size.height
                val nightHeight = (entry.night / maxTime) * size.height

                drawSegment(nightHeight, nightColor)
                drawSegment(afternoonHeight, afternoonColor)
                drawSegment(morningHeight, morningColor)
            }

            // X-axis label
            drawContext.canvas.nativeCanvas.drawText(
                entry.date.takeLast(5), // Show MM-DD
                x,
                size.height + 20f,
                android.graphics.Paint().apply {
                    textSize = 28f
                    color = android.graphics.Color.DKGRAY
                }
            )
        }
    }
}*/


