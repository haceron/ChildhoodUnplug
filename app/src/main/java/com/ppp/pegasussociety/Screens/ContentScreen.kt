package com.ppp.pegasussociety.Screens

import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ppp.pegasussociety.ViewModel.BannerViewModel


import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

// Define the custom orange color from the design
val MythologyOrange = Color(0xFFE67E22)

val AccentColor = Color(0xFF00838F) // A deep teal
val PageBackground = Color(0xFFF7F9FA) // A very light gray, almost white
val TextPrimary = Color(0xFF212121)    // For main headings
val TextSecondary = Color.DarkGray      // For body text

// --- 2. The Data Class (ensure your project's class matches this) ---

// --- 3. The Main Screen Composable with Logic ---
@Composable
fun ContentScreen(
    articleId: Int,
) {
    val     viewModel: BannerViewModel = hiltViewModel()

    val article by viewModel.selectedActivity.collectAsState()

    LaunchedEffect(articleId) {
        viewModel.fetchActivityById(articleId)
    }

    // The Surface applies the exact background color to the entire screen
    Surface(modifier = Modifier.fillMaxSize(), color = PageBackground) {
        Box(contentAlignment = Alignment.Center) {
            if (article == null) {
                CircularProgressIndicator(color = AccentColor)
            } else {
                // The ArticleContent composable now holds the specific UI design
                ArticleContent(article = article!!)
            }
        }
    }
}

// --- 4. The Exact UI Design Composable ---
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ArticleContent(article: ActivityBannerItem) {
    LazyColumn(
        // Adds padding to the entire scrollable list
        contentPadding = PaddingValues(vertical = 16.dp)
    )   {
        // Title
        item {
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Image with rounded corners and padding
        item {
            AsyncImage(
                model = article.imageUrl,
                contentDescription = article.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .aspectRatio(16 / 9f)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Metadata Tags
/*
        item {
            article.tagInterest?.let { tags ->
                FlowRow(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tags.forEach { tag ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text(tag) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = AccentColor.copy(alpha = 0.1f)
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
*/

        // Divider
        item {
            Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        }

        // Main Parsed Content
        item {
            article.content?.let {
                StyledContentText(
                    content = it,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Attachment Button
        item {
            article.attachmentUrl?.let { url ->
                val context = LocalContext.current
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentColor),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(colors = listOf(AccentColor, AccentColor)))
                ) {
                    Icon(Icons.Default.Attachment, contentDescription = "Attachment")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Open Attachment", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}


// --- 5. The Content Parsing Composable (Unchanged Logic) ---
@Composable
fun StyledContentText(content: String, modifier: Modifier = Modifier) {
    val sections = content.split("\r\n\r\n")
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        sections.forEach { section ->
            val trimmedSection = section.trim()
            if (trimmedSection.lines().size > 1 && trimmedSection.lines().drop(1)
                    .all { it.trim().startsWith("•") || it.trim().startsWith("①") }
            ) {
                val lines = trimmedSection.lines()
                Text(
                    text = lines.first(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                    lines.drop(1).forEach { line ->
                        val bullet = line.trim().substring(0, 1)
                        val text = line.trim().substring(1).trim()
                        Row {
                            Text(
                                text = bullet,
                                color = AccentColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(text = text, fontSize = 16.sp, color = TextSecondary, lineHeight = 24.sp)
                        }
                    }
                }
            } else if (trimmedSection.contains(":")) {
                Text(
                    text = buildAnnotatedString {
                        val heading = trimmedSection.substringBefore(":") + ":"
                        val body = trimmedSection.substringAfter(":")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)) {
                            append(heading)
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, color = TextSecondary)) {
                            append(body)
                        }
                    },
                    lineHeight = 24.sp
                )
            } else {
                Text(text = trimmedSection, fontSize = 16.sp, color = TextSecondary, lineHeight = 24.sp)
            }
        }
    }
}


/*@Composable
fun ContentScreen(
    articleId: Int,
    viewModel: BannerViewModel = hiltViewModel() // Using Hilt to get the ViewModel
) {
    // 1. Collect the article state from the ViewModel. It starts as null.
    val article by viewModel.selectedActivity.collectAsState()

    // 2. Trigger the data fetch when the screen is first composed or articleId changes.
    LaunchedEffect(articleId) {
        viewModel.fetchActivityById(articleId)
    }

    // 3. Handle the UI based on the state.
    Box(modifier = Modifier.fillMaxSize()) {
        if (article == null) {
            // --- LOADING STATE ---
            // Show a progress indicator in the center while article is null (loading).
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            // --- CONTENT STATE ---
            // Once the article is not null, display the main content.
            // We use a non-null assertion (!!) because the if-check guarantees it's not null here.
            ArticleContent(article = article!!)
        }
    }
}

@Composable
fun ArticleContent(article: ActivityBannerItem) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding() // Handles status bar padding
    ) {
        // Top Image
        item {
            AsyncImage(
                model = article.imageUrl,
                contentDescription = article.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }

        // Content Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-24).dp), // Pull the card up slightly to overlap the image
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Title
                    Text(
                        text = article.title.uppercase(),
                        color = MythologyOrange,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Custom composable to handle HTML content
                    article.content?.let {
                        StyledContentText(content = it, modifier = Modifier.fillMaxWidth())
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Attachment Button
                    article.attachmentUrl?.let { url ->
                        val context = LocalContext.current
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                Color(0xFFF39C12),
                                                MythologyOrange
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Attachment,
                                        contentDescription = "Attachment",
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Open Attachment", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

*//**
 * A custom composable that takes an HTML string and displays it with proper styling.
 * Handles <b> tags for bolding and <ul><li> for bullet points.
 *//*
@Composable
fun StyledContentText(content: String, modifier: Modifier = Modifier) {
    // Split the content into major sections separated by double line breaks.
    val sections = content.split("\r\n\r\n")

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp) // Adds space between each section
    ) {
        sections.forEach { section ->
            // Trim whitespace to handle parsing correctly
            val trimmedSection = section.trim()

            // Check if the section contains a bulleted list structure
            if (trimmedSection.lines().size > 1 && trimmedSection.lines().drop(1)
                    .all { it.trim().startsWith("•") || it.trim().startsWith("①") }
            ) {
                val lines = trimmedSection.lines()
                // Display the heading of the list (e.g., "Guide:")
                Text(
                    text = lines.first(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                // Display the bullet points
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    lines.drop(1).forEach { line ->
                        // Extract bullet and text
                        val bullet = line.trim().substring(0, 1)
                        val text = line.trim().substring(1).trim()

                        Row {
                            Text(
                                text = bullet,
                                color = MythologyOrange,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = text,
                                fontSize = 15.sp,
                                color = Color.DarkGray,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }
            // Handle sections with a heading and text on the same line (e.g., "Supplies:")
            else if (trimmedSection.contains(":")) {
                Text(
                    text = buildAnnotatedString {
                        val heading = trimmedSection.substringBefore(":") + ":"
                        val body = trimmedSection.substringAfter(":")

                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)) {
                            append(heading)
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, fontSize = 15.sp, color = Color.DarkGray)) {
                            append(body)
                        }
                    },
                    lineHeight = 22.sp
                )
            }
            // Fallback for any other plain text
            else {
                Text(
                    text = trimmedSection,
                    fontSize = 15.sp,
                    color = Color.DarkGray,
                    lineHeight = 22.sp
                )
            }
        }
    }
}*/


// NOTE: Ensure your project's Article data class matches this structure
// It should be the same one used in your ViewModel and Repository.
data class Article(
    val imageUrl: String?,
    val title: String,
    val content: String?,
    val attachmentUrl: String?
)
/*
@Composable
fun ContentScreen(
    articleId: Int,
) {
    val viewModel: BannerViewModel = hiltViewModel()
    val article by viewModel.selectedActivity.collectAsState()

    // Fetch article when screen is first opened
    LaunchedEffect(articleId) {
        viewModel.fetchActivityById(articleId)
    }

    when {
        article == null -> {
            // Loading state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        else -> {
            val context = LocalContext.current

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = article!!.imageUrl,
                    contentDescription = article!!.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    article!!.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            settings.javaScriptEnabled = true
                            loadDataWithBaseURL(
                                null,
                                article!!.content ?: "",
                                "text/html",
                                "UTF-8",
                                null
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 400.dp)
                        .padding(16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Attachment button
                article!!.attachmentUrl?.let { url ->
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Icon(Icons.Default.AttachFile, contentDescription = "Attachment")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Open Attachment")
                    }
                }

            }
        }
    }
}
*/

