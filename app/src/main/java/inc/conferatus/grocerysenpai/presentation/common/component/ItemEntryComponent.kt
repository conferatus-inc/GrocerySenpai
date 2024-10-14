package inc.conferatus.grocerysenpai.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ItemEntryComponent(
    mainText: String,
    secondaryText: String,
    amountText: String,
    modifier: Modifier = Modifier,
    rightSideComponents: List<@Composable () -> Unit> = emptyList(),
    horizontalPadding: Dp = 12.dp,
    verticalPadding: Dp = 14.dp
) {
    Box(
        modifier = modifier
            .padding(horizontalPadding, verticalPadding)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontalPadding, verticalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column (
                modifier = modifier
            ){
                Text(
                    text = mainText,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 18.sp,
                    modifier = modifier
                )

                Text(
                    text = secondaryText,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = modifier
                )
            }

            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
            ){
                Text(
                    text = amountText,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = modifier
                )

                rightSideComponents.forEach { it.invoke() }
            }

        }
    }
}
