package inc.conferatus.grocerysenpai.presentation.grocerylist

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import inc.conferatus.grocerysenpai.R
import inc.conferatus.grocerysenpai.presentation.grocerylist.component.GroceryItemComponent
import inc.conferatus.grocerysenpai.presentation.grocerylist.component.GroceryItemTextInputComponent
import inc.conferatus.grocerysenpai.presentation.grocerylist.component.GroceryNoItemsTextComponent

// todo TODO!!!!!!!!!!!!
// split onto components
// focus change after typing is finished
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
                    Text(
                        text = stringResource(R.string.app_name)
                    )
                }
            )
        },

        bottomBar = {
            GroceryItemTextInputComponent(
                value = viewModel.itemInput,
                onInsertClick = viewModel::addItem,
                onValueChange = viewModel::updateItemInput,
                isError = !viewModel.itemInputValidate
            )
        }

){ innerPadding ->
        Column(
            Modifier
                .fillMaxHeight()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            if (groceryListUiState.groceryListItems.isEmpty()) {
                GroceryNoItemsTextComponent()
            } else {
                groceryListUiState.groceryListItems.forEach {
                    GroceryItemComponent(
                        item = it,
                        onRemove = viewModel::removeItem
                    )
                }
            }
        }
    }
}