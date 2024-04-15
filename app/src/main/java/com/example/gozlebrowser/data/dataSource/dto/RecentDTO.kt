package com.example.gozlebrowser.data.dataSource.dto

import com.example.gozlebrowser.domain.model.Recent

data class RecentDTO(
    val id: Int,
    val title: String?,
    val imageUrl: String
){
    fun toRecent(): Recent {
        return Recent(
            id = id,
            title = title,
            imageUrl = imageUrl,
        )
    }
}
