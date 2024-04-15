package com.example.gozlebrowser.domain.repository

import com.example.gozlebrowser.data.dataSource.dto.RecentListDTO

interface RecentRepository {

    suspend fun getAllRecents(page: String): RecentListDTO

}