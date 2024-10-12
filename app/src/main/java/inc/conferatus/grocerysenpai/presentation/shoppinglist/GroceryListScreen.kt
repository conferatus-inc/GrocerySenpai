package inc.conferatus.grocerysenpai.presentation.shoppinglist

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// todo TODO!!!!!!!!!!!!
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showSystemUi = true, showBackground = true)
fun GroceryListScreen(viewModel: GroceryListViewModel = GroceryListViewModel()) {
    val groceryListUiState by viewModel.uiState.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text("GrocerySenpai")
                }
            )
        },

        bottomBar = {
            Row {
                OutlinedTextField(
                    value = viewModel.itemInput,
                    onValueChange = { viewModel.updateUserItemInput(it) },
                    singleLine = true,
//                modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { viewModel.addRandomNote() }
                    )
                )

                Button(
                    onClick = { viewModel.addRandomNote() }
                ) {
                    Text(
                        text = "+"
                    )
                }
            }
        }

){ innerPadding ->
        Column(
            Modifier.fillMaxHeight()
                .padding(innerPadding)
        ) {
            groceryListUiState.groceryListItems.forEach {
                Text(
                    text = it.toString()
                )
            }
        }
    }
}
