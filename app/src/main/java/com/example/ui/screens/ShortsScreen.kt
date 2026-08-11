package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.data.model.VideoItem
import com.example.ui.components.ShortCard

@Composable
fun ShortsScreen(
    shortsList: List<VideoItem>,
    onCommentClick: (VideoItem) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { shortsList.size })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val shortVideo = shortsList[page]
            ShortCard(
                shortVideo = shortVideo,
                onCommentClick = { onCommentClick(shortVideo) }
            )
        }
    }
}
