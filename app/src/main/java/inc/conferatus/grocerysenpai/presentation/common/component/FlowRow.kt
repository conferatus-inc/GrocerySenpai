package inc.conferatus.grocerysenpai.presentation.common.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 4.dp,
    verticalSpacing: Dp = 4.dp,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        var xPosition = 0
        var yPosition = 0
        val itemConstraints = Constraints(maxWidth = constraints.maxWidth)

        layout(constraints.maxWidth, constraints.maxHeight) {
            for (measurable in measurables) {
                val placeable = measurable.measure(itemConstraints)

                if (xPosition + placeable.width > constraints.maxWidth) {
                    xPosition = 0
                    yPosition += placeable.height + verticalSpacing.value.toInt()
                }

                placeable.place(x = xPosition, y = yPosition)

                xPosition += placeable.width + horizontalSpacing.value.toInt()
            }
        }
    }
}
