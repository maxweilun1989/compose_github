package com.andro.github.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    ) : ViewModel() {
        private val _uiState =
            MutableStateFlow<RepositoryListUiState<List<Repository>>>(RepositoryListUiState.Loading)
        val uiState: StateFlow<RepositoryListUiState<List<Repository>>> = _uiState

        fun fetchRepositories(language: String) {
            _uiState.value = RepositoryListUiState.Loading
            viewModelScope.launch {
                val result = getRepositoriesUseCase(language)
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
    }
