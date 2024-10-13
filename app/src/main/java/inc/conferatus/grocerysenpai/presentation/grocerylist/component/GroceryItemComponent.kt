package inc.conferatus.grocerysenpai.presentation.grocerylist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inc.conferatus.grocerysenpai.R
import inc.conferatus.grocerysenpai.data.GroceryListItem

@Composable
fun GroceryItemComponent(
    item: GroceryListItem,
    onRemove: (GroceryListItem) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    horizontalPadding : Dp = 10.dp,
    verticalPadding : Dp = 10.dp,
    textStyle: TextStyle = TextStyle(fontSize = 18.sp)
) {
    Box(
        modifier = modifier
            .padding(horizontalPadding, verticalPadding)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = shape
            )
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.name,
                style = textStyle,
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
