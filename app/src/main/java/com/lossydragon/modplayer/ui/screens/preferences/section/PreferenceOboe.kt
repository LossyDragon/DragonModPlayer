package com.lossydragon.modplayer.ui.screens.preferences.section

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lossydragon.modplayer.db.AppPreferences
import com.lossydragon.modplayer.ui.screens.preferences.components.PreferenceSection
import com.lossydragon.modplayer.ui.theme.AppTheme
import org.helllabs.libxmp.Xmp
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PreferenceOboe(
    colors: ListItemColors
) {
    val scope = rememberCoroutineScope()
    val prefs = if (LocalView.current.isInEditMode) {
        AppPreferences(LocalContext.current)
    } else {
        koinInject<AppPreferences>()
    }

    val perfMode = prefs.getOboePerfModeFlow()
        .collectAsStateWithLifecycle(initialValue = Xmp.OBOE_PERFMODE_LOWLATENCY)
    val channels = prefs.getOboeChannelsFlow()
        .collectAsStateWithLifecycle(initialValue = Xmp.OBOE_CHANNELS_STEREO)
    val audioApi = prefs.getOboeAudioApiFlow()
        .collectAsStateWithLifecycle(
            initialValue = Xmp.OBOE_AUDIO_API_UNSPECIFIED
        )

    var isPerfModeShowing by remember { mutableStateOf(false) }
    if (isPerfModeShowing) {
    }

    var isChannelsShowing by remember { mutableStateOf(false) }
    if (isChannelsShowing) {
    }

    var isAudioApiShowing by remember { mutableStateOf(false) }
    if (isAudioApiShowing) {
    }

    PreferenceSection(
        title = {
            Text(
                text = "Oboe (Audio)",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        verticalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            // SettingsSwitch(
            //     title = { Text("Auto-resume on startup") },
            //     subtitle = {
            //         Text("Restore the last queue and continue playback when the app opens.")
            //     },
            //     state = autoResume,
            //     onCheckedChange = { scope.launch { prefs.setAutoResume(it) } },
            //     colors = colors,
            //     shapes = ListItemDefaults.segmentedShapes(0, 11),
            // )
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            val colors = ListItemDefaults.segmentedColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            )
            PreferenceOboe(colors = colors)
        }
    }
}
