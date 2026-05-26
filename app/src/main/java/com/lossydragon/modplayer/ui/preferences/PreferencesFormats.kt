package com.lossydragon.modplayer.ui.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lossydragon.modplayer.ui.components.BackButton
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun PreferencesFormats(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    formatList: ImmutableList<String>,
    onClick: (String) -> Unit
) {
    val listState = rememberLazyListState()

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Formats (${formatList.size})") },
                navigationIcon = { BackButton(onBack = onBack) }
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                state = listState,
                content = {
                    items(
                        items = formatList,
                        key = { it },
                        itemContent = { item ->
                            ListItem(
                                onClick = { onClick(item) },
                                shapes = ListItemDefaults.shapes(
                                    shape = MaterialTheme.shapes.small,
                                    focusedShape = MaterialTheme.shapes.small,
                                    pressedShape = MaterialTheme.shapes.small,
                                ),
                                content = { Text(text = item) }
                            )
                        }
                    )
                }
            )
        }
    )
}
