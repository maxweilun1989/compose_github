package com.andro.github.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class TestCoroutineScopeProvider : CoroutineScopeProvider {
    private val testScope = TestScope(UnconfinedTestDispatcher())

    override fun getScope(viewModel: ViewModel): CoroutineScope = testScope

    fun getTestScope(): TestScope = testScope
}
