package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watch_history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val videoId: String,
    val title: String,
    val channelName: String,
    val thumbnailUrl: String,
    val duration: String,
    val viewsCount: String,
    val timeAgo: String,
    val watchedAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val videoId: String,
    val title: String,
    val channelName: String,
    val thumbnailUrl: String,
    val duration: String,
    val viewsCount: String,
    val savedAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String = "",
    val videoCount: Int = 0,
    val createdAtMillis: Long = System.currentTimeMillis()
)
