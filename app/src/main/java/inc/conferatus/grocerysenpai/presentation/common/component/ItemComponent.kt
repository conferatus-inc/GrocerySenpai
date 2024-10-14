package inc.conferatus.grocerysenpai.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ItemComponent(
    text: String,
    modifier: Modifier = Modifier,
    buttons: List<@Composable () -> Unit> = emptyList(),
    horizontalPadding: Dp = 10.dp,
    verticalPadding: Dp = 10.dp,
) {
    Box(
        modifier = modifier
            .padding(horizontalPadding, verticalPadding)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.small
            )
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = modifier.padding(horizontalPadding, verticalPadding)
            )

            Row {
                buttons.forEach { it.invoke() }
            }
        }
    }
}
