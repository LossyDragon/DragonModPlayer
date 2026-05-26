package com.lossydragon.modplayer.ui.preferences.section

import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.lossydragon.modplayer.ui.preferences.components.PreferenceSection

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PreferenceInterface(
    colors: ListItemColors
) {
    PreferenceSection(
        title = {
            Text(
                text = "Interface",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        content = {
            Text(text = "TODO")
            // SettingsMenuLink(
            //     title = { Text(text = "Sample Rate") },
            //     subtitle = { Text(text = "44100") },
            //     colors = colors,
            //     shapes = ListItemDefaults.segmentedShapes(0, 1),
            //     onClick = { /* TODO */ }
            // )
        }
    )
}
