package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.adblock.AdBlockerEngine
import com.example.data.adblock.AdBlockStats
import com.example.data.api.YouTubeApiService
import com.example.data.db.AppDatabase
import com.example.data.db.BookmarkEntity
import com.example.data.db.HistoryEntity
import com.example.data.db.PlaylistEntity
import com.example.data.gms.GmsCoreManager
import com.example.data.model.CommentItem
import com.example.data.model.GmsCoreInfo
import com.example.data.model.VideoItem
import com.example.data.repository.YouTubeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class NavigationTab {
    HOME, SHORTS, SUBSCRIPTIONS, LIBRARY, GMS_SETTINGS
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = YouTubeRepository(db.youTubeCxDao())
    private val gmsManager = GmsCoreManager(application)
    private val apiService = YouTubeApiService()
    private val adBlocker = AdBlockerEngine()

    // Navigation state
    private val _currentTab = MutableStateFlow(NavigationTab.HOME)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    // Active Category Filter
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Live YouTube Videos Feed State
    private val _youtubeVideos = MutableStateFlow<List<VideoItem>>(repository.getSampleVideos())
    val youtubeVideos: StateFlow<List<VideoItem>> = _youtubeVideos.asStateFlow()

    private val _searchResults = MutableStateFlow<List<VideoItem>>(emptyList())
    val searchResults: StateFlow<List<VideoItem>> = _searchResults.asStateFlow()

    private val _isLoadingFeed = MutableStateFlow(false)
    val isLoadingFeed: StateFlow<Boolean> = _isLoadingFeed.asStateFlow()

    // Search state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    // Video Player State
    private val _activeVideo = MutableStateFlow<VideoItem?>(null)
    val activeVideo: StateFlow<VideoItem?> = _activeVideo.asStateFlow()

    private val _isPlaying = MutableStateFlow(true)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isMiniPlayer = MutableStateFlow(false)
    val isMiniPlayer: StateFlow<Boolean> = _isMiniPlayer.asStateFlow()

    private val _playbackProgress = MutableStateFlow(0.35f)
    val playbackProgress: StateFlow<Float> = _playbackProgress.asStateFlow()

    private val _currentQuality = MutableStateFlow("1080p60")
    val currentQuality: StateFlow<String> = _currentQuality.asStateFlow()

    private val _playbackSpeed = MutableStateFlow("1.0x")
    val playbackSpeed: StateFlow<String> = _playbackSpeed.asStateFlow()

    private val _isBackgroundAudioActive = MutableStateFlow(false)
    val isBackgroundAudioActive: StateFlow<Boolean> = _isBackgroundAudioActive.asStateFlow()

    // Comments for active video
    private val _activeComments = MutableStateFlow<List<CommentItem>>(emptyList())
    val activeComments: StateFlow<List<CommentItem>> = _activeComments.asStateFlow()

    // GMS Core status
    private val _gmsCoreInfo = MutableStateFlow(gmsManager.getGmsCoreInfo())
    val gmsCoreInfo: StateFlow<GmsCoreInfo> = _gmsCoreInfo.asStateFlow()

    // Ad Block Engine Stats
    val adBlockStats: StateFlow<AdBlockStats> = adBlocker.stats

    // Room DB StateFlows
    val historyList: StateFlow<List<HistoryEntity>> = repository.historyList
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarkList: StateFlow<List<BookmarkEntity>> = repository.bookmarkList
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val playlistsList: StateFlow<List<PlaylistEntity>> = repository.playlists
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All videos & shorts list
    val allVideos: List<VideoItem> get() = _youtubeVideos.value
    val allShorts: List<VideoItem> = repository.getSampleShorts()
    val subscribedChannels = repository.getSubscribedChannels()

    init {
        loadLiveYouTubeFeed("All")
    }

    fun selectTab(tab: NavigationTab) {
        _currentTab.value = tab
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        loadLiveYouTubeFeed(category)
    }

    private fun loadLiveYouTubeFeed(category: String) {
        viewModelScope.launch {
            _isLoadingFeed.value = true
            val fetched = apiService.fetchTrendingVideos(category)
            if (fetched.isNotEmpty()) {
                _youtubeVideos.value = fetched
            }
            _isLoadingFeed.value = false
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isNotBlank()) {
            viewModelScope.launch {
                val results = apiService.searchYouTubeVideos(query)
                _searchResults.value = results
            }
        } else {
            _searchResults.value = emptyList()
        }
    }

    fun setSearchActive(active: Boolean) {
        _isSearchActive.value = active
    }

    fun playVideo(video: VideoItem) {
        _activeVideo.value = video
        _isPlaying.value = true
        _isMiniPlayer.value = false
        _activeComments.value = repository.getCommentsForVideo(video.id)

        // AdBlocker registers an ad blocked seamlessly
        adBlocker.recordAdBlocked()

        viewModelScope.launch {
            repository.addToHistory(video)
        }
    }

    fun closePlayer() {
        _activeVideo.value = null
        _isMiniPlayer.value = false
    }

    fun togglePlayPause() {
        _isPlaying.value = !_isPlaying.value
    }

    fun toggleMiniPlayer() {
        _isMiniPlayer.value = !_isMiniPlayer.value
    }

    fun setQuality(quality: String) {
        _currentQuality.value = quality
    }

    fun setSpeed(speed: String) {
        _playbackSpeed.value = speed
    }

    fun toggleBackgroundAudio() {
        _isBackgroundAudioActive.value = !_isBackgroundAudioActive.value
    }

    fun toggleLikeActiveVideo() {
        val current = _activeVideo.value ?: return
        val newIsLiked = !current.isLiked
        val newLikeCount = if (newIsLiked) current.likeCount + 1 else current.likeCount - 1
        _activeVideo.value = current.copy(
            isLiked = newIsLiked,
            likeCount = newLikeCount,
            isDisliked = false
        )
    }

    fun toggleDislikeActiveVideo() {
        val current = _activeVideo.value ?: return
        val newIsDisliked = !current.isDisliked
        val newDislikeCount = if (newIsDisliked) current.dislikeCount + 1 else current.dislikeCount - 1
        _activeVideo.value = current.copy(
            isDisliked = newIsDisliked,
            dislikeCount = newDislikeCount,
            isLiked = false
        )
    }

    fun toggleSubscribeChannel(channelName: String) {
        val current = _activeVideo.value
        if (current != null && current.channelName == channelName) {
            _activeVideo.value = current.copy(isSubscribed = !current.isSubscribed)
        }
    }

    fun toggleBookmarkVideo(video: VideoItem) {
        viewModelScope.launch {
            val isBookmarkedCurrently = bookmarkList.value.any { it.videoId == video.id }
            repository.toggleBookmark(video, isBookmarkedCurrently)
        }
    }

    fun addComment(text: String) {
        val current = _activeVideo.value ?: return
        if (text.isBlank()) return
        val newComment = CommentItem(
            id = "c_${System.currentTimeMillis()}",
            videoId = current.id,
            authorName = "konyakpivo (com.gmscx.services)",
            authorAvatarUrl = "https://picsum.photos/seed/you/100/100",
            text = text,
            timeAgo = "Just now",
            likesCount = 1,
            isLiked = true
        )
        _activeComments.value = listOf(newComment) + _activeComments.value
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    fun createPlaylist(name: String, description: String = "") {
        viewModelScope.launch {
            repository.createPlaylist(name, description)
        }
    }

    fun deletePlaylist(id: Int) {
        viewModelScope.launch {
            repository.deletePlaylist(id)
        }
    }

    fun refreshGmsCoreStatus() {
        _gmsCoreInfo.value = gmsManager.getGmsCoreInfo()
    }

    fun launchGmsCoreSettings(): Boolean {
        return gmsManager.openGmsCoreSettings()
    }

    fun toggleSponsorBlock() {
        val current = _gmsCoreInfo.value
        _gmsCoreInfo.value = current.copy(sponsorBlockEnabled = !current.sponsorBlockEnabled)
        adBlocker.toggleSponsorBlock()
    }

    fun toggleReturnDislike() {
        val current = _gmsCoreInfo.value
        _gmsCoreInfo.value = current.copy(returnDislikeEnabled = !current.returnDislikeEnabled)
        adBlocker.toggleReturnDislike()
    }

    fun toggleGmsBackgroundPlay() {
        val current = _gmsCoreInfo.value
        _gmsCoreInfo.value = current.copy(backgroundPlayEnabled = !current.backgroundPlayEnabled)
    }

    fun toggleAdBlock() {
        adBlocker.toggleAdBlock()
    }
}

