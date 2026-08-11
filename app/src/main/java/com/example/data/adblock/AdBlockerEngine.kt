package com.example.data.adblock

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AdBlockStats(
    val isAdBlockActive: Boolean = true,
    val isSponsorBlockActive: Boolean = true,
    val isReturnDislikeActive: Boolean = true,
    val blockedVideoAdsCount: Int = 34,
    val blockedBannerAdsCount: Int = 112,
    val sponsorSegmentsSkipped: Int = 18,
    val savedDataMb: Float = 68.4f
)

class AdBlockerEngine {

    private val _stats = MutableStateFlow(AdBlockStats())
    val stats: StateFlow<AdBlockStats> = _stats.asStateFlow()

    fun recordAdBlocked() {
        _stats.value = _stats.value.copy(
            blockedVideoAdsCount = _stats.value.blockedVideoAdsCount + 1,
            savedDataMb = _stats.value.savedDataMb + 2.5f
        )
    }

    fun recordSponsorSkipped() {
        _stats.value = _stats.value.copy(
            sponsorSegmentsSkipped = _stats.value.sponsorSegmentsSkipped + 1,
            savedDataMb = _stats.value.savedDataMb + 1.2f
        )
    }

    fun toggleAdBlock() {
        _stats.value = _stats.value.copy(
            isAdBlockActive = !_stats.value.isAdBlockActive
        )
    }

    fun toggleSponsorBlock() {
        _stats.value = _stats.value.copy(
            isSponsorBlockActive = !_stats.value.isSponsorBlockActive
        )
    }

    fun toggleReturnDislike() {
        _stats.value = _stats.value.copy(
            isReturnDislikeActive = !_stats.value.isReturnDislikeActive
        )
    }
}
