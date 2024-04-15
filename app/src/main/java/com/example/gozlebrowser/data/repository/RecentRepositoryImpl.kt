package com.example.gozlebrowser.data.repository

import com.example.gozlebrowser.data.dataSource.BrowserApi
import com.example.gozlebrowser.data.dataSource.dto.RecentListDTO
import com.example.gozlebrowser.domain.repository.RecentRepository
import javax.inject.Inject

class RecentRepositoryImpl @Inject constructor(
    private val browserApi: BrowserApi
) : RecentRepository {
    override suspend fun getAllRecents(page: String): RecentListDTO {
        return browserApi.getAllRecents(page = page)
    }
}