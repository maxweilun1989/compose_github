package com.andro.github.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andro.github.network.GitHubApiService
import com.andro.github.network.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GitHubViewModel
    @Inject
    constructor(
        private val gitHubApiService: GitHubApiService,
    ) : ViewModel() {
        private val _repositories = MutableStateFlow<List<Repository>>(emptyList())
        val repositories: StateFlow<List<Repository>> = _repositories

        fun fetchPopularRepositories() {
            viewModelScope.launch {
                try {
                    val response = gitHubApiService.getPopularRepositories()
                    _repositories.value = response.items
                } catch (e: Exception) {
                    // 处理错误
                    e.printStackTrace()
                }
            }
        }
    }
