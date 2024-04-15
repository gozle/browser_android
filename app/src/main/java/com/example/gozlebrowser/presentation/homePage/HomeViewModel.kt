package com.example.gozlebrowser.presentation.homePage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gozlebrowser.domain.usecase.RecentListUseCase
import com.example.gozlebrowser.util.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val recentListUseCase: RecentListUseCase
) : ViewModel() {

    private val _recentListValue = MutableStateFlow(RecentListState())
    var recentListValue: StateFlow<RecentListState> = _recentListValue

    fun getAllRecents(page: String) = viewModelScope.launch(Dispatchers.IO) {
        recentListUseCase(page).collect {
            when (it) {
                is ResponseState.Success -> {
                    _recentListValue.value = RecentListState(recentList = it.data ?: emptyList())
                }

                is ResponseState.Loading -> {
                    _recentListValue.value = RecentListState(isLoading = true)
                }

                is ResponseState.Error -> {
                    _recentListValue.value =
                        RecentListState(error = it.message ?: "An Unexpected Error")
                }
            }
        }
    }


}