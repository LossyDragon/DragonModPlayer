package com.lossydragon.modplayer.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lossydragon.modplayer.R

@Composable
fun BackButton(onBack: () -> Unit) {
    IconButton(
        onClick = onBack,
        content = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.desc_back_button)
            )
        }
    )
}
