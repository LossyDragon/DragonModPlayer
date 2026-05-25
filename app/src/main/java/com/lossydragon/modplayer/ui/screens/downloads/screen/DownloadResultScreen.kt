package com.lossydragon.modplayer.ui.screens.downloads.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lossydragon.modplayer.model.ArtistResult
import com.lossydragon.modplayer.model.DownloadSearchState
import com.lossydragon.modplayer.model.Item
import com.lossydragon.modplayer.model.Items
import com.lossydragon.modplayer.model.Module
import com.lossydragon.modplayer.model.SearchListResult
import com.lossydragon.modplayer.model.SearchResult
import com.lossydragon.modplayer.model.SearchType
import com.lossydragon.modplayer.model.Sponsor
import com.lossydragon.modplayer.ui.components.MessageBox
import com.lossydragon.modplayer.ui.screens.downloads.components.ArtistListItem
import com.lossydragon.modplayer.ui.screens.downloads.components.ModuleListItem
import com.lossydragon.modplayer.ui.screens.downloads.viewmodel.DownloadViewModel
import com.lossydragon.modplayer.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun DownloadResultScreen(
    modifier: Modifier = Modifier,
    searchType: SearchType,
    query: String,
    onBack: () -> Unit,
    onModuleClick: (Int) -> Unit

) {
    val viewModel = koinViewModel<DownloadViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (searchType == SearchType.ARTIST) {
            viewModel.searchArtist(query)
        } else {
            viewModel.searchFileOrTitle(query)
        }
    }

    DownloadScreenContent(
        modifier = modifier,
        state = state,
        onBack = onBack,
        onModuleClick = onModuleClick,
        onArtistClick = viewModel::getArtistById,
    )
}

@Composable
private fun DownloadScreenContent(
    modifier: Modifier = Modifier,
    state: DownloadSearchState,
    onBack: () -> Unit,
    onModuleClick: (Int) -> Unit,
    onArtistClick: (Int) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.title.ifBlank { "Results" },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    )
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            state.error?.let {
                MessageBox(
                    text = it,
                    actions = {
                        TextButton(onClick = onBack, content = { Text(text = "Go Back") })
                    }
                )
            }

            if (!state.isLoading && state.error == null) {
                when (val result = state.result) {
                    is SearchResult.Modules -> if (result.data.module.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            content = {
                                items(
                                    items = result.data.module,
                                    itemContent = { module ->
                                        ModuleListItem(
                                            module = module,
                                            onClick = { onModuleClick(module.id) }
                                        )
                                    }
                                )
                            }
                        )
                    } else {
                        MessageBox(
                            text = "No modules found.",
                            actions = {
                                TextButton(onClick = onBack, content = { Text(text = "Go Back") })
                            }
                        )
                    }

                    is SearchResult.Artists -> if (result.data.items.item.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            content = {
                                items(
                                    items = result.data.listItems,
                                    itemContent = { artist ->
                                        ArtistListItem(
                                            alias = artist.alias,
                                            onClick = { onArtistClick(artist.id) }
                                        )
                                    }
                                )
                            }
                        )
                    } else {
                        MessageBox(
                            text = "No artists found.",
                            actions = {
                                TextButton(onClick = onBack, content = { Text(text = "Go Back") })
                            }
                        )
                    }

                    null -> Unit
                }
            }
        }
    }
}

private class DownloadPreviewParameter : PreviewParameterProvider<DownloadSearchState> {
    override val values = sequenceOf(
        DownloadSearchState(isLoading = true),
        DownloadSearchState(result = SearchResult.Modules(data = SearchListResult())),
        DownloadSearchState(result = SearchResult.Artists(data = ArtistResult())),
        DownloadSearchState(
            error = "Modules Error",
            title = "Modules",
            result = SearchResult.Modules(data = SearchListResult()),
        ),
        DownloadSearchState(
            error = "Artists Error",
            title = "Artists",
            result = SearchResult.Artists(data = ArtistResult()),
        ),
        DownloadSearchState(
            title = "Modules",
            result = SearchResult.Modules(
                data = SearchListResult(module = Array(10) { Module() }.toList())
            ),
        ),
        DownloadSearchState(
            title = "Artists",
            result = SearchResult.Artists(
                data = ArtistResult(
                    sponsor = Sponsor(),
                    items = Items(item = Array(10) { Item() }.toList()),
                )
            ),
        ),
    )
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(DownloadPreviewParameter::class) state: DownloadSearchState
) {
    AppTheme {
        DownloadScreenContent(
            state = state,
            onBack = {},
            onModuleClick = {},
            onArtistClick = {},
        )
    }
}
