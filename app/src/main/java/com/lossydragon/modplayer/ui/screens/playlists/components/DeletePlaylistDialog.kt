package com.lossydragon.modplayer.ui.screens.playlists.components

import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.*
import com.lossydragon.modplayer.R
import com.lossydragon.modplayer.db.entity.PlaylistEntity
import com.lossydragon.modplayer.ui.theme.AppTheme

@Composable
internal fun DeletePlaylistDialog(
    playlist: PlaylistEntity,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(imageVector = Icons.Default.DeleteForever, contentDescription = null)
        },
        title = { Text(text = stringResource(R.string.dialog_title_delete_playlist)) },
        text = {
            Text(text = stringResource(R.string.dialog_message_delete_playlist, playlist.name))
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                },
                content = { Text(text = stringResource(R.string.delete)) }
            )
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                content = { Text(text = stringResource(R.string.cancel)) }
            )
        }
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        DeletePlaylistDialog(
            playlist = PlaylistEntity(name = "Amiga Tunes"),
            onDismiss = {},
            onConfirm = {}
        )
    }
}
