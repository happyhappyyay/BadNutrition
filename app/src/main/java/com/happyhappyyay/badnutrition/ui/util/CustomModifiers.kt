package com.happyhappyyay.badnutrition.ui.util

import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.zoom(
    onZoom: (zoom: Float) -> Unit
): Modifier {
    return this
        .pointerInput(Unit) {
            forEachGesture {
                awaitPointerEventScope {
                    awaitFirstDown(false)
                    do {
                        val event = awaitPointerEvent()
                        val zoom = event.calculateZoom()
                        if (zoom != 1F) {
                            onZoom(zoom)
                        }
                    } while (event.changes.any { it.pressed })
                }
            }
        }
}

fun Modifier.doubleTap(
    onDoubleTap: () -> Unit
): Modifier {
    return this
        .pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = {
                    onDoubleTap()
                },
            )
        }
}

fun Modifier.tapGestures(
    interactionSource: MutableInteractionSource,
    onTap: () -> Unit = {},
    onLongPress: () -> Unit = {},
    onDoubleTap: () -> Unit = {},
): Modifier {
    return this
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = { offset ->
                    val press = PressInteraction. Press(offset)
                    interactionSource.emit(press)
                    tryAwaitRelease()
                    interactionSource.emit(PressInteraction.Release(press))
                },
                onLongPress = {
                    onLongPress()
                },
                onDoubleTap = {
                    onDoubleTap()
                },
                onTap = {
                    onTap()
                }
            )
        }
}

fun Modifier.longPressDrag(
    onSelect: (Offset) -> Unit
): Modifier {
    return this
//            needs to cancel out detectTapGestures later on the modifier chain to override simultaneous long press
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(onDragStart = { offset -> onSelect(offset) }) { change, _ ->
                onSelect(Offset(change.position.x, change.position.y))
            }
        }
}