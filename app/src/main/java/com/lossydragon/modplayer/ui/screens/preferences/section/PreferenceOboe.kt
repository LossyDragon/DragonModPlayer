package com.lossydragon.modplayer.ui.screens.preferences.section

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alorma.compose.settings.ui.expressive.SettingsMenuLink
import com.lossydragon.modplayer.R
import com.lossydragon.modplayer.db.AppPreferences
import com.lossydragon.modplayer.ui.screens.preferences.components.PreferenceItem
import com.lossydragon.modplayer.ui.screens.preferences.components.PreferenceSection
import com.lossydragon.modplayer.ui.screens.preferences.components.SingleChoiceAlertDialog
import com.lossydragon.modplayer.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.helllabs.libxmp.Xmp
import org.koin.compose.koinInject

private fun Int.toPerfModeKey() = when (this) {
    Xmp.OBOE_PERFMODE_NONE -> "none"
    Xmp.OBOE_PERFMODE_POWERSAVING -> "powersaving"
    else -> "lowlatency"
}

private fun String.toPerfModeInt() = when (this) {
    "none" -> Xmp.OBOE_PERFMODE_NONE
    "powersaving" -> Xmp.OBOE_PERFMODE_POWERSAVING
    else -> Xmp.OBOE_PERFMODE_LOWLATENCY
}

private fun Int.toAudioApiKey() = when (this) {
    Xmp.OBOE_AUDIO_API_AAUDIO -> "aaudio"
    Xmp.OBOE_AUDIO_API_OPENSLES -> "opensles"
    else -> "unspecified"
}

private fun String.toAudioApiInt() = when (this) {
    "aaudio" -> Xmp.OBOE_AUDIO_API_AAUDIO
    "opensles" -> Xmp.OBOE_AUDIO_API_OPENSLES
    else -> Xmp.OBOE_AUDIO_API_UNSPECIFIED
}

// TODO localize
private val perfModeOptions = persistentListOf(
    PreferenceItem(
        key = "lowlatency",
        title = "Low Latency (Default)",
        description = "Minimizes audio latency. Best for real-time playback but uses more battery."
    ),
    PreferenceItem(
        key = "none",
        title = "None",
        description = "No particular performance target. Balanced between latency and battery."
    ),
    PreferenceItem(
        key = "powersaving",
        title = "Power Saving",
        description = "Prioritizes battery life over latency. May introduce audio buffering."
    ),
)

// TODO localize
private val apiOptions = persistentListOf(
    PreferenceItem(
        key = "unspecified",
        title = "Unspecified (Default)",
        description = "Lets Oboe choose the best available API. Recommended for most devices."
    ),
    PreferenceItem(
        key = "aaudio",
        title = "AAudio",
        description = "Modern low-latency Android audio API. Available on Android 8.0+ (API 26+)."
    ),
    PreferenceItem(
        key = "opensles",
        title = "OpenSL ES",
        description = "Legacy audio API. Deprecated in Android 13 but may help on older or problematic devices."
    ),
)

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

    val perfMode by prefs.getOboePerfModeFlow()
        .collectAsStateWithLifecycle(initialValue = Xmp.OBOE_PERFMODE_LOWLATENCY)
    val audioApi by prefs.getOboeAudioApiFlow()
        .collectAsStateWithLifecycle(initialValue = Xmp.OBOE_AUDIO_API_UNSPECIFIED)

    var isPerfModeShowing by remember { mutableStateOf(false) }
    if (isPerfModeShowing) {
        SingleChoiceAlertDialog(
            selectedItemKey = perfMode.toPerfModeKey(),
            items = perfModeOptions,
            onItemSelected = { key ->
                isPerfModeShowing = false
                key?.let { scope.launch { prefs.setOboePerfMode(it.toPerfModeInt()) } }
            },
        )
    }

    var isAudioApiShowing by remember { mutableStateOf(false) }
    if (isAudioApiShowing) {
        SingleChoiceAlertDialog(
            selectedItemKey = audioApi.toAudioApiKey(),
            items = apiOptions,
            onItemSelected = { key ->
                isAudioApiShowing = false
                key?.let { scope.launch { prefs.setOboeAudioApi(it.toAudioApiInt()) } }
            },
        )
    }

    PreferenceSection(
        title = {
            Text(
                text = stringResource(R.string.pref_title_oboe),
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        verticalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            SettingsMenuLink(
                onClick = { isPerfModeShowing = true },
                title = { Text(text = stringResource(R.string.pref_performance_mode)) },
                subtitle = { Text(text = stringResource(R.string.pref_performance_mode_desc)) },
                action = {
                    Text(
                        text = perfModeOptions.find { it.key == perfMode.toPerfModeKey() }!!.title
                    )
                },
                colors = colors,
                shapes = ListItemDefaults.segmentedShapes(0, 3),
            )
            SettingsMenuLink(
                onClick = { isAudioApiShowing = true },
                title = { Text(text = stringResource(R.string.pref_audio_api)) },
                subtitle = { Text(text = stringResource(R.string.pref_audio_api_desc)) },
                action = {
                    Text(text = apiOptions.find { it.key == audioApi.toAudioApiKey() }!!.title)
                },
                colors = colors,
                shapes = ListItemDefaults.segmentedShapes(2, 3),
            )
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
