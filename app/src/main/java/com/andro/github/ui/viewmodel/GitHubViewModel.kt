package com.andro.github.ui.viewmodel

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.andro.github.app.AppConfig
import com.andro.github.common.CoroutineScopeProvider
import com.andro.github.data.Repository
import com.andro.github.domain.GetRepositoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GitHubViewModel
    @Inject
    constructor(
        private val getRepositoriesUseCase: GetRepositoriesUseCase,
        private val scopeProvider: CoroutineScopeProvider,
    ) : ViewModel() {
        private val scope = scopeProvider.getScope(this)

        private val _uiState =
            MutableStateFlow<RepositoryListUiState<List<Repository>>>(RepositoryListUiState.Loading)
        val uiState: StateFlow<RepositoryListUiState<List<Repository>>> = _uiState

        private val _language = MutableStateFlow(AppConfig.DEFAULT_LANGUAGE)
        val language: StateFlow<String> = _language

        // for lazyColumn scroll position
        var llState: LazyListState by mutableStateOf(LazyListState(0, 0))

        fun fetchRepositories() {
            resetListState()
            _uiState.value = RepositoryListUiState.Loading
            scope.launch {
                val result = getRepositoriesUseCase.execute(language.value)
                _uiState.value =
                    if (result.isSuccess) {
                        RepositoryListUiState.Success(result.getOrThrow())
                    } else {
                        RepositoryListUiState.Error(
                            result.exceptionOrNull()?.message ?: "Unknown error",
                        )
                    }
            }
        }

        fun setLanguage(newLanguage: String) {
            _language.value = newLanguage
            fetchRepositories()
        }

        private fun resetListState() {
            llState = LazyListState(0, 0)
        }
    }
