package com.example.gozlebrowser.presentation.homePage

import com.example.gozlebrowser.domain.model.Recent

data class RecentListState(
    val isLoading: Boolean = false,
    val recentList: List<Recent> = emptyList(),
    val error: String = ""
)