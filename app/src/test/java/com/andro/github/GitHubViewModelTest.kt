package com.andro.github

import com.andro.github.common.TestCoroutineScopeProvider
import com.andro.github.di.FakeGetReposErrorUserCase
import com.andro.github.ui.viewmodel.GitHubViewModel
import com.andro.github.ui.viewmodel.RepositoryListUiState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GitHubViewModelTest {
    private lateinit var viewModel: GitHubViewModel
    private lateinit var fakeUseCase: FakeGetReposErrorUserCase
    private lateinit var testCoroutineScopeProvider: TestCoroutineScopeProvider

    @Before
    fun setUp() {
        testCoroutineScopeProvider = TestCoroutineScopeProvider()
        fakeUseCase = FakeGetReposErrorUserCase()
        viewModel = GitHubViewModel(fakeUseCase, testCoroutineScopeProvider)
    }

    @Test
    fun `fetchRepositories returns success`(): Unit =
        runTest {
            fakeUseCase.setReturnError(false)
            viewModel.fetchRepositories("Kotlin")

            val uiState = viewModel.uiState.value
            assertTrue(uiState is RepositoryListUiState.Success)

            assertEquals(10, (uiState as RepositoryListUiState.Success).data.size)
            for (i in 0 until 10) {
                assertEquals("name$i", uiState.data[i].name)
                assertEquals("full_name$i", uiState.data[i].fullName)
                assertEquals("Description for TestRepo$i", uiState.data[i].description)
                assertEquals(i * 10, uiState.data[i].stars)
                assertEquals(i * 5, uiState.data[i].forks)
                assertEquals(i * 2, uiState.data[i].openIssues)
                assertEquals("Kotlin", uiState.data[i].language)
            }
        }

    @Test
    fun `fetchRepositories returns error`(): Unit =
        runTest {
            fakeUseCase.setReturnError(true)

            viewModel.fetchRepositories("Kotlin")

            val uiState = viewModel.uiState.value
            assertTrue(uiState is RepositoryListUiState.Error)
            assertEquals("Test error", (uiState as RepositoryListUiState.Error).message)
        }
}
