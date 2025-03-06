package com.andro.github.domain

import com.andro.github.data.Repository

interface GetRepositoriesUseCase {
    suspend fun execute(language: String): Result<List<Repository>>
}
