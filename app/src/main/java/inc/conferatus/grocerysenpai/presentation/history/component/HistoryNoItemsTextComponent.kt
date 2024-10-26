package inc.conferatus.grocerysenpai.presentation.history.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import inc.conferatus.grocerysenpai.R
import inc.conferatus.grocerysenpai.presentation.common.component.OnEmptyMessageComponent

//todo font style, align and so on
@Composable
fun HistoryNoItemsTextComponent(
    modifier: Modifier = Modifier
) {
   OnEmptyMessageComponent(text = stringResource(R.string.history_no_items_text))
}
