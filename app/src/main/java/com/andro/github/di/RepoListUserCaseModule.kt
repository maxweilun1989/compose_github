package com.andro.github.di

import com.andro.github.common.CoroutineScopeProvider
import com.andro.github.common.DefaultCoroutineScopeProvider
import com.andro.github.data.RepositoryRepository
import com.andro.github.domain.GetRepositoriesUseCase
import com.andro.github.domain.GetRepositoriesUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepoListUserCaseModule {
    @Provides
    fun provideGetRepositoriesUseCase(repository: RepositoryRepository): GetRepositoriesUseCase = GetRepositoriesUseCaseImpl(repository)

    @Provides
    fun provideViewModelScope(): CoroutineScopeProvider = DefaultCoroutineScopeProvider()
}
