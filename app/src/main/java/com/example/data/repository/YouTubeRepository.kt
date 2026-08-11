package com.example.data.repository

import com.example.data.db.BookmarkEntity
import com.example.data.db.HistoryEntity
import com.example.data.db.PlaylistEntity
import com.example.data.db.YouTubeCxDao
import com.example.data.model.ChannelItem
import com.example.data.model.CommentItem
import com.example.data.model.VideoItem
import kotlinx.coroutines.flow.Flow

class YouTubeRepository(private val dao: YouTubeCxDao) {

    val historyList: Flow<List<HistoryEntity>> = dao.getWatchHistory()
    val bookmarkList: Flow<List<BookmarkEntity>> = dao.getBookmarks()
    val playlists: Flow<List<PlaylistEntity>> = dao.getPlaylists()

    suspend fun addToHistory(video: VideoItem) {
        dao.insertHistory(
            HistoryEntity(
                videoId = video.id,
                title = video.title,
                channelName = video.channelName,
                thumbnailUrl = video.thumbnailUrl,
                duration = video.duration,
                viewsCount = video.viewsCount,
                timeAgo = video.timeAgo
            )
        )
    }

    suspend fun clearHistory() {
        dao.clearWatchHistory()
    }

    fun isBookmarked(videoId: String): Flow<Boolean> = dao.isBookmarked(videoId)

    suspend fun toggleBookmark(video: VideoItem, currentIsBookmarked: Boolean) {
        if (currentIsBookmarked) {
            dao.removeBookmark(video.id)
        } else {
            dao.insertBookmark(
                BookmarkEntity(
                    videoId = video.id,
                    title = video.title,
                    channelName = video.channelName,
                    thumbnailUrl = video.thumbnailUrl,
                    duration = video.duration,
                    viewsCount = video.viewsCount
                )
            )
        }
    }

    suspend fun createPlaylist(name: String, description: String = "") {
        dao.insertPlaylist(PlaylistEntity(name = name, description = description, videoCount = 0))
    }

    suspend fun deletePlaylist(id: Int) {
        dao.deletePlaylist(id)
    }

    fun getSampleVideos(): List<VideoItem> {
        return listOf(
            VideoItem(
                id = "cx_01",
                title = "Android 16 Features Breakdown: Custom MicroG & Extended GMS Core com.gmscx.services Integration",
                channelName = "Android CX Tech",
                channelAvatarUrl = "https://picsum.photos/seed/cxtech/100/100",
                thumbnailUrl = "https://picsum.photos/seed/android16/640/360",
                viewsCount = "248K views",
                timeAgo = "3 hours ago",
                duration = "14:22",
                category = "Tech",
                description = "Exploring the inner workings of YouTube cx v0.0.1 and microG gms-core under the custom package name com.gmscx.services. Features ad blocking, background playback, and Return YouTube Dislike integration.",
                likeCount = 18400,
                dislikeCount = 142
            ),
            VideoItem(
                id = "cx_02",
                title = "10 Hours of Relaxing Cyberpunk Lofi Beats to Study/Chill to (CX Red Edition)",
                channelName = "Lofi CX Station",
                channelAvatarUrl = "https://picsum.photos/seed/lofiavatar/100/100",
                thumbnailUrl = "https://picsum.photos/seed/lofi/640/360",
                viewsCount = "1.8M views",
                timeAgo = "2 days ago",
                duration = "10:00:00",
                category = "Music",
                description = "Relax with continuous chilled synthwave and lofi beats. Background audio enabled via YouTube cx v0.0.1 with com.gmscx.services account sync.",
                likeCount = 89200,
                dislikeCount = 512
            ),
            VideoItem(
                id = "cx_03",
                title = "Unreal Engine 5.5 Mobile Graphics Test on Flagship Android Devices",
                channelName = "Mobile Gaming CX",
                channelAvatarUrl = "https://picsum.photos/seed/gamingavatar/100/100",
                thumbnailUrl = "https://picsum.photos/seed/ue5/640/360",
                viewsCount = "520K views",
                timeAgo = "5 hours ago",
                duration = "18:45",
                category = "Gaming",
                description = "Testing ray tracing and nanite mobile rendering on modern GPUs with full 60fps unlocked playback.",
                likeCount = 34100,
                dislikeCount = 820
            ),
            VideoItem(
                id = "cx_04",
                title = "SpaceX Starship Orbital Launch Test #8 Full Stream Highlight & Analysis",
                channelName = "Cosmo News CX",
                channelAvatarUrl = "https://picsum.photos/seed/cosmo/100/100",
                thumbnailUrl = "https://picsum.photos/seed/space/640/360",
                viewsCount = "3.2M views",
                timeAgo = "1 day ago",
                duration = "22:10",
                category = "News",
                description = "Full coverage of the Starship orbital flight test including stage separation and booster catch.",
                likeCount = 195000,
                dislikeCount = 1200
            ),
            VideoItem(
                id = "cx_05",
                title = "Building a Custom MicroG Client & MicroG Patch for YouTube in Kotlin Compose",
                channelName = "Code Craft CX",
                channelAvatarUrl = "https://picsum.photos/seed/code/100/100",
                thumbnailUrl = "https://picsum.photos/seed/kotlin/640/360",
                viewsCount = "95K views",
                timeAgo = "12 hours ago",
                duration = "31:05",
                category = "Tech",
                description = "Step by step tutorial on packaging GMS core services under com.gmscx.services and configuring YouTube cx client v0.0.1.",
                likeCount = 9800,
                dislikeCount = 45
            ),
            VideoItem(
                id = "cx_06",
                title = "Top 10 Hidden Android Settings You Must Change Immediately!",
                channelName = "Tech Zone CX",
                channelAvatarUrl = "https://picsum.photos/seed/tzavatar/100/100",
                thumbnailUrl = "https://picsum.photos/seed/androidhacks/640/360",
                viewsCount = "1.1M views",
                timeAgo = "4 days ago",
                duration = "12:50",
                category = "Tech",
                description = "Optimize battery life, notification behavior, and privacy permissions on your Android device.",
                likeCount = 67000,
                dislikeCount = 950
            )
        )
    }

    fun getSampleShorts(): List<VideoItem> {
        return listOf(
            VideoItem(
                id = "short_01",
                title = "Insane 120 FPS Jetpack Compose Animation Trick! 🚀 #android #cx",
                channelName = "Android CX Shorts",
                channelAvatarUrl = "https://picsum.photos/seed/sh1/100/100",
                thumbnailUrl = "https://picsum.photos/seed/short1/360/640",
                viewsCount = "1.2M",
                timeAgo = "Yesterday",
                duration = "0:30",
                isShort = true,
                likeCount = 142000,
                dislikeCount = 1100
            ),
            VideoItem(
                id = "short_02",
                title = "When your custom microG package com.gmscx.services connects instantly! 😂 #microg",
                channelName = "Dev Humor CX",
                channelAvatarUrl = "https://picsum.photos/seed/sh2/100/100",
                thumbnailUrl = "https://picsum.photos/seed/short2/360/640",
                viewsCount = "850K",
                timeAgo = "3 days ago",
                duration = "0:15",
                isShort = true,
                likeCount = 98000,
                dislikeCount = 420
            ),
            VideoItem(
                id = "short_03",
                title = "Return YouTube Dislike is back! See exact dislikes everywhere 🔥",
                channelName = "CX Vanced Tricks",
                channelAvatarUrl = "https://picsum.photos/seed/sh3/100/100",
                thumbnailUrl = "https://picsum.photos/seed/short3/360/640",
                viewsCount = "2.4M",
                timeAgo = "1 week ago",
                duration = "0:45",
                isShort = true,
                likeCount = 230000,
                dislikeCount = 890
            )
        )
    }

    fun getSubscribedChannels(): List<ChannelItem> {
        return listOf(
            ChannelItem(
                id = "ch_01",
                name = "Android CX Tech",
                handle = "@androidcx",
                avatarUrl = "https://picsum.photos/seed/cxtech/100/100",
                subscriberCount = "1.24M subscribers",
                isSubscribed = true
            ),
            ChannelItem(
                id = "ch_02",
                name = "Lofi CX Station",
                handle = "@loficx",
                avatarUrl = "https://picsum.photos/seed/lofiavatar/100/100",
                subscriberCount = "5.8M subscribers",
                isSubscribed = true
            ),
            ChannelItem(
                id = "ch_03",
                name = "Mobile Gaming CX",
                handle = "@gamingcx",
                avatarUrl = "https://picsum.photos/seed/gamingavatar/100/100",
                subscriberCount = "920K subscribers",
                isSubscribed = true
            ),
            ChannelItem(
                id = "ch_04",
                name = "Cosmo News CX",
                handle = "@cosmonews",
                avatarUrl = "https://picsum.photos/seed/cosmo/100/100",
                subscriberCount = "2.1M subscribers",
                isSubscribed = true
            )
        )
    }

    fun getCommentsForVideo(videoId: String): List<CommentItem> {
        return listOf(
            CommentItem(
                id = "c1",
                videoId = videoId,
                authorName = "Alex Developer",
                authorAvatarUrl = "https://picsum.photos/seed/u1/100/100",
                text = "YouTube cx client v0.0.1 works amazingly smooth! The com.gmscx.services microG sync is flawless.",
                timeAgo = "1 hour ago",
                likesCount = 342,
                isLiked = true
            ),
            CommentItem(
                id = "c2",
                videoId = videoId,
                authorName = "GMS Tester",
                authorAvatarUrl = "https://picsum.photos/seed/u2/100/100",
                text = "Having SponsorBlock and Return YouTube Dislike out of the box in this CX client is a game changer!",
                timeAgo = "2 hours ago",
                likesCount = 189
            ),
            CommentItem(
                id = "c3",
                videoId = videoId,
                authorName = "Elena_K",
                authorAvatarUrl = "https://picsum.photos/seed/u3/100/100",
                text = "Loved the dark red OLED theme. Background audio continues playing even when screen turns off!",
                timeAgo = "4 hours ago",
                likesCount = 95
            ),
            CommentItem(
                id = "c4",
                videoId = videoId,
                authorName = "TechEnthusiast99",
                authorAvatarUrl = "https://picsum.photos/seed/u4/100/100",
                text = "Awesome work on package com.gmscx.services support!",
                timeAgo = "5 hours ago",
                likesCount = 42
            )
        )
    }
}
