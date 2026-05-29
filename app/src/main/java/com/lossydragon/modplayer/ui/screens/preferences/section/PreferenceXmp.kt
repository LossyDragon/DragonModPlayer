package com.lossydragon.modplayer.ui.screens.preferences.section

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alorma.compose.settings.ui.expressive.SettingsMenuLink
import com.alorma.compose.settings.ui.expressive.SettingsSwitch
import com.lossydragon.modplayer.R
import com.lossydragon.modplayer.db.AppPreferences
import com.lossydragon.modplayer.ui.screens.preferences.components.FlagItem
import com.lossydragon.modplayer.ui.screens.preferences.components.MultiChoiceAlertDialog
import com.lossydragon.modplayer.ui.screens.preferences.components.PreferenceItem
import com.lossydragon.modplayer.ui.screens.preferences.components.PreferenceSection
import com.lossydragon.modplayer.ui.screens.preferences.components.SettingsSlider
import com.lossydragon.modplayer.ui.screens.preferences.components.SingleChoiceAlertDialog
import com.lossydragon.modplayer.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.helllabs.libxmp.Xmp
import org.koin.compose.koinInject

// TODO localize
private fun formatFlagsLabel(flags: Int): String {
    val parts = buildList {
        if (flags and Xmp.XMP_FORMAT_8BIT != 0) {
            add("8-bit")
        } else if (flags and Xmp.XMP_FORMAT_32BIT != 0) {
            add("32-bit")
        } else {
            add("16-bit")
        }
        if (flags and Xmp.XMP_FORMAT_UNSIGNED != 0) add("unsigned")
        if (flags and Xmp.XMP_FORMAT_MONO != 0) {
            add("mono")
        } else {
            add("stereo")
        }
    }
    return parts.joinToString(", ")
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PreferenceXmp(
    colors: ListItemColors
) {
    val scope = rememberCoroutineScope()
    val prefs = if (LocalView.current.isInEditMode) {
        AppPreferences(LocalContext.current)
    } else {
        koinInject<AppPreferences>()
    }

    // TODO localize
    val sampleRateOptions = remember {
        persistentListOf(
            PreferenceItem(key = "8000", title = "8000 Hz", description = "Telephone quality"),
            PreferenceItem(key = "22050", title = "22050 Hz", description = "Low quality"),
            PreferenceItem(key = "44100", title = "44100 Hz", description = "CD quality"),
            PreferenceItem(key = "48000", title = "48000 Hz", description = "DVD quality"),
        )
    }
    // TODO localize
    val flagItems = remember {
        persistentListOf(
            FlagItem(
                flag = Xmp.XMP_FLAGS_VBLANK,
                title = "VBlank timing",
                description = "Use vblank timing"
            ),
            FlagItem(
                flag = Xmp.XMP_FLAGS_FX9BUG,
                title = "FX9 bug",
                description = "Emulate Protracker 2.x FX9 bug"
            ),
            FlagItem(
                flag = Xmp.XMP_FLAGS_FIXLOOP,
                title = "Fix loop",
                description = "Halve sample loop values"
            ),
            FlagItem(
                flag = Xmp.XMP_FLAGS_A500,
                title = "Amiga 500 mixer",
                description = "Use Paula mixer for Amiga modules"
            ),
        )
    }

    // TODO localize
    val interpOptions = remember {
        persistentListOf(
            PreferenceItem(
                key = Xmp.XMP_INTERP_NEAREST.toString(),
                title = "Nearest",
                description = "Nearest neighbor — sharp, low CPU"
            ),
            PreferenceItem(
                key = Xmp.XMP_INTERP_LINEAR.toString(),
                title = "Linear",
                description = "Linear interpolation (default)"
            ),
            PreferenceItem(
                key = Xmp.XMP_INTERP_SPLINE.toString(),
                title = "Spline",
                description = "Cubic spline — smoothest, highest CPU"
            ),
        )
    }

    val sampleRate by prefs.getSampleRateFlow()
        .collectAsStateWithLifecycle(initialValue = Xmp.DEFAULT_SAMPLE_RATE)
    val formatFlags by prefs.getPlayerFormatFlow()
        .collectAsStateWithLifecycle(initialValue = 0)
    val bufferMs by prefs.getBufferMsFlow()
        .collectAsStateWithLifecycle(initialValue = Xmp.DEFAULT_BUFFER_MS)
    val flags by prefs.getPlayerFlagsFlow()
        .collectAsStateWithLifecycle(initialValue = 0)
    val pan by prefs.getDefaultPanFlow()
        .collectAsStateWithLifecycle(Xmp.DEFAULT_PAN_SEPARATION)
    val mix by prefs.getStereoMixFlow()
        .collectAsStateWithLifecycle(Xmp.DEFAULT_STEREO_MIX)
    val dspEffect by prefs.getDspEffectFlow()
        .collectAsStateWithLifecycle(initialValue = Xmp.XMP_DSP_LOWPASS)
    val interp by prefs.getInterpolationTypeFlow()
        .collectAsStateWithLifecycle(initialValue = Xmp.DEFAULT_INTERPOLATION)
    val volume by prefs.getPlayerVolumeFlow()
        .collectAsStateWithLifecycle(initialValue = Xmp.DEFAULT_PLAYER_VOLUME)
    val boost by prefs.getVolumeBoostFlow()
        .collectAsStateWithLifecycle(initialValue = Xmp.DEFAULT_VOLUME_BOOST)

    var isSampleRateShowing by remember { mutableStateOf(false) }
    if (isSampleRateShowing) {
        SingleChoiceAlertDialog(
            selectedItemKey = sampleRate.toString(),
            items = sampleRateOptions,
            onItemSelected = { key ->
                key?.toIntOrNull()?.let { scope.launch { prefs.setSampleRate(it) } }
                isSampleRateShowing = false
            },
        )
    }

    var isFlagsShowing by remember { mutableStateOf(false) }
    if (isFlagsShowing) {
        MultiChoiceAlertDialog(
            currentFlags = flags,
            items = flagItems,
            onConfirm = { newFlags ->
                scope.launch { prefs.setPlayerFlags(newFlags) }
                isFlagsShowing = false
            },
            onDismiss = { isFlagsShowing = false }
        )
    }

    var isInterpShowing by remember { mutableStateOf(false) }
    if (isInterpShowing) {
        SingleChoiceAlertDialog(
            selectedItemKey = interp.toString(),
            items = interpOptions,
            onItemSelected = { key ->
                key?.toIntOrNull()?.let { scope.launch { prefs.setInterpolationType(it) } }
                isInterpShowing = false
            },
        )
    }

    var showFormatDialog by remember { mutableStateOf(false) }
    if (showFormatDialog) {
        // TODO localize
        MultiChoiceAlertDialog(
            currentFlags = formatFlags,
            items = persistentListOf(
                FlagItem(Xmp.XMP_FORMAT_8BIT, "8-bit output", "Takes precedence over 32-bit"),
                FlagItem(Xmp.XMP_FORMAT_UNSIGNED, "Unsigned samples", "Mix to unsigned samples"),
                FlagItem(Xmp.XMP_FORMAT_MONO, "Mono output", "Mix to mono instead of stereo"),
                FlagItem(Xmp.XMP_FORMAT_32BIT, "32-bit output", "Ignored if 8-bit is enabled"),
            ),
            onConfirm = { newFlags ->
                val sanitized = if ((newFlags and Xmp.XMP_FORMAT_8BIT) != 0) {
                    // 8-bit wins, clear 32-bit
                    newFlags and Xmp.XMP_FORMAT_32BIT.inv()
                } else {
                    newFlags
                }
                scope.launch { prefs.setPlayerFormat(sanitized) }
                showFormatDialog = false
            },
            onDismiss = { showFormatDialog = false },
        )
    }

    PreferenceSection(
        title = {
            Text(
                text = stringResource(R.string.pref_title_libxmp),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        verticalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            SettingsMenuLink(
                title = { Text(text = stringResource(R.string.pref_sample_rate)) },
                subtitle = { Text(text = stringResource(R.string.pref_sample_rate_desc)) },
                action = { Text(text = stringResource(R.string.size_hz, sampleRate)) },
                colors = colors,
                shapes = ListItemDefaults.segmentedShapes(0, 10),
                onClick = { isSampleRateShowing = true }
            )
            SettingsMenuLink(
                title = { Text(text = stringResource(R.string.pref_output_format)) },
                subtitle = { Text(text = stringResource(R.string.pref_output_format_desc)) },
                action = { Text(formatFlagsLabel(formatFlags)) },
                colors = colors,
                shapes = ListItemDefaults.segmentedShapes(1, 10),
                onClick = { showFormatDialog = true },
            )
            SettingsMenuLink(
                title = { Text(text = stringResource(R.string.pref_interpolation_type)) },
                subtitle = { Text(text = stringResource(R.string.pref_interpolation_type_desc)) },
                action = {
                    val text = when (interp) {
                        Xmp.XMP_INTERP_NEAREST -> stringResource(R.string.interp_nearest)
                        Xmp.XMP_INTERP_LINEAR -> stringResource(R.string.interp_linear)
                        Xmp.XMP_INTERP_SPLINE -> stringResource(R.string.interp_spline)
                        else -> stringResource(R.string.unknown)
                    }
                    Text(text = text)
                },
                colors = colors,
                shapes = ListItemDefaults.segmentedShapes(2, 10),
                onClick = { isInterpShowing = true }
            )
            SettingsMenuLink(
                title = { Text(text = stringResource(R.string.pref_flags)) },
                subtitle = { Text(text = stringResource(R.string.pref_flags_desc)) },
                action = {
                    val count = flagItems.count { (flags and it.flag) != 0 }
                    val text = if (count == 0) {
                        stringResource(R.string.none)
                    } else {
                        stringResource(R.string.pref_flags_enabled)
                    }
                    Text(text = text)
                },
                colors = colors,
                shapes = ListItemDefaults.segmentedShapes(3, 10),
                onClick = { isFlagsShowing = true }
            )
            SettingsSwitch(
                title = { Text(text = stringResource(R.string.pref_dsp)) },
                subtitle = { Text(text = stringResource(R.string.pref_dsp_desc)) },
                state = dspEffect != Xmp.XMP_DSP_NONE,
                onCheckedChange = { enabled ->
                    scope.launch {
                        val value = if (enabled) Xmp.XMP_DSP_LOWPASS else Xmp.XMP_DSP_NONE
                        prefs.setDspEffect(value)
                    }
                },
                colors = colors,
                shapes = ListItemDefaults.segmentedShapes(4, 10),
            )
            SettingsSlider(
                title = { Text(text = stringResource(R.string.pref_buffer_ms)) },
                subtitle = { Text(text = stringResource(R.string.pref_buffer_ms_desc)) },
                action = { Text(text = stringResource(R.string.pref_buffer_ms_count, bufferMs)) },
                colors = colors,
                steps = ((Xmp.MAX_BUFFER_MS - Xmp.MIN_BUFFER_MS) / 40) - 1, // = 22
                valueRange = Xmp.MIN_BUFFER_MS.toFloat()..Xmp.MAX_BUFFER_MS.toFloat(),
                value = bufferMs.toFloat(),
                onValueChange = { scope.launch { prefs.setBufferMs(it.toInt()) } },
                shapes = ListItemDefaults.segmentedShapes(5, 10),
            )
            SettingsSlider(
                title = { Text(text = stringResource(R.string.pref_volume)) },
                subtitle = { Text(text = stringResource(R.string.pref_volume_desc)) },
                action = { Text(text = stringResource(R.string.value_percent, volume)) },
                colors = colors,
                steps = 0,
                valueRange = 0f..100f,
                value = volume.toFloat(),
                onValueChange = { scope.launch { prefs.setPlayerVolume(it.toInt()) } },
                shapes = ListItemDefaults.segmentedShapes(6, 10),
            )
            SettingsSlider(
                title = { Text(text = stringResource(R.string.pref_boost)) },
                subtitle = { Text(text = stringResource(R.string.pref_boost_desc)) },
                action = { Text(text = stringResource(R.string.value_x, boost)) },
                colors = colors,
                steps = 2,
                valueRange = 0f..3f,
                value = boost.toFloat(),
                onValueChange = { scope.launch { prefs.setVolumeBoost(it.toInt()) } },
                shapes = ListItemDefaults.segmentedShapes(7, 10),
            )
            SettingsSlider(
                title = { Text(text = stringResource(R.string.pref_stereo_mix)) },
                subtitle = { Text(text = stringResource(R.string.pref_stereo_mix_desc)) },
                action = { Text(text = stringResource(R.string.value_percent, mix)) },
                colors = colors,
                steps = 0,
                valueRange = -100f..100f,
                value = mix.toFloat(),
                onValueChange = { scope.launch { prefs.setStereoMix(it.toInt()) } },
                shapes = ListItemDefaults.segmentedShapes(8, 10),
            )
            SettingsSlider(
                title = { Text(text = stringResource(R.string.pref_pan)) },
                subtitle = { Text(text = stringResource(R.string.pref_pan_desc),) },
                action = { Text(text = stringResource(R.string.value_percent, pan)) },
                colors = colors,
                steps = 4,
                valueRange = 0f..100f,
                value = pan.toFloat(),
                onValueChange = { scope.launch { prefs.setDefaultPan(it.toInt()) } },
                shapes = ListItemDefaults.segmentedShapes(9, 10),
            )
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
private fun Preview() {
    AppTheme {
        val colors = ListItemDefaults.segmentedColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        )
        Surface {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                PreferenceXmp(colors)
            }
        }
    }
}
