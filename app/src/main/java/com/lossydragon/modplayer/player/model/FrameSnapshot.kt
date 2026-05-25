package com.lossydragon.modplayer.player.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class FrameSnapshot(
    val position: Int,
    val pattern: Int,
    val row: Int,
    val numRows: Int,
    val speed: Int,
    val bpm: Int,
    val timeMs: Int,
    val totalTimeMs: Int,
    val channels: ImmutableList<ChannelSnapshot>
)
