package com.lossydragon.modplayer.player.model

import androidx.compose.runtime.Immutable

@Immutable
data class ChannelSnapshot(
    val volume: Int,
    val finalVol: Int,
    val pan: Int,
    val instrument: Int,
    val note: Int,
    val period: Int
)
