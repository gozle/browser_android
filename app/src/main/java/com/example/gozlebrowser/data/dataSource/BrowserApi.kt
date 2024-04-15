package com.example.gozlebrowser.data.dataSource

import com.example.gozlebrowser.data.dataSource.dto.RecentListDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface BrowserApi {
    @GET("")
    suspend fun getAllRecents(@Query("page")page:String): RecentListDTO

}