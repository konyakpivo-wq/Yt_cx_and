package com.example.data.model

data class VideoItem(
    val id: String,
    val title: String,
    val channelName: String,
    val channelAvatarUrl: String,
    val thumbnailUrl: String,
    val videoUrl: String = "",
    val viewsCount: String,
    val timeAgo: String,
    val duration: String,
    val category: String = "All",
    val description: String = "",
    val isShort: Boolean = false,
    val likeCount: Int = 12400,
    val dislikeCount: Int = 312,
    val isLiked: Boolean = false,
    val isDisliked: Boolean = false,
    val isSubscribed: Boolean = false
)

data class CommentItem(
    val id: String,
    val videoId: String,
    val authorName: String,
    val authorAvatarUrl: String,
    val text: String,
    val timeAgo: String,
    val likesCount: Int = 42,
    val isLiked: Boolean = false
)

data class ChannelItem(
    val id: String,
    val name: String,
    val handle: String,
    val avatarUrl: String,
    val subscriberCount: String,
    val isSubscribed: Boolean = false
)

data class GmsCoreInfo(
    val packageName: String = "com.gmscx.services",
    val isInstalled: Boolean = false,
    val versionName: String = "v24.08.15 (GMS-Core)",
    val isAccountConnected: Boolean = true,
    val accountEmail: String = "user.cx@gmscx.services",
    val sponsorBlockEnabled: Boolean = true,
    val returnDislikeEnabled: Boolean = true,
    val backgroundPlayEnabled: Boolean = true,
    val preferredQuality: String = "1080p60"
)
