package inc.conferatus.grocerysenpai.presentation.shoppinglist

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// todo TODO!!!!!!!!!!!!
// split onto components
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
            Row (
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                OutlinedTextField(
                    value = viewModel.itemInput,
                    onValueChange = { viewModel.updateUserItemInput(it) },
                    singleLine = true,
                    modifier = Modifier.padding(8.dp, 8.dp),
//                modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { viewModel.addItem() }
                    )
                )

                Button(
                    onClick = { viewModel.addItem() },
                    modifier = Modifier.padding(8.dp, 8.dp)
                ) {
                    Text(
                        text = "+"
                    )
                }
            }
        }

){ innerPadding ->
        Column(
            Modifier
                .fillMaxHeight()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            groceryListUiState.groceryListItems.forEach {
                Box(
                    modifier = Modifier
                        .padding(8.dp, 8.dp)
                        .fillMaxWidth()
                ) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = it.name,
                            style = TextStyle(fontSize = 18.sp)
                        )

                        TextButton(
                            onClick = { viewModel.removeItem(it) },
                            modifier = Modifier.padding(8.dp, 8.dp)
                        ) {
                            Text(
                                text = "-"
                            )
                        }
                    }

                }
            }
        }
    }
}
