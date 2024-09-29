package inc.conferatus.grocerysenpai.presentation.shoppinglist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun GroceryListScreen(groceryListViewModel: GroceryListViewModel = GroceryListViewModel()) {
    val groceryListUiState by groceryListViewModel.uiState.collectAsState()

    Column(
//        Modifier.fillMaxHeight(),
//        verticalArrangement = Arrangement.SpaceBetween
    ) {
        groceryListUiState.groceryListItems.forEach {
            Text(
                text = it.toString()
            )
        }

        Text(
            text = "count is " + groceryListUiState.counter
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        Row {
            Button(
                onClick = { groceryListViewModel.newGavna() }
            ) {
                Text(
                    text = "add gavna"
                )
            }

            Button(
                onClick = { groceryListViewModel.clear() }
            ) {
                Text(
                    text = "clear"
                )
            }
        }
    }
}
