package com.andro.github

import app.cash.turbine.test
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
    fun `fetchRepositories returns loading state first when success`(): Unit =
        testCoroutineScopeProvider.getTestScope().runTest {
            viewModel.uiState.test {
                fakeUseCase.setReturnError(false)
                viewModel.fetchRepositories()

                assertTrue(awaitItem() is RepositoryListUiState.Loading)

                val nextState = awaitItem()
                assertTrue(nextState is RepositoryListUiState.Success)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `fetchRepositories returns loading state first when error`(): Unit =
        testCoroutineScopeProvider.getTestScope().runTest {
            viewModel.uiState.test {
                fakeUseCase.setReturnError(true)
                viewModel.fetchRepositories()

                assertTrue(awaitItem() is RepositoryListUiState.Loading)

                val nextState = awaitItem()
                assertTrue(nextState is RepositoryListUiState.Error)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `fetchRepositories returns success`(): Unit =
        testCoroutineScopeProvider.getTestScope().runTest {
            fakeUseCase.setReturnError(false)
            viewModel.fetchRepositories()

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
        testCoroutineScopeProvider.getTestScope().runTest {
            fakeUseCase.setReturnError(true)

            viewModel.fetchRepositories()

            val uiState = viewModel.uiState.value
            assertTrue(uiState is RepositoryListUiState.Error)
            assertEquals("Test error", (uiState as RepositoryListUiState.Error).message)
        }

    @Test
    fun `fetchRepositories should reset list state`() =
        testCoroutineScopeProvider.getTestScope().run {
            fakeUseCase.setReturnError(false)
            viewModel.fetchRepositories()

            val uiState = viewModel.uiState.value
            assertTrue(uiState is RepositoryListUiState.Success)
            assertEquals(0, viewModel.llState.firstVisibleItemIndex)
            assertEquals(0, viewModel.llState.firstVisibleItemScrollOffset)
        }

    @Test
    fun `setLanguage should show loading first`(): Unit =
        testCoroutineScopeProvider.getTestScope().runTest {
            viewModel.uiState.test {
                fakeUseCase.setReturnError(false)
                viewModel.setLanguage("Java")

                assertTrue(awaitItem() is RepositoryListUiState.Loading)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `setLanguage updates language and fetches repositories`(): Unit =
        testCoroutineScopeProvider.getTestScope().runTest {
            fakeUseCase.setReturnError(false)
            viewModel.setLanguage("Java")

            val uiState = viewModel.uiState.value
            assertTrue(uiState is RepositoryListUiState.Success)
            assertEquals("Java", viewModel.language.value)
        }
}
