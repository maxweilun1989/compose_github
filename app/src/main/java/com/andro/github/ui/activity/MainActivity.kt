package com.andro.github.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.andro.github.app.AppConfig
import com.andro.github.ui.theme.GithubTheme
import com.andro.github.ui.viewmodel.GitHubViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        const val TAG = "[MainActivity]"
    }

    private val viewModel: GitHubViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubTheme(dynamicColor = false) {
                Surface(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .systemBarsPadding(),
                ) {
                    RepositoryApp(viewModel, {
                        val authUri =
                            Uri.parse("${AppConfig.AUTH_URL}?client_id=${AppConfig.CLIENT_ID}&redirect_uri=${AppConfig.REDIRECT_URI}")
                        val customTabsIntent = CustomTabsIntent.Builder().build()
                        customTabsIntent.launchUrl(this, authUri)
                    }) { _, _ ->
                        Toast.makeText(this, "Not Supported!!!!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleGitHubCallback(intent)
    }

    override fun onResume() {
        super.onResume()
        handleGitHubCallback(intent)
    }

    private fun handleGitHubCallback(intent: Intent?) {
        val uri = intent?.data ?: return
        val code = uri.getQueryParameter("code") ?: return
        Log.d(TAG, "code: $code")
        viewModel.fetchGithubUserInfo(code)
    }
}
