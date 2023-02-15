package com.happyhappyyay.badnutrition.util

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun DimensionSubcomposeLayout(
    modifier: Modifier = Modifier,
    showMainContent: Boolean = false,
    mainContent: @Composable () -> Unit,
    dependentContent: @Composable (IntSize) -> Unit
) {
    SubcomposeLayout { constraints ->
        val mainPlaceables = subcompose(SlotsEnum.Main, mainContent).map {
            it.measure(constraints)
        }
        val maxSize = mainPlaceables.fold(IntSize.Zero) { currentMax, placeable ->
            IntSize(
                width = maxOf(currentMax.width, placeable.width),
                height = maxOf(currentMax.height, placeable.height)
            )
        }
        layout(maxSize.width, maxSize.height) {
            if(showMainContent) mainPlaceables.forEach { it.placeRelative(0, 0) }
            subcompose(SlotsEnum.Dependent) {
                dependentContent(maxSize)
            }.forEach {
//                -100x for drop box
                it.measure(constraints).placeRelative(0, 0)
            }
        }
    }
}


@Composable
fun NestedDropdownLayout(nestingDropboxWidth: Dp, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each children
            measurable.measure(constraints)
        }

        val maxSize = placeables.fold(IntSize.Zero) { currentMax, placeable ->
            IntSize(
                width = maxOf(currentMax.width, placeable.width),
                height = maxOf(currentMax.height, placeable.height)
            )
        }

        // Set the size of the layout as big as it can
        layout(maxSize.width, maxSize.height) {
            // Track the y co-ord we have placed children up to

            // Place children in the parent layout
            placeables.forEach { placeable ->
                // Position item on the screen
                placeable.placeRelative(x = nestingDropboxWidth.toPx().toInt(), y = 0)

                // Record the y co-ord placed up to
            }
        }
    }
}

enum class SlotsEnum { Main, Dependent }

@Composable
fun OffsetSubcomposeLayout(
    modifier: Modifier = Modifier,
    containerEnd: Dp,
    mainContent: @Composable () -> Unit,
    dependentContent: @Composable (Size) -> Unit
) {
    SubcomposeLayout(
        modifier = modifier
    ) { constraints: Constraints ->

        // Subcompose(compose only a section) main content and get Placeable
        val mainPlaceables: List<Placeable> = subcompose(SlotsEnum.Main, mainContent)
            .map {
                it.measure(constraints.copy(minWidth = 0, minHeight = 0))
            }

        // Get max width and height of main component
        var maxWidth = 0
        var maxHeight = 0

        mainPlaceables.forEach { placeable: Placeable ->
            maxWidth += placeable.width
            maxHeight = placeable.height
        }

        val dependentPlaceables: List<Placeable> = subcompose(SlotsEnum.Dependent) {
            dependentContent(Size(maxWidth.toFloat(), maxHeight.toFloat()))
        }
            .map { measurable: Measurable ->
                measurable.measure(constraints)
            }

        layout(maxWidth, maxHeight) {

            dependentPlaceables.forEach { placeable: Placeable ->
                placeable.placeRelative(0, 0)
            }
        }
    }
}

enum class SlotsEnum2 { Main, Dependent }