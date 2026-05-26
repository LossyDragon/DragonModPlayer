package com.lossydragon.modplayer.ui.preferences

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import com.lossydragon.modplayer.di.appModule
import com.lossydragon.modplayer.ui.components.BackButton
import com.lossydragon.modplayer.ui.preferences.section.PreferenceInfo
import com.lossydragon.modplayer.ui.preferences.section.PreferenceInterface
import com.lossydragon.modplayer.ui.preferences.section.PreferencePlayer
import com.lossydragon.modplayer.ui.theme.AppTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun PreferencesScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onFormats: () -> Unit,
    onAbout: () -> Unit
) {
    var isShowingResetDialog by remember { mutableStateOf(false) }

    if (isShowingResetDialog) {
        TODO()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Settings") },
                navigationIcon = { BackButton(onBack = onBack) },
                actions = {
                    IconButton(
                        onClick = { isShowingResetDialog = true },
                        content = {
                            Icon(
                                imageVector = Icons.Default.RestartAlt,
                                contentDescription = null
                            )
                        }
                    )
                }
            )
        },
        content = { paddingValues ->
            val scrollState = rememberScrollState()
            val colors = ListItemDefaults.segmentedColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            )

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                content = {
                    PreferenceInterface(colors)
                    PreferencePlayer(colors)
                    PreferenceInfo(
                        colors = colors,
                        onFormats = onFormats,
                        onAbout = onAbout
                    )
                }
            )
        }
    )
}

@Preview
@Composable
private fun Preview() {
    val context = LocalContext.current
    startKoin {
        androidContext(context)
        modules(appModule)
    }
    AppTheme {
        PreferencesScreen(
            onBack = {},
            onAbout = {},
            onFormats = {},
        )
    }
}
