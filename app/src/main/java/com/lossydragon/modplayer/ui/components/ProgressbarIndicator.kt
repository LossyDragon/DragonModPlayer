package com.lossydragon.modplayer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.lossydragon.modplayer.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProgressbarIndicator(
    modifier: Modifier = Modifier,
    isLoading: Boolean = true
) {
    if (!isLoading) return

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            tonalElevation = 3.dp,
            content = {
                Box(
                    modifier = Modifier.padding(48.dp),
                    content = { LoadingIndicator(modifier = Modifier.size(56.dp)) }
                )
            }
        )
    }
}

@Preview
@Composable
private fun ProgressbarIndicatorPreview() {
    AppTheme {
        ProgressbarIndicator(
            modifier = Modifier.fillMaxSize(),
            isLoading = true
        )
    }
}
