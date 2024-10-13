package inc.conferatus.grocerysenpai.presentation.grocerylist.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import inc.conferatus.grocerysenpai.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryItemTextInputComponent(
    value: String,
    onInsertClick: () -> Unit,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier,
    inputFieldShape: Shape = RoundedCornerShape(16.dp),
    horizontalPadding : Dp = 10.dp,
    verticalPadding : Dp = 10.dp,
    textStyle: TextStyle = TextStyle.Default // ?todo
) {
    OutlinedTextField(
        isError = isError,
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier
            .padding(horizontalPadding, verticalPadding)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { if (!isError) onInsertClick() }
        ),
        shape = inputFieldShape,
        label = {
            Text(
                text = stringResource(R.string.grocery_item_text_input_label)
            )},
        trailingIcon = {
            TextButton(
                enabled = !isError,
                onClick = onInsertClick,
                shape = CircleShape,
                modifier = modifier.padding(horizontalPadding, verticalPadding)
            ) {
                Icon(
                    Icons.Default.AddCircle,
                    stringResource(R.string.grocery_item_text_input_insert_btn)
                )
            }
        }
    )
}
