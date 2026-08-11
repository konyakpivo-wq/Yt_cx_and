package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.db.BookmarkEntity
import com.example.data.db.HistoryEntity
import com.example.data.db.PlaylistEntity
import com.example.data.model.VideoItem
import com.example.ui.theme.YTRed

@Composable
fun LibraryScreen(
    historyList: List<HistoryEntity>,
    bookmarkList: List<BookmarkEntity>,
    playlists: List<PlaylistEntity>,
    onClearHistory: () -> Unit,
    onCreatePlaylist: (String, String) -> Unit,
    onDeletePlaylist: (Int) -> Unit,
    onVideoClick: (VideoItem) -> Unit
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var playlistNameInput by remember { mutableStateOf("") }
    var playlistDescInput by remember { mutableStateOf("") }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("New Playlist", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = playlistNameInput,
                        onValueChange = { playlistNameInput = it },
                        label = { Text("Playlist Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = playlistDescInput,
                        onValueChange = { playlistDescInput = it },
                        label = { Text("Description (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (playlistNameInput.isNotBlank()) {
                            onCreatePlaylist(playlistNameInput, playlistDescInput)
                            playlistNameInput = ""
                            playlistDescInput = ""
                            showCreateDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = YTRed)
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 12.dp)
    ) {
        // Watch History Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.History, contentDescription = null, tint = YTRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Watch History (${historyList.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                if (historyList.isNotEmpty()) {
                    TextButton(onClick = onClearHistory) {
                        Text("Clear All", fontSize = 12.sp, color = YTRed)
                    }
                }
            }

            if (historyList.isEmpty()) {
                Text(
                    text = "No watch history recorded yet.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            } else {
                LazyRow(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                ) {
                    items(historyList) { historyItem ->
                        Card(
                            modifier = Modifier
                                .width(160.dp)
                                .padding(horizontal = 4.dp)
                                .clickable {
                                    onVideoClick(
                                        VideoItem(
                                            id = historyItem.videoId,
                                            title = historyItem.title,
                                            channelName = historyItem.channelName,
                                            channelAvatarUrl = "https://picsum.photos/seed/hist/100/100",
                                            thumbnailUrl = historyItem.thumbnailUrl,
                                            viewsCount = historyItem.viewsCount,
                                            timeAgo = historyItem.timeAgo,
                                            duration = historyItem.duration
                                        )
                                    )
                                },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(16f / 9f)
                                        .background(Color.DarkGray)
                                ) {
                                    AsyncImage(
                                        model = historyItem.thumbnailUrl,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Text(
                                    text = historyItem.title,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Saved / Bookmarks Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Bookmark, contentDescription = null, tint = YTRed)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Saved Videos (${bookmarkList.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            if (bookmarkList.isEmpty()) {
                Text(
                    text = "No saved videos yet. Tap 3-dots on any video to save.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            } else {
                LazyRow(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                ) {
                    items(bookmarkList) { bookmark ->
                        Card(
                            modifier = Modifier
                                .width(160.dp)
                                .padding(horizontal = 4.dp)
                                .clickable {
                                    onVideoClick(
                                        VideoItem(
                                            id = bookmark.videoId,
                                            title = bookmark.title,
                                            channelName = bookmark.channelName,
                                            channelAvatarUrl = "https://picsum.photos/seed/bm/100/100",
                                            thumbnailUrl = bookmark.thumbnailUrl,
                                            viewsCount = bookmark.viewsCount,
                                            timeAgo = "Saved",
                                            duration = bookmark.duration
                                        )
                                    )
                                },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(16f / 9f)
                                        .background(Color.DarkGray)
                                ) {
                                    AsyncImage(
                                        model = bookmark.thumbnailUrl,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Text(
                                    text = bookmark.title,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Playlists Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.PlaylistPlay, contentDescription = null, tint = YTRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Playlists",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                IconButton(
                    onClick = { showCreateDialog = true },
                    modifier = Modifier.testTag("add_playlist_btn")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "New Playlist", tint = YTRed)
                }
            }

            if (playlists.isEmpty()) {
                Text(
                    text = "No custom playlists created. Tap + to add one.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            } else {
                playlists.forEach { playlist ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(YTRed.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = Icons.Default.PlaylistPlay, contentDescription = null, tint = YTRed)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(text = playlist.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(text = playlist.description.ifEmpty { "Playlist" }, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }

                            IconButton(onClick = { onDeletePlaylist(playlist.id) }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
                            }
                        }
                    }
                }
            }
        }

        // Offline Downloads Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = YTRed)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "CX Offline Downloads", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "3 videos ready for offline viewing (1.2 GB used)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
