package inc.conferatus.grocerysenpai.presentation.mainlist.component

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import inc.conferatus.grocerysenpai.R
import inc.conferatus.grocerysenpai.presentation.common.component.EntryComponent

@Composable
fun HistoryEntryComponent(
    mainText: String,
    secondaryText: String,
    amountText: String,
    onAddClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EntryComponent(
        mainText = mainText,
        secondaryText = secondaryText,
        amountText = amountText,
        modifier = modifier,
        rightSideComponents = listOf {
            TextButton(
                onClick = onRemoveClick,
                modifier = modifier,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Delete, stringResource(R.string.grocery_item_remove_btn))
            }

            TextButton(
                onClick = onAddClick,
                modifier = modifier,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, stringResource(R.string.add_from_history_btn))
            }
        }
    )
}
