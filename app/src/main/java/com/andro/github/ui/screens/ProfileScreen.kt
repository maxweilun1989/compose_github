package com.andro.github.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.andro.github.data.GitHubUser
import com.andro.github.data.Repository
import androidx.compose.runtime.remember as remember1

@Suppress("ktlint:standard:function-naming")
@Composable
fun ProfileScreen(
    user: GitHubUser?,
    repos: List<Repository>,
    onRaiseIssue: (user: GitHubUser?, repo: String, title: String, content: String) -> Unit,
) {
    if (user == null) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "User not found",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
            )
        }
        return
    }
    val (showDialog, setShowDialog) = remember1 { mutableStateOf(false) }
    val (dialogRepoName, setDialogRepoName) = remember1 { mutableStateOf("") }
    val (dialogTitle, setDialogTitle) = remember1 { mutableStateOf("") }
    val (dialogContent, setDialogContent) = remember1 { mutableStateOf("") }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(user.avatarUrl)
                    .crossfade(true)
                    .build(),
            contentDescription = "User Avatar",
            modifier =
                Modifier
                    .size(120.dp)
                    .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = user.name ?: "No Name",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
        )

        Text(
            text = "@${user.login}",
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!user.bio.isNullOrEmpty()) {
            Text(
                text = user.bio,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        InfoCard(label = "Email", value = user.email ?: "Not Available")
        Spacer(modifier = Modifier.height(8.dp))
        InfoCard(label = "Location", value = user.location ?: "Not Available")
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(repos) { repo ->
                RepoCard(repo = repo) { repo ->
                    setDialogRepoName(repo.name)
                    setShowDialog(true)
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { setShowDialog(false) },
                title = {
                    Text(text = "Raise Issue - $dialogRepoName")
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = dialogTitle,
                            onValueChange = { setDialogTitle(it) },
                            label = { Text("Title") },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = dialogContent,
                            onValueChange = { setDialogContent(it) },
                            label = { Text("Content") },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onRaiseIssue(user, dialogRepoName, dialogTitle, dialogContent)
                            setShowDialog(false)
                            setDialogContent("")
                            setDialogTitle("")
                        },
                    ) {
                        Text("Submit")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            setShowDialog(false)
                            setDialogContent("")
                            setDialogTitle("")
                        },
                    ) {
                        Text("Cancel")
                    }
                },
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun InfoCard(
    label: String,
    value: String,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(60.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f),
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(2f),
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun RepoCard(
    repo: Repository,
    onRaiseClick: (Repository) -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = repo.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                )
                if (!repo.description.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = repo.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Button(
                onClick = { onRaiseClick(repo) },
                modifier =
                    Modifier
                        .padding(start = 16.dp),
            ) {
                Text(text = "Raise")
            }
        }
    }
}
