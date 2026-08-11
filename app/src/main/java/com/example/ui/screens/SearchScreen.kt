package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.BookmarkEntity
import com.example.data.model.VideoItem
import com.example.ui.components.VideoCard
import com.example.ui.theme.YTRed

@Composable
fun SearchScreen(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onCloseSearch: () -> Unit,
    allVideos: List<VideoItem>,
    onVideoClick: (VideoItem) -> Unit,
    onBookmarkToggle: (VideoItem) -> Unit,
    bookmarkList: List<BookmarkEntity>
) {
    val searchResults = if (searchQuery.isBlank()) {
        emptyList()
    } else {
        allVideos.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.channelName.contains(searchQuery, ignoreCase = true) ||
            it.description.contains(searchQuery, ignoreCase = true)
        }
    }

    val sampleSuggestions = listOf(
        "com.gmscx.services microg setup",
        "YouTube cx client v0.0.1",
        "Android 16 features breakdown",
        "Lofi beats background play",
        "Unreal Engine 5.5 mobile test"
    ).filter { it.contains(searchQuery, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Search Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCloseSearch) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onQueryChange,
                placeholder = { Text("Search YouTube cx...", fontSize = 14.sp) },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("search_input"),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = YTRed),
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                }
            )
        }

        if (searchQuery.isBlank()) {
            // Show suggestions / search history
            Text(
                text = "Recent Searches",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn {
                items(sampleSuggestions) { suggestion ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onQueryChange(suggestion) }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.History, contentDescription = null, tint = YTRed)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = suggestion, fontSize = 14.sp)
                    }
                }
            }
        } else {
            // Search Results
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Text(
                        text = "Results (${searchResults.size})",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                items(searchResults) { video ->
                    val isBookmarked = bookmarkList.any { it.videoId == video.id }
                    VideoCard(
                        video = video,
                        onClick = { onVideoClick(video) },
                        onBookmarkToggle = { onBookmarkToggle(video) },
                        isBookmarked = isBookmarked
                    )
                }
            }
        }
    }
}
