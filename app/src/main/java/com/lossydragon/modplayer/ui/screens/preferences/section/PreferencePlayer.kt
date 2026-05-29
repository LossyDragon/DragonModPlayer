package com.lossydragon.modplayer.ui.screens.preferences.section

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alorma.compose.settings.ui.expressive.SettingsSwitch
import com.lossydragon.modplayer.db.AppPreferences
import com.lossydragon.modplayer.ui.screens.preferences.components.PreferenceSection
import com.lossydragon.modplayer.ui.theme.AppTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PreferencePlayer(
    colors: ListItemColors
) {
    val scope = rememberCoroutineScope()
    val prefs = if (LocalView.current.isInEditMode) {
        AppPreferences(LocalContext.current)
    } else {
        koinInject<AppPreferences>()
    }

    val autoResume by prefs.getAutoResumeFlow()
        .collectAsStateWithLifecycle(initialValue = false)
    val rowNumbers by prefs.getRowNumbersFlow()
        .collectAsStateWithLifecycle(initialValue = false)

    PreferenceSection(
        title = {
            Text(
                text = "Player",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        verticalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            SettingsSwitch(
                title = { Text("Auto-resume on startup") },
                subtitle = {
                    Text("Restore the last queue and continue playback when the app opens.")
                },
                state = autoResume,
                onCheckedChange = { scope.launch { prefs.setAutoResume(it) } },
                colors = colors,
                shapes = ListItemDefaults.segmentedShapes(0, 2),
            )
            SettingsSwitch(
                title = { Text("Show decimal row numbers") },
                subtitle = { Text("Show decimal row numbers instead of hex.") },
                state = rowNumbers,
                onCheckedChange = { scope.launch { prefs.setRowNumbers(it) } },
                colors = colors,
                shapes = ListItemDefaults.segmentedShapes(1, 2),
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
            PreferencePlayer(colors = colors)
        }
    }
}
