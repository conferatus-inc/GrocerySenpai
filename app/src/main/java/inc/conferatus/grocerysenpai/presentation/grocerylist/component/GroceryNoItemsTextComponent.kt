package inc.conferatus.grocerysenpai.presentation.grocerylist.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inc.conferatus.grocerysenpai.R

//todo font style, align and so on
@Composable
fun GroceryNoItemsTextComponent(
    modifier: Modifier = Modifier,
    horizontalPadding : Dp = 10.dp,
    verticalPadding : Dp = 10.dp
) {
    Text(
        text = stringResource(R.string.grocery_no_items_text),
        modifier = modifier.padding(horizontalPadding, verticalPadding)
    )
}
