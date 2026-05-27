package com.lossydragon.modplayer.ui.screens.player.components

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.lossydragon.modplayer.player.model.NoteCell
import com.lossydragon.modplayer.player.model.PatternData
import com.lossydragon.modplayer.ui.theme.AppTheme
import kotlinx.collections.immutable.toImmutableList

// TODO this is kinda bad, very large GC's

@Composable
fun PatternView(
    pattern: PatternData,
    currentRow: Int,
    modifier: Modifier = Modifier
) {
    var zoom by remember { mutableFloatStateOf(1f) }
    var userOffset by remember { mutableStateOf(Offset.Zero) }

    val density = LocalDensity.current
    val baseRowHeight = with(density) { 14.sp.toPx() }
    val baseChannelWidth = with(density) { 88.dp.toPx() }
    val baseFontSize = 12.sp

    val rowHeight = baseRowHeight * zoom
    val channelWidth = baseChannelWidth * zoom
    val fontSize = baseFontSize * zoom

    val rowNumberWidth = with(density) { 32.dp.toPx() } * zoom

    val textMeasurer = rememberTextMeasurer()
    val textCache = remember { mutableMapOf<String, TextLayoutResult>() }

    val cellColor = MaterialTheme.colorScheme.onSurface
    val emptyColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
    val rowNumColor = MaterialTheme.colorScheme.onSurfaceVariant
    val beatColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    val currentRowColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
    val bgColor = MaterialTheme.colorScheme.surface

    val baseStyle = remember(fontSize, cellColor) {
        TextStyle(
            fontSize = fontSize,
            fontFamily = FontFamily.Monospace,
            color = cellColor,
        )
    }
    val emptyStyle = remember(fontSize, emptyColor) {
        baseStyle.copy(color = emptyColor)
    }
    val rowNumStyle = remember(fontSize, rowNumColor) {
        baseStyle.copy(color = rowNumColor)
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, gestureZoom, _ ->
                    zoom = (zoom * gestureZoom).coerceIn(0.5f, 3f)
                    userOffset += pan
                }
            }
    ) {
        if (pattern.numRows == 0 || pattern.numChannels == 0) return@Canvas

        drawRect(color = bgColor, size = size)

        // Auto-center the current row, allow user to pan within
        val targetY = currentRow * rowHeight - size.height / 2 + rowHeight / 2
        val scrollY = -targetY + userOffset.y

        val firstVisibleRow = ((-scrollY) / rowHeight).toInt()
            .coerceAtLeast(0)
        val lastVisibleRow = ((size.height - scrollY) / rowHeight).toInt()
            .coerceAtMost(pattern.numRows - 1)

        for (row in firstVisibleRow..lastVisibleRow) {
            val rowY = row * rowHeight + scrollY

            // Beat highlight
            if (row % 4 == 0) {
                drawRect(
                    color = beatColor,
                    topLeft = Offset(0f, rowY),
                    size = Size(size.width, rowHeight),
                )
            }

            // Current row
            if (row == currentRow) {
                drawRect(
                    color = currentRowColor,
                    topLeft = Offset(0f, rowY),
                    size = Size(size.width, rowHeight),
                )
            }

            // Row number
            drawCachedText(
                textMeasurer = textMeasurer,
                cache = textCache,
                text = "%02X".format(row),
                style = rowNumStyle,
                topLeft = Offset(4f, rowY),
            )

            // Channel cells
            for (ch in 0 until pattern.numChannels) {
                val cellX = rowNumberWidth + ch * channelWidth + userOffset.x

                if (cellX + channelWidth < 0 || cellX > size.width) continue

                val cell = pattern.cells[row][ch]
                val style = if (cell.isEmpty) emptyStyle else baseStyle
                val text = "${cell.noteStr} ${cell.instrumentStr} ${cell.effectStr}"

                drawCachedText(
                    textMeasurer = textMeasurer,
                    cache = textCache,
                    text = text,
                    style = style,
                    topLeft = Offset(cellX, rowY),
                )
            }
        }
    }
}

private fun DrawScope.drawCachedText(
    textMeasurer: TextMeasurer,
    cache: MutableMap<String, TextLayoutResult>,
    text: String,
    style: TextStyle,
    topLeft: Offset
) {
    val cacheKey = "$text|${style.fontSize}|${style.color}"
    val layout = cache.getOrPut(cacheKey) {
        textMeasurer.measure(text, style)
    }
    drawText(layout, topLeft = topLeft)
}

@Preview(showBackground = true, widthDp = 400, heightDp = 600)
@Composable
private fun PreviewPatternView() {
    val sample = PatternData(
        patternIndex = 0,
        numRows = 64,
        numChannels = 8,
        cells = List(64) { row ->
            List(8) { ch ->
                if (row % 2 == 0 && ch < 4) {
                    NoteCell(
                        note = 37 + (row / 4) % 12,
                        instrument = ch + 1,
                        fxType = if (ch == 0) 0 else -1,
                        fxParam = if (ch == 0) row else -1,
                    )
                } else {
                    NoteCell(0, 0, -1, -1)
                }
            }.toImmutableList()
        }.toImmutableList(),
    )

    AppTheme {
        PatternView(
            pattern = sample,
            currentRow = 12,
            modifier = Modifier.size(400.dp, 600.dp),
        )
    }
}
