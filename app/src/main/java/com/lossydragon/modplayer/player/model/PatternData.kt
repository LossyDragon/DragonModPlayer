package com.lossydragon.modplayer.player.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

fun emptyPatternData() = PatternData(
    patternIndex = -1,
    numRows = 0,
    numChannels = 0,
    cells = persistentListOf(persistentListOf()),
)

data class PatternData(
    val patternIndex: Int,
    val numRows: Int,
    val numChannels: Int,
    val cells: ImmutableList<ImmutableList<NoteCell>>
)

data class NoteCell(
    val note: Int,
    val instrument: Int,
    val fxType: Int,
    val fxParam: Int
) {
    val isEmpty: Boolean
        get() = note == 0 && instrument == 0 && fxType < 0

    val noteStr: String
        get() = when (note) {
            0 -> "---"

            0x80 -> "==="

            0x81 -> "^^^"

            in 1..127 -> {
                val n = note - 1
                "${NOTE_NAMES[n % 12]}${n / 12}"
            }

            else -> "???"
        }

    val instrumentStr: String
        get() = if (instrument > 0) "%02X".format(instrument) else ".."

    val effectStr: String
        get() = if (fxType >= 0) "%c%02X".format(effectChar(fxType), fxParam) else "..."

    companion object {
        private val NOTE_NAMES = arrayOf(
            "C-", "C#", "D-", "D#", "E-", "F-",
            "F#", "G-", "G#", "A-", "A#", "B-",
        )

        private fun effectChar(effect: Int): Char = when (effect) {
            in 0..9 -> '0' + effect
            in 10..35 -> 'A' + (effect - 10)
            else -> '?'
        }
    }
}
