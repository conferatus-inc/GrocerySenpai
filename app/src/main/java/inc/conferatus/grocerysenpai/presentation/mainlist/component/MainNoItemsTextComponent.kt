package inc.conferatus.grocerysenpai.presentation.mainlist.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import inc.conferatus.grocerysenpai.R

//todo font style, align and so on
@Composable
fun MainNoItemsTextComponent(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 12.dp,
    verticalPadding: Dp = 12.dp
) {
    Text(
        text = stringResource(R.string.grocery_no_items_text),
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier.padding(horizontalPadding, verticalPadding)
    )
}
