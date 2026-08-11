package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BottomNavBar
import com.example.ui.components.TopNavBar
import com.example.ui.components.VideoPlayerSheet
import com.example.ui.screens.GmsCoreSettingsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.ShortsScreen
import com.example.ui.screens.SubscriptionsScreen
import com.example.ui.theme.YouTubeCxTheme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.NavigationTab

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YouTubeCxTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val isSearchActive by viewModel.isSearchActive.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    val activeVideo by viewModel.activeVideo.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val isMiniPlayer by viewModel.isMiniPlayer.collectAsStateWithLifecycle()
    val currentQuality by viewModel.currentQuality.collectAsStateWithLifecycle()
    val playbackSpeed by viewModel.playbackSpeed.collectAsStateWithLifecycle()
    val isBackgroundAudio by viewModel.isBackgroundAudioActive.collectAsStateWithLifecycle()
    val activeComments by viewModel.activeComments.collectAsStateWithLifecycle()

    val gmsCoreInfo by viewModel.gmsCoreInfo.collectAsStateWithLifecycle()
    val adBlockStats by viewModel.adBlockStats.collectAsStateWithLifecycle()
    val isLoadingFeed by viewModel.isLoadingFeed.collectAsStateWithLifecycle()
    val historyList by viewModel.historyList.collectAsStateWithLifecycle()
    val bookmarkList by viewModel.bookmarkList.collectAsStateWithLifecycle()
    val playlistsList by viewModel.playlistsList.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        if (isSearchActive) {
            SearchScreen(
                searchQuery = searchQuery,
                onQueryChange = { viewModel.updateSearchQuery(it) },
                onCloseSearch = { viewModel.setSearchActive(false) },
                allVideos = viewModel.allVideos,
                onVideoClick = { video ->
                    viewModel.playVideo(video)
                    viewModel.setSearchActive(false)
                },
                onBookmarkToggle = { video -> viewModel.toggleBookmarkVideo(video) },
                bookmarkList = bookmarkList
            )
        } else {
            Scaffold(
                topBar = {
                    if (currentTab != NavigationTab.SHORTS) {
                        TopNavBar(
                            gmsInfo = gmsCoreInfo,
                            onSearchClick = { viewModel.setSearchActive(true) },
                            onGmsBadgeClick = { viewModel.selectTab(NavigationTab.GMS_SETTINGS) }
                        )
                    }
                },
                bottomBar = {
                    BottomNavBar(
                        selectedTab = currentTab,
                        onTabSelected = { viewModel.selectTab(it) }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentTab) {
                        NavigationTab.HOME -> {
                            HomeScreen(
                                videos = viewModel.allVideos,
                                selectedCategory = selectedCategory,
                                onCategorySelected = { viewModel.selectCategory(it) },
                                gmsInfo = gmsCoreInfo,
                                adBlockStats = adBlockStats,
                                isLoadingFeed = isLoadingFeed,
                                onVideoClick = { video -> viewModel.playVideo(video) },
                                onBookmarkToggle = { video -> viewModel.toggleBookmarkVideo(video) },
                                bookmarkList = bookmarkList,
                                onOpenGmsSettings = { viewModel.selectTab(NavigationTab.GMS_SETTINGS) }
                            )
                        }
                        NavigationTab.SHORTS -> {
                            ShortsScreen(
                                shortsList = viewModel.allShorts,
                                onCommentClick = { shortVideo -> viewModel.playVideo(shortVideo) }
                            )
                        }
                        NavigationTab.SUBSCRIPTIONS -> {
                            SubscriptionsScreen(
                                channels = viewModel.subscribedChannels,
                                videos = viewModel.allVideos,
                                onVideoClick = { video -> viewModel.playVideo(video) },
                                onBookmarkToggle = { video -> viewModel.toggleBookmarkVideo(video) },
                                bookmarkList = bookmarkList
                            )
                        }
                        NavigationTab.LIBRARY -> {
                            LibraryScreen(
                                historyList = historyList,
                                bookmarkList = bookmarkList,
                                playlists = playlistsList,
                                onClearHistory = { viewModel.clearHistory() },
                                onCreatePlaylist = { name, desc -> viewModel.createPlaylist(name, desc) },
                                onDeletePlaylist = { id -> viewModel.deletePlaylist(id) },
                                onVideoClick = { video -> viewModel.playVideo(video) }
                            )
                        }
                        NavigationTab.GMS_SETTINGS -> {
                            GmsCoreSettingsScreen(
                                gmsInfo = gmsCoreInfo,
                                onRefreshGmsStatus = { viewModel.refreshGmsCoreStatus() },
                                onLaunchGmsSettings = { viewModel.launchGmsCoreSettings() },
                                onToggleSponsorBlock = { viewModel.toggleSponsorBlock() },
                                onToggleReturnDislike = { viewModel.toggleReturnDislike() },
                                onToggleBackgroundPlay = { viewModel.toggleGmsBackgroundPlay() }
                            )
                        }
                    }
                }
            }
        }

        // Active Video Player Overlay (Full or Miniplayer at bottom)
        activeVideo?.let { currentVideo ->
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier.align(if (isMiniPlayer) Alignment.BottomCenter else Alignment.TopStart)
            ) {
                VideoPlayerSheet(
                    video = currentVideo,
                    isPlaying = isPlaying,
                    isMiniPlayer = isMiniPlayer,
                    currentQuality = currentQuality,
                    playbackSpeed = playbackSpeed,
                    isBackgroundAudio = isBackgroundAudio,
                    commentsList = activeComments,
                    relatedVideos = viewModel.allVideos.filter { it.id != currentVideo.id },
                    onClose = { viewModel.closePlayer() },
                    onTogglePlayPause = { viewModel.togglePlayPause() },
                    onToggleMiniPlayer = { viewModel.toggleMiniPlayer() },
                    onQualitySelected = { viewModel.setQuality(it) },
                    onSpeedSelected = { viewModel.setSpeed(it) },
                    onToggleBackgroundAudio = { viewModel.toggleBackgroundAudio() },
                    onLikeToggle = { viewModel.toggleLikeActiveVideo() },
                    onDislikeToggle = { viewModel.toggleDislikeActiveVideo() },
                    onSubscribeToggle = { viewModel.toggleSubscribeChannel(currentVideo.channelName) },
                    onBookmarkToggle = { viewModel.toggleBookmarkVideo(currentVideo) },
                    onAddComment = { viewModel.addComment(it) },
                    onSelectRelatedVideo = { newVid -> viewModel.playVideo(newVid) }
                )
            }
        }
    }
}
