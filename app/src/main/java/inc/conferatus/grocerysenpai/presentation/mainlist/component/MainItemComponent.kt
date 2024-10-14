package inc.conferatus.grocerysenpai.presentation.mainlist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import inc.conferatus.grocerysenpai.R
import inc.conferatus.grocerysenpai.model.GroceryItem

@Composable
fun MainItemComponent(
    item: GroceryItem,
    onRemove: (GroceryItem) -> Unit,
    modifier: Modifier = Modifier,
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
                text = item.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = modifier.padding(horizontalPadding, verticalPadding)
            )

            TextButton(
                onClick = { onRemove(item) },
                modifier = modifier.padding(horizontalPadding, verticalPadding)
            ) {
                Icon(Icons.Default.Delete, stringResource(R.string.grocery_item_remove_btn))
            }
        }
    }
}
