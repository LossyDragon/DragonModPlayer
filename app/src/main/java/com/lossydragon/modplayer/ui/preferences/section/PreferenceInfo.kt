package com.lossydragon.modplayer.ui.preferences.section

import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.alorma.compose.settings.ui.expressive.SettingsMenuLink
import com.lossydragon.modplayer.ui.preferences.components.PreferenceSection

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PreferenceInfo(
    colors: ListItemColors,
    onFormats: () -> Unit,
    onAbout: () -> Unit
) {
    PreferenceSection(
        title = {
            Text(
                text = "About",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        content = {
            SettingsMenuLink(
                title = { Text(text = "Formats") },
                subtitle = { Text(text = "Supported Trackers libxmp can play") },
                colors = colors,
                shapes = ListItemDefaults.segmentedShapes(0, 2),
                onClick = onFormats
            )
            SettingsMenuLink(
                title = { Text(text = "About") },
                subtitle = { Text(text = "Version information and library credits") },
                colors = colors,
                shapes = ListItemDefaults.segmentedShapes(1, 2),
                onClick = onAbout
            )
        }
    )
}
