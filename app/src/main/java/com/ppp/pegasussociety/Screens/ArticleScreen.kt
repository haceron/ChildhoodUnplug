package com.ppp.pegasussociety.Screens

// Hotstar-style Homepage UI Clone for Jetpack Compose with Auto-Scrolling Banner and Read Article Button

// Hearty-style HomeScreen Clone UI in Jetpack Compose with Auto-Scrolling Banner and Dynamic Background Color

// Hearty-style HomeScreen Clone UI in Jetpack Compose with Auto-Scrolling Banner and Dynamic Background Color

/*import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch*/

// Hearty-style HomeScreen Clone UI in Jetpack Compose with Swipeable Auto-Scrolling Banner and Dynamic Background Color

/*import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import coil.compose.AsyncImage

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.draw.clip

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

import androidx.compose.foundation.clickable
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ppp.pegasussociety.BottomNavItem
import com.ppp.pegasussociety.R
import com.ppp.pegasussociety.ViewModel.BannerViewModel*/

// Data classes for a more robust, data-driven UI
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.navigation.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.ppp.pegasussociety.Screen
import com.ppp.pegasussociety.ViewModel.BannerViewModel
import kotlinx.coroutines.delay
import retrofit2.http.Url

// --- DATA CLASSES ---
//data class ActivityBannerItem(val title: String, val imageUrl: String, val bgColor: Color, val content: String)

//data class Category(val name: String, val icon: ImageVector, val color: Color)

// --- SAMPLE DATA ---
/*
val sampleCategories = listOf(
    Category("Science", Icons.Default.Science, Color(0xFFE0F7FA)),
    Category("Games", Icons.Default.Games, Color(0xFFFCE4EC)),
    Category("Handmade", Icons.Default.ContentCut, Color(0xFFFFF9C4)),
    Category("Cooking", Icons.Default.OutdoorGrill, Color(0xFFFFE0B2)),
    Category("Outdoors", Icons.Default.Forest, Color(0xFFDCEDC8)),
    Category("Stories", Icons.Default.MenuBook, Color(0xFFF0E68C))
)
*/
data class ActivityCardItem1(val title: String, val imageUrl: Int, val content: String)

data class ActivityBannerItem(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val bgColor: Color,
    val content: String,
    val attachmentUrl: String?
)


data class ActivityCardItem(
    val title: String,
    val imageUrl: String,
    val content: String
    //  val tagInterest: List<String>
)

data class Category(
    val name: String,       // Display name
    val apiKey: String,     // API category name (for URL)
    val icon: ImageVector,
    val color: Color
)

// --- SAMPLE DATA ---
val sampleCategories = listOf(
    Category("Arts & Crafts", "Arts%20%26%20Crafts", Icons.Default.ContentCut, Color(0xFFFFF9C4)),
    Category("Science", "Science", Icons.Default.Science, Color(0xFFE0F7FA)),
    Category("Reading", "Reading", Icons.Default.MenuBook, Color(0xFFF0E68C)),
    Category("Music", "Music", Icons.Default.MusicNote, Color(0xFFFCE4EC)),
    Category("Sports", "Sports", Icons.Default.SportsSoccer, Color(0xFFFFE0B2)),
    Category("Technology", "Technology", Icons.Default.Memory, Color(0xFFDCEDC8)),
    Category("Mythology", "Mythology", Icons.Default.AutoStories, Color(0xFFD1C4E9))
)


@Composable
fun HeartyHomeScreen(
    onBackgroundColorChange: (Color) -> Unit,
    navController: NavController
) {
    val viewModel: BannerViewModel = hiltViewModel()
    val bannerItems by viewModel.bannerItemsState.collectAsState()
    val latestItems by viewModel.latestItemsState.collectAsState()
    val popularItems by viewModel.popularItemsState.collectAsState()

    val pagerState = rememberPagerState(pageCount = { bannerItems.size })

    LaunchedEffect(pagerState.currentPage, bannerItems) {
        if (bannerItems.isNotEmpty()) {
            onBackgroundColorChange(bannerItems[pagerState.currentPage].bgColor)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            if (bannerItems.isNotEmpty()) {
                AutoScrollingBanner(
                    items = bannerItems,
                    pagerState = pagerState,
                    navController = navController
                )
            }
        }
        item { ClickableSearchBar(modifier = Modifier.padding(horizontal = 16.dp)) }
        item {
            Box(modifier = Modifier.padding(top = 8.dp)) {
                CategoryRow(
                    categories = sampleCategories,
                    navController = navController
                )
            }
        }

        if (latestItems.isNotEmpty()) {
            item {
                ActivitySection(
                    title = "Latest",
                    activities = latestItems,
                    navController = navController
                )
            }
        }

        if (popularItems.isNotEmpty()) {
            item {
                ActivitySection(
                    title = "Popular",
                    activities = popularItems,
                    navController = navController
                )
            }
        }
    }
}
@Composable
fun AutoScrollingBanner(
    items: List<ActivityBannerItem>,
    pagerState: PagerState,
    navController: NavController
) {
    LaunchedEffect(pagerState) {
        while (true) {
            delay(4000)
            val nextPage = (pagerState.currentPage + 1) % items.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp
        ) { page ->
            val item = items[page]
            HeroBanner(
                title = item.title,
                subtitle = "Make your own Audio Stories with pictures!",
                imageUrl = item.imageUrl,
                content = item.content,
                onReadMoreClick = {
                    navController.navigate(Screen.Content.createRoute(item.id))
                },
                onBannerClick = {
                    navController.navigate(Screen.Content.createRoute(item.id))
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        PagerIndicator(pagerState = pagerState, pageCount = items.size)
    }
}

@Composable
fun HeroBanner(
    title: String,
    subtitle: String,
    imageUrl: String,
    content: String,
    onReadMoreClick: () -> Unit,
    onBannerClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable { onBannerClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier.fillMaxSize().background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                        startY = 300f
                    )
                )
            )
            Column(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(subtitle, fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f))
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onReadMoreClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Read Article", color = Color.Black, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun ActivitySection(
    title: String,
    activities: List<ActivityBannerItem>,
    navController: NavController
) {
    if (activities.isNotEmpty()) {
        Column {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(activities) { activity ->
                    ActivityCard(item = activity) {
                        navController.navigate(Screen.Content.createRoute(activity.id))
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
            .size(width = 160.dp, height = 200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            AsyncImage(
                model = activity.imageUrl,
                contentDescription = activity.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Column(Modifier.padding(12.dp)) {
                Text(
                    activity.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1
                )
            }
        }
    }
}


@Composable
fun ActivityCard(item: ActivityBannerItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(width = 160.dp, height = 200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )
            Column(Modifier.padding(12.dp)) {
                Text(item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
            }
        }
    }
}

// ------------------------- PAGER INDICATOR -------------------------
@Composable
fun PagerIndicator(pagerState: PagerState, pageCount: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration)
                MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            Box(
                modifier = Modifier.padding(2.dp).clip(CircleShape).background(color).size(10.dp)
            )
        }
    }
}

// ------------------------- SEARCH BAR -------------------------
@Composable
fun ClickableSearchBar(modifier: Modifier = Modifier, onSearchClick: () -> Unit = {}) {
    Surface(
        modifier = modifier.fillMaxWidth().height(56.dp).clickable { onSearchClick() },
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 4.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.width(12.dp))
            Text("Search activities or stories...", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.Tune, contentDescription = "Filter", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// ------------------------- CATEGORY ROW -------------------------
@Composable
fun CategoryRow(categories: List<Category>, navController: NavController) {
    Column {
        Text(
            "Explore by Category",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { category ->
                CategoryChip(
                    category = category,
                    onClick = {
                        navController.navigate("category/${category.apiKey}/${category.name}")
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryChip(category: Category, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(category.color),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                category.icon,
                contentDescription = category.name,
                tint = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(category.name, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}


/*
@Composable
fun CategoryChip(category: Category, onClick: () -> Unit = {}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Box(
            modifier = Modifier.size(72.dp).clip(CircleShape).background(category.color),
            contentAlignment = Alignment.Center
        ) {
            Icon(category.icon, contentDescription = category.name, tint = Color.Black.copy(alpha = 0.7f), modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(category.name, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}*/

// ------------------------- BOTTOM NAV -------------------------
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Activity : BottomNavItem(Screen.Home.route, Icons.Default.Home, "Home")
    object Timer : BottomNavItem(Screen.Timer.route, Icons.Default.Timer, "Timer")
    object Profile : BottomNavItem(Screen.Profile.route, Icons.Default.Person, "Profile")
}

@Composable
fun HeartyBottomNavigationBar(navController: NavController) {
    val items = listOf(BottomNavItem.Activity, BottomNavItem.Timer, BottomNavItem.Profile)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}



/*  --------------11-08     10.22

data class ActivityBannerItem(
    val title: String,
    val imageUrl: String,
    val bgColor: Color,
    val content: String
)



data class Category(val name: String, val icon: ImageVector, val color: Color)

data class ActivityCardItem(
    val title: String,
    val imageUrl: String, // ✅ Now from API, not drawable Int
    val tag: String
)

// --- SAMPLE CATEGORY DATA ---
val sampleCategories = listOf(
    Category("Science", Icons.Default.Science, Color(0xFFE0F7FA)),
    Category("Games", Icons.Default.Games, Color(0xFFFCE4EC)),
    Category("Handmade", Icons.Default.ContentCut, Color(0xFFFFF9C4)),
    Category("Cooking", Icons.Default.OutdoorGrill, Color(0xFFFFE0B2)),
    Category("Outdoors", Icons.Default.Forest, Color(0xFFDCEDC8)),
    Category("Stories", Icons.Default.MenuBook, Color(0xFFF0E68C))
)

val samplePopular = listOf(
    ActivityCardItem("Baking Cookies", "https://via.placeholder.com/300", "Cooking"),
    ActivityCardItem("DIY Puppets", "https://via.placeholder.com/300", "Handmade"),
    ActivityCardItem("Stargazing", "https://via.placeholder.com/300", "Science")
)


*/
/*
val sampleIdeas = listOf(
    ActivityCardItem("Clay Modeling", R.drawable.banner1, "Handmade"),
    ActivityCardItem("Volcano Experiment", R.drawable.banner2, "Science"),
    ActivityCardItem("Story Dice", R.drawable.banner3, "Games"),
    ActivityCardItem("Leaf Painting", R.drawable.banner4, "Outdoors")
)

val samplePopular = listOf(
    ActivityCardItem("Baking Cookies", R.drawable.banner1, "Cooking"),
    ActivityCardItem("DIY Puppets", R.drawable.banner2, "Handmade"),
    ActivityCardItem("Stargazing", R.drawable.banner3, "Science")
)
*//*


// --- MAIN SCREEN ---
@Composable
fun HeartyHomeScreen(onBackgroundColorChange: (Color) -> Unit) {
    val viewModel: BannerViewModel = hiltViewModel()
    val latestItems by viewModel.latestItemsState.collectAsState()
    val bannerItems by viewModel.bannerItemsState.collectAsState()

    val pagerState = remember { PagerState(pageCount = { bannerItems.size }) }

    LaunchedEffect(pagerState.currentPage, bannerItems) {
        if (bannerItems.isNotEmpty()) {
            onBackgroundColorChange(bannerItems[pagerState.currentPage].bgColor)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            if (bannerItems.isNotEmpty()) {
                AutoScrollingBanner(items = bannerItems, pagerState = pagerState)
            }
        }

        item { ClickableSearchBar(modifier = Modifier.padding(horizontal = 16.dp)) }
        item { CategoryRow(categories = sampleCategories, modifier = Modifier.padding(top = 8.dp)) }

        if (latestItems.isNotEmpty()) {
            item { ActivitySection(title = "Latest", activities = latestItems) }
        }

       // item { ActivitySection(title = "Popular Activities", activities = samplePopular) }
    }
}

// --- BANNER ---
@Composable
fun AutoScrollingBanner(items: List<ActivityBannerItem>, pagerState: PagerState) {
    LaunchedEffect(pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            while (true) {
                delay(4000)
                val nextPage = (pagerState.currentPage + 1) % items.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp
        ) { page ->
            val item = items[page]
            HeroBanner(
                title = item.title,
                subtitle = "Make your own Audio Stories with pictures!",
                imageUrl = item.imageUrl,
                content = item.content
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        PagerIndicator(pagerState = pagerState, pageCount = items.size)
    }
}

// --- HERO BANNER ---
@Composable
fun HeroBanner(title: String, subtitle: String, imageUrl: String, content: String) {
    var showContent by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier.fillMaxSize().background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                        startY = 300f
                    )
                )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(subtitle, fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f))
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { showContent = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Read Article", color = Color.Black, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }

    if (showContent) {
        Dialog(onDismissRequest = { showContent = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier.fillMaxSize()
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = title,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { showContent = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }
                    AndroidView(
                        factory = { context ->
                            WebView(context).apply {
                                settings.javaScriptEnabled = true
                                loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// --- ACTIVITY SECTION --- /////
@Composable
fun ActivitySection(title: String, activities: List<ActivityCardItem>) {
    if (activities.isNotEmpty()) {
        Column {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(activities) { activity -> ActivityCard(item = activity) }
            }
        }
    }
}

// --- ACTIVITY CARD ---
@Composable
fun ActivityCard(item: ActivityCardItem, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .size(width = 160.dp, height = 200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Column(Modifier.padding(12.dp)) {
                Text(item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
                Spacer(Modifier.height(4.dp))
                Text(item.tag, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}


*/
/*

@Composable
fun HeroBanner(title: String, subtitle: String, imageUrl: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth().height(220.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier.fillMaxSize().background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                        startY = 300f
                    )
                )
            )
            Column(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(subtitle, fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f))
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = {

                }, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                    Text("Read Article", color = Color.Black, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
*//*


@Composable
fun PagerIndicator(pagerState: PagerState, pageCount: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration)
                MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(10.dp)
            )
        }
    }
}

@Composable
fun ClickableSearchBar(modifier: Modifier = Modifier, onSearchClick: () -> Unit = {}) {
    Surface(
        modifier = modifier.fillMaxWidth().height(56.dp).clickable { onSearchClick() },
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 4.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.width(12.dp))
            Text("Search activities or stories...", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.Tune, contentDescription = "Filter", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun CategoryRow(categories: List<Category>, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text("Explore by Category", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(start = 16.dp, bottom = 12.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(categories) { category -> CategoryChip(category = category) }
        }
    }
}

@Composable
fun CategoryChip(category: Category, onClick: () -> Unit = {}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Box(
            modifier = Modifier.size(72.dp).clip(CircleShape).background(category.color),
            contentAlignment = Alignment.Center
        ) {
            Icon(category.icon, contentDescription = category.name, tint = Color.Black.copy(alpha = 0.7f), modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(category.name, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}
*/
/*
@Composable
fun ActivitySection(title: String, activities: List<ActivityCardItem>) {
    Column {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(start = 16.dp, bottom = 12.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(activities) { activity -> ActivityCard(item = activity) }
        }
    }
}*//*


@Composable
fun ActivitySection1(title: String, activities: List<ActivityCardItem1>) {
    if (activities.isNotEmpty()) { // ✅ Avoid empty LazyRow rendering
        Column {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(activities) { activity ->
                    ActivityCard(item = activity)
                }
            }
        }
    }
}


@Composable
fun ActivityCard1(item: ActivityCardItem1, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .size(width = 160.dp, height = 200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Column(Modifier.padding(12.dp)) {
                Text(
                    item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    item.content,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@Composable
fun ActivityCard(item: ActivityCardItem, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier.size(width = 160.dp, height = 200.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = item.imageUrl),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )
            Column(Modifier.padding(12.dp)) {
                Text(item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
                Spacer(Modifier.height(4.dp))
                Text(item.tag, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Activity : BottomNavItem("activity", Icons.Default.Home, "Home")
    object Timer : BottomNavItem("timer", Icons.Default.Timer, "Timer")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
}

@Composable
fun HeartyBottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Activity,
        BottomNavItem.Timer,
        BottomNavItem.Profile
    )
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}


@Composable
fun BottomNavigationBar() {
    NavigationBar(containerColor = Color(0xFFB4DB6F)) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.VideoLibrary, contentDescription = null) },
            label = { Text("Activity") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.GridView, contentDescription = null) },
            label = { Text("Library") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.AccessTimeFilled, contentDescription = null) },
            label = { Text("Screen Timer") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Face, contentDescription = null) },
            label = { Text("Profile") }
        )
    }
}
*/




//@OptIn(ExperimentalPagerApi::class)
/*
@Composable
fun HeartyHomeScreen(navController: NavController? = null) {
    val bannerItems = listOf(
        ActivityItem("FairyTeller", R.drawable.banner1, Color(0xFFB4DB6F)),
        ActivityItem("DinoPark", R.drawable.banner2, Color(0xFFB4E1A1)),
        ActivityItem("CraftZone", R.drawable.banner3, Color(0xFFF6E49C))
    )

    val pagerState = rememberPagerState()
    val backgroundColor by animateColorAsState(
        targetValue = bannerItems[pagerState.currentPage].bgColor,
        label = "Background Color"
    )

    Scaffold(
        bottomBar = { BottomNavigationBar() },
        containerColor = backgroundColor
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(bottom = 64.dp),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            item {
                AutoScrollingBanner(bannerItems, pagerState)
            }

            item {
                SearchBar(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }

            item {
                Text(
                    text = "Indoor",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 12.dp),
                    color = Color.White
                )
            }

            item {
                CategoryRow(listOf("Science", "Games", "Handmade", "Cooking", "Outdoors", "Stories"))
            }

            items(3) {
                CategoryCardSection(title = "More Ideas")
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AutoScrollingBanner(items: List<ActivityItem>, pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            val nextPage = (pagerState.currentPage + 1) % items.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column {
        HorizontalPager(count = items.size, state = pagerState, modifier = Modifier.height(260.dp)) { page ->
            val item = items[page]
            HeroBanner(
                title = item.title,
                subtitle = "Make your own Audio Stories with pictures!",
                imageRes = item.imageRes
            )
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = Color.White,
            inactiveColor = Color.LightGray,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(8.dp)
        )
    }
}

@Composable
fun HeroBanner(title: String, subtitle: String, imageRes: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .align(Alignment.BottomStart)
        ) {
            Text(title, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(subtitle, fontSize = 14.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { */
/* Navigate to Article *//*
 },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Read Article", color = Color.Black)
            }
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Search, contentDescription = "Search", modifier = Modifier.padding(12.dp), tint = Color.Gray)
        Text(text = "Search...", color = Color.Gray)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.Tune, contentDescription = "Filter", modifier = Modifier.padding(12.dp), tint = Color.Gray)
    }
}

@Composable
fun CategoryRow(categories: List<String>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            CategoryChip(category)
        }
    }
}

@Composable
fun CategoryChip(name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFDEF1A3)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Star, contentDescription = name, tint = Color.Black)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(name, fontSize = 12.sp, color = Color.White)
    }
}

@Composable
fun CategoryCardSection(title: String) {
    Column(modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        LazyRow(contentPadding = PaddingValues(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(5) {
                Box(
                    modifier = Modifier
                        .size(width = 140.dp, height = 160.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                ) {
                    Text("Activity $it", modifier = Modifier.align(Alignment.Center), color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(containerColor = Color(0xFFB4DB6F)) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.VideoLibrary, contentDescription = null) },
            label = { Text("Activity") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.GridView, contentDescription = null) },
            label = { Text("Library") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.AccessTimeFilled, contentDescription = null) },
            label = { Text("Screen Timer") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Face, contentDescription = null) },
            label = { Text("Profile") }
        )
    }
}

// Data class for banner items
data class ActivityItem(val title: String, val imageRes: Int, val bgColor: Color)
*/

/*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HeartyHomeScreen() {
    val bannerItems = listOf(
        ActivityItem("FairyTeller", R.drawable.banner1, Color(0xFFB4DB6F)),
        ActivityItem("DinoPark", R.drawable.banner2, Color(0xFFB4E1A1)),
        ActivityItem("CraftZone", R.drawable.banner3, Color(0xFFF6E49C))
    )

    var backgroundColor by remember { mutableStateOf(bannerItems[0].bgColor) }

    Scaffold(
        bottomBar = { BottomNavigationBar() },
        containerColor = backgroundColor
    ) { padding ->

        LazyColumn(
            contentPadding = PaddingValues(bottom = 64.dp),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            item {
                AutoScrollingBanner(bannerItems) { selectedColor ->
                    backgroundColor = selectedColor
                }
            }

            item {
                SearchBar(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }

            item {
                Text(
                    text = "Indoor",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 12.dp),
                    color = Color.White
                )
            }

            item {
                CategoryRow(listOf("Science", "Games", "Handmade", "Cooking", "Outdoors", "Stories"))
            }

            items(3) {
                CategoryCardSection(title = "More Ideas")
            }
        }
    }
}

@Composable
fun AutoScrollingBanner(items: List<ActivityItem>, onBannerChange: (Color) -> Unit) {
    var index by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            index = (index + 1) % items.size
            onBannerChange(items[index].bgColor)
        }
    }

    val currentItem = items[index]

    HeroBanner(
        title = currentItem.title,
        subtitle = "Make your own Audio Stories with pictures!",
        imageRes = currentItem.imageRes
    )
}

@Composable
fun HeroBanner(title: String, subtitle: String, imageRes: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(240.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .align(Alignment.BottomStart)
        ) {
            Text(title, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(subtitle, fontSize = 14.sp, color = Color.White)
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Search, contentDescription = "Search", modifier = Modifier.padding(12.dp), tint = Color.Gray)
        Text(text = "Search...", color = Color.Gray)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.Tune, contentDescription = "Filter", modifier = Modifier.padding(12.dp), tint = Color.Gray)
    }
}

@Composable
fun CategoryRow(categories: List<String>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            CategoryChip(category)
        }
    }
}

@Composable
fun CategoryChip(name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFDEF1A3)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Star, contentDescription = name, tint = Color.Black)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(name, fontSize = 12.sp, color = Color.White)
    }
}

@Composable
fun CategoryCardSection(title: String) {
    Column(modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        LazyRow(contentPadding = PaddingValues(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(5) {
                Box(
                    modifier = Modifier
                        .size(width = 140.dp, height = 160.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                ) {
                    Text("Activity $it", modifier = Modifier.align(Alignment.Center), color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(containerColor = Color(0xFFB4DB6F)) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.VideoLibrary, contentDescription = null) },
            label = { Text("Browse") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.GridView, contentDescription = null) },
            label = { Text("Library") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Search, contentDescription = null) },
            label = { Text("Search") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Face, contentDescription = null) },
            label = { Text("Profile") }
        )
    }
}

// Data class for banner items
data class ActivityItem(val title: String, val imageRes: Int, val bgColor: Color)
*/


/*import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Data Class
data class BannerItem(val title: String, val imageRes: Int)

// Sample Data
val bannerItems = listOf(
    BannerItem("Avengers: Endgame", R.drawable.banner1),
    BannerItem("Loki Season 2", R.drawable.banner2),
    BannerItem("Guardians Vol.3", R.drawable.banner3),
    BannerItem("The Mandalorian", R.drawable.banner4)
)

@Composable
fun ArticleScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item { AutoScrollingBanner(items = bannerItems) }
        item { CategoryRow(title = "Recommended For You", items = bannerItems) }
        item { CategoryRow(title = "Latest Releases", items = bannerItems) }
        item { CategoryRow(title = "Top Picks", items = bannerItems) }
        item { CategoryRow(title = "Marvel Universe", items = bannerItems) }
    }
}

@Composable
fun AutoScrollingBanner(items: List<BannerItem>) {
    val state = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            val nextIndex = (state.firstVisibleItemIndex + 1) % items.size
            state.animateScrollToItem(nextIndex)
        }
    }

    LazyRow(
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(items) { item ->
            BannerHeroCard(item)
        }
    }
}

@Composable
fun BannerHeroCard(item: BannerItem) {
    Box(
        modifier = Modifier
            .width(300.dp)
            .height(350.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Image(
            painter = painterResource(id = item.imageRes),
            contentDescription = item.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                        startY = 300f
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            Text(
                text = item.title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Button(onClick = { *//* TODO: Handle Read Article *//* }) {
                Text("Read Article")
            }
        }
    }
}

@Composable
fun CategoryRow(title: String, items: List<BannerItem>) {
    Column(modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)) {
        Text(
            text = title,
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { item ->
                BannerCard(item)
            }
        }
    }
}

@Composable
fun BannerCard(item: BannerItem) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Image(
            painter = painterResource(id = item.imageRes),
            contentDescription = item.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                        startY = 300f
                    )
                )
        )

        Text(
            text = item.title,
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
        )
    }
}*/


