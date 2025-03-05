package com.andro.github.ui.viewmodel

sealed class RepositoryListUiState<out T> {
    object Loading : RepositoryListUiState<Nothing>()

    data class Success<T>(
        val data: T,
    ) : RepositoryListUiState<T>()

    data class Error(
        val message: String,
    ) : RepositoryListUiState<Nothing>()
}
