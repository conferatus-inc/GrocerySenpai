package inc.conferatus.grocerysenpai.presentation.mainlist.component

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import inc.conferatus.grocerysenpai.R
import inc.conferatus.grocerysenpai.presentation.common.component.EntryComponent

@Composable
fun MainItemEntryComponent(
    mainText: String,
    secondaryText: String,
    amountText: String,
    onRemoveButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    EntryComponent(
        mainText = mainText,
        secondaryText = secondaryText,
        amountText = amountText,
        modifier = modifier,
        rightSideComponents = listOf {
            TextButton(
                onClick = onRemoveButton,
                modifier = modifier,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Delete, stringResource(R.string.grocery_item_remove_btn))
            }

            TextButton(
                onClick = onRemoveButton,
                modifier = modifier,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Delete, stringResource(R.string.grocery_item_remove_btn))
            }
        }
    )
}
