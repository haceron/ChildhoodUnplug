package com.ppp.pegasussociety.Screens

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.SentimentDissatisfied
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ppp.pegasussociety.ViewModel.BannerViewModel

// Assuming ActivityBannerItem data class looks something like this
// data class ActivityBannerItem(val id: String, val title: String, val imageUrl: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    categoryApiKey: String,
    categoryDisplayName: String,
    navController: NavController,
    viewModel: BannerViewModel = hiltViewModel()
) {
    // Assuming your ViewModel exposes an isLoading state
    val isLoading by viewModel.isLoading.collectAsState()
    val activities by viewModel.categoryActivitiesState.collectAsState()

    // Trigger the fetch when screen is opened
    LaunchedEffect(categoryApiKey) {
        viewModel.fetchActivitiesByCategory(categoryApiKey)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryDisplayName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        when {
            // 1. Shimmer loading state
            isLoading -> {
                LoadingGrid(modifier = Modifier.padding(padding))
            }
            // 2. Empty state after loading
            activities.isEmpty() -> {
                EmptyState(
                    modifier = Modifier.padding(padding),
                    categoryName = categoryDisplayName
                )
            }
            // 3. Content state
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp),
                    modifier = Modifier.padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(activities, key = { it.id }) { activity ->
                        ActivityCard1(activity = activity) {
                            navController.navigate("article/${activity.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityCard1(activity: ActivityBannerItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.height(220.dp)) {
            AsyncImage(
                model = activity.imageUrl,
                contentDescription = activity.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Gradient overlay for text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 300f
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = activity.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun LoadingGrid(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(6) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.height(220.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmerEffect()
                )
            }
        }
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier, categoryName: String) {
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
                imageVector = Icons.Outlined.SentimentDissatisfied,
                contentDescription = "Empty",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = "No Activities Found",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "There are currently no activities available in the \"$categoryName\" category.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}


fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val alpha by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ), label = "shimmerAlpha"
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.LightGray.copy(alpha = alpha),
                Color.LightGray.copy(alpha = 0.2f),
                Color.LightGray.copy(alpha = alpha)
            ),
            start = Offset.Zero,
            end = Offset(x = 100f, y = 100f)
        )
    )
}
