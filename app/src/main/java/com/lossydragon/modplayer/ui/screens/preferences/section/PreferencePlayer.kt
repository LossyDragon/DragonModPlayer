package com.lossydragon.modplayer.ui.screens.preferences.section

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alorma.compose.settings.ui.expressive.SettingsSwitch
import com.lossydragon.modplayer.R
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
                text = stringResource(R.string.pref_title_player),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        verticalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            SettingsSwitch(
                title = { Text(text = stringResource(R.string.pref_auto_resume)) },
                subtitle = { Text(text = stringResource(R.string.pref_auto_resume_desc)) },
                state = autoResume,
                onCheckedChange = { scope.launch { prefs.setAutoResume(it) } },
                colors = colors,
                shapes = ListItemDefaults.segmentedShapes(0, 2),
            )
            SettingsSwitch(
                title = { Text(text = stringResource(R.string.pref_show_row_numbers)) },
                subtitle = { Text(text = stringResource(R.string.pref_show_row_numbers_desc)) },
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
