package com.lossydragon.modplayer.ui.screens.browser.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lossydragon.modplayer.R
import com.lossydragon.modplayer.ui.theme.AppTheme

@Composable
internal fun EmptyPrompt(padding: PaddingValues, onPick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(32.dp),
        contentAlignment = Alignment.Center,
        content = {
            Card(
                shape = MaterialTheme.shapes.small,
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    content = {
                        IconButton(
                            onClick = onPick,
                            modifier = Modifier.size(88.dp),
                            content = {
                                Icon(
                                    imageVector = Icons.Default.FolderOpen,
                                    contentDescription = stringResource(R.string.desc_folder_pick),
                                    modifier = Modifier.size(72.dp),
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        )
                        Text(
                            text = stringResource(R.string.no_folder_selected),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 16.dp),
                        )
                        Text(
                            text = stringResource(R.string.no_folder_selected_message),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp),
                            textAlign = TextAlign.Center,
                        )
                    }
                )
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            EmptyPrompt(
                padding = PaddingValues(),
                onPick = {},
            )
        }
    }
}
