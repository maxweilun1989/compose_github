package com.andro.github.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

interface CoroutineScopeProvider {
    fun getScope(viewModel: ViewModel): CoroutineScope
}

class DefaultCoroutineScopeProvider
    @Inject
    constructor() : CoroutineScopeProvider {
        override fun getScope(viewModel: ViewModel): CoroutineScope = viewModel.viewModelScope
    }
