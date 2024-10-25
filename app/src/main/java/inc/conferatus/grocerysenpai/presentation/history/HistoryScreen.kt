package inc.conferatus.grocerysenpai.presentation.mainlist

import CategoriesSuggesterComponent
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import inc.conferatus.grocerysenpai.R
import inc.conferatus.grocerysenpai.presentation.mainlist.component.HistoryEntryComponent
import inc.conferatus.grocerysenpai.presentation.mainlist.component.MainItemTextInputComponent
import inc.conferatus.grocerysenpai.presentation.mainlist.component.MainNoItemsTextComponent

//import androidx.hilt.navigation.compose.hiltViewModel

// todo TODO!!!!!!!!!!!!
// todo мб сюда вообще не отдавать айтемы, а чисто стрингами все?? подумать как будет лучше архитектурно
// todo focus change after typing is finished
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainListScreen(viewModel: MainListViewModel) {
    val currentGroceries by viewModel.currentGroceries.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            )
        },

        bottomBar = {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Bottom
            ) {

                CategoriesSuggesterComponent(
                    onCategoryClick = { viewModel.updateInput(it) },
                    categories = viewModel.suggestedCategories
                )
                MainItemTextInputComponent(
                    value = viewModel.textInput,
                    onInsertClick = viewModel::addItem,
                    onValueChange = { value ->
                        viewModel.updateInput(value)
                    },
                    isError = !viewModel.isInputValidated
                )
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxHeight()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            if (currentGroceries.isEmpty()) {
                MainNoItemsTextComponent()
            } else {
                currentGroceries.forEach {
                    HistoryEntryComponent(
                        mainText = it.category.name,
                        secondaryText = it.description,
                        amountText = "%d %s".format(it.amount, it.amountPostfix),
                        onRemoveButton = { viewModel.removeItem(it) }
                    )
                }
            }
        }
    }
}

