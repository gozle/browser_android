package com.example.gozlebrowser.domain.usecase

import com.example.gozlebrowser.domain.model.Recent
import com.example.gozlebrowser.domain.repository.RecentRepository
import com.example.gozlebrowser.util.ResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RecentListUseCase @Inject constructor(
    private val repository: RecentRepository
) {

    operator fun invoke(page: String): Flow<ResponseState<List<Recent>>> = flow {
        try {
            emit(ResponseState.Loading())
            val recents = repository.getAllRecents(page).map {
                it.toRecent()
            }
            emit(ResponseState.Success(recents))
        } catch (e: HttpException) {
            emit(ResponseState.Error(e.localizedMessage ?: "An unexpected error"))
        } catch (e: IOException) {
            emit(ResponseState.Error("InternetError"))
        }
    }
}