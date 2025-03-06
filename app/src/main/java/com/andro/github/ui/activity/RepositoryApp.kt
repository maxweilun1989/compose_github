package com.andro.github.ui.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.andro.github.app.AppConfig
import com.andro.github.data.GitHubUser
import com.andro.github.data.Repository
import com.andro.github.ui.screens.LoginScreen
import com.andro.github.ui.screens.ProfileScreen
import com.andro.github.ui.screens.RepoListErrorScreen
import com.andro.github.ui.screens.RepoListLoadingScreen
import com.andro.github.ui.screens.RepositoryDetailScreen
import com.andro.github.ui.screens.RepositoryListScreen
import com.andro.github.ui.viewmodel.GitHubViewModel
import com.andro.github.ui.viewmodel.RepositoryListUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun RepositoryApp(
    viewModel: GitHubViewModel,
    onOAuthLogin: () -> Unit,
    onLogin: (String, String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val language by viewModel.language.collectAsState()
    val user by viewModel.githubUser.collectAsState()
    val currentUserRepos by viewModel.currentUserRepos.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val drawerContent: @Composable () -> Unit = {
        DrawerContent(
            navController = navController,
            user = user,
            closeDrawer = { scope.launch { drawerState.close() } },
            onLogoutClick = { viewModel.logout() },
            onProfilePageShow = { viewModel.fetchOwnRepos() },
        )
    }

    LaunchedEffect(Unit) {
        viewModel.fetchLoginState()
        viewModel.fetchRepositories()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = drawerContent,
        scrimColor = Color.Transparent,
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(AppConfig.TITLE) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                )
            },
        ) { padding ->
            AppNavigation(
                navController,
                uiState,
                viewModel,
                language,
                user,
                currentUserRepos,
                padding,
                onOAuthLogin,
                onLogin,
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun AppNavigation(
    navController: NavHostController,
    uiState: RepositoryListUiState<List<Repository>>,
    viewModel: GitHubViewModel,
    language: String,
    user: GitHubUser?,
    currentUserRepos: List<Repository>,
    padding: PaddingValues,
    onOAuthLogin: () -> Unit,
    onLogin: (String, String) -> Unit,
) {
    NavHost(
        modifier = Modifier.padding(padding),
        navController = navController,
        startDestination = AppConfig.ROUTER_LOADING,
    ) {
        composable(AppConfig.ROUTER_LOADING) {
            RepoListLoadingScreen()
        }

        composable(AppConfig.ROUTER_ERROR) {
            val error = uiState as? RepositoryListUiState.Error
            if (error != null) {
                RepoListErrorScreen(
                    message = error.message,
                    onRetry = { viewModel.fetchRepositories() },
                )
            }
        }

        composable(AppConfig.ROUTER_LOGIN) {
            LoginScreen(onLogin) {
                navController.popBackStack()
                onOAuthLogin()
            }
        }

        composable(AppConfig.ROUTER_PROFILE) {
            ProfileScreen(user, currentUserRepos) { user, repo, title, content ->
                viewModel.raiseIssue(user, repo, title, content)
            }
        }

        composable(AppConfig.ROUTE_REPOSITORY_LIST) {
            val success = uiState as? RepositoryListUiState.Success
            if (success != null) {
                RepositoryListScreen(
                    repositories = viewModel.repositories(),
                    language = language,
                    listState = viewModel.llState,
                    viewModel.isDescending.value,
                    onLanguageChange = { newLanguage ->
                        viewModel.setLanguage(newLanguage)
                    },
                    onRepositoryClick = { repository ->
                        navController.navigate("${AppConfig.ROUTER_REPOSITORY_DETAIL}/${repository.id}")
                    },
                    onSortClick = { viewModel.toggleSortOrder() },
                )
            }
        }

        composable(
            route = "${AppConfig.ROUTER_REPOSITORY_DETAIL}/{repositoryId}",
        ) { backStackEntry ->
            val repositoryId = backStackEntry.arguments?.getString("repositoryId")
            val repos = (uiState as RepositoryListUiState.Success).data
            val repository = repos.find { it.id.toString() == repositoryId }
            if (repository != null) {
                RepositoryDetailScreen(repository = repository)
            }
        }
    }

    if (navController.currentDestination?.route in
        listOf(
            AppConfig.ROUTER_LOADING,
            AppConfig.ROUTER_ERROR,
            AppConfig.ROUTE_REPOSITORY_LIST,
        )
    ) {
        val destination =
            when (uiState) {
                is RepositoryListUiState.Error -> AppConfig.ROUTER_ERROR
                RepositoryListUiState.Loading -> AppConfig.ROUTER_LOADING
                is RepositoryListUiState.Success -> AppConfig.ROUTE_REPOSITORY_LIST
            }
        navController.navigate(destination) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun DrawerContent(
    navController: NavController,
    user: GitHubUser?,
    closeDrawer: () -> Unit,
    onLogoutClick: () -> Unit,
    onProfilePageShow: () -> Unit,
) {
    val backgroundColor = MaterialTheme.colorScheme.primary

    Box(
        modifier =
            Modifier
                .fillMaxHeight()
                .width(280.dp)
                .background(backgroundColor),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            if (user != null) {
                                return@clickable
                            }
                            closeDrawer()
                            navController.navigate(AppConfig.ROUTER_LOGIN)
                        },
                contentAlignment = Alignment.Center,
            ) {
                if (user != null) {
                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = "User Avatar",
                        modifier = Modifier.size(80.dp),
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Default Avatar",
                        tint = Color.White,
                        modifier = Modifier.size(80.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = user?.login ?: "点击头像登录",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(32.dp))
            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                thickness = 1.dp,
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (user != null) {
                DrawerOption(
                    icon = Icons.Default.Info,
                    label = "关于",
                    onClick = {
                        closeDrawer()
                        if (navController.currentDestination?.route == AppConfig.ROUTER_PROFILE) {
                            return@DrawerOption
                        }
                        navController.navigate(AppConfig.ROUTER_PROFILE)
                        onProfilePageShow()
                    },
                )

                DrawerOption(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    label = "退出",
                    onClick = {
                        onLogoutClick()
                    },
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun DrawerOption(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
