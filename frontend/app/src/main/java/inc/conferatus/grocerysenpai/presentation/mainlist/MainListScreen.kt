package inc.conferatus.grocerysenpai.presentation.mainlist

import CategoriesSuggesterComponent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import inc.conferatus.grocerysenpai.R
import inc.conferatus.grocerysenpai.presentation.common.component.EntryComponent
import inc.conferatus.grocerysenpai.presentation.mainlist.component.MainItemTextInputComponent
import inc.conferatus.grocerysenpai.presentation.mainlist.component.MainListEntryComponent
import inc.conferatus.grocerysenpai.presentation.mainlist.component.SuggestedItemsEntryComponent
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.max

//import androidx.hilt.navigation.compose.hiltViewModel

// todo TODO!!!!!!!!!!!!
// todo мб сюда вообще не отдавать айтемы, а чисто стрингами все?? подумать как будет лучше архитектурно
// todo focus change after typing is finished
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainListScreen(
    viewModel: MainListViewModel,
    onGoToHistoryClick: () -> Unit
) {
    val currentGroceries by viewModel.currentGroceries.collectAsState(initial = emptyList())
    val historyGroceries by viewModel.historyGroceries.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                actions = {
                    IconButton(
                        onClick = onGoToHistoryClick
                    ) {
                        Icon(Icons.Default.History, stringResource(R.string.go_to_history_btn))
                    }
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
        LazyColumn(
            Modifier
                .fillMaxHeight()
                .padding(innerPadding)
        ) {
            items(currentGroceries) {
                MainListEntryComponent(
                    mainText = it.category.name,
                    secondaryText = it.description,
                    amountText = "%d %s".format(it.amount, it.amountPostfix),
                    onDoneButton = { viewModel.buyItem(it) },
                    onRemoveButton = { viewModel.removeItem(it) }
                )
            }

            item {
                Text(
                    text = stringResource(R.string.suggested_text),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(10.dp, 10.dp)
                )
            }

            // tmp
            item {
                Button(
                    onClick = { viewModel.genFakes() }
                ) {
                    Text(text = "fakes")
                }
            }

            items(viewModel.getSuggested(historyGroceries)) {
                SuggestedItemsEntryComponent(
                    mainText = it.category,
                    secondaryText = "%d days before next buy".format(max(0, ZonedDateTime.now().until(it.nextBuy, ChronoUnit.DAYS))),
                    amountText = "",
                    onAddClick = { viewModel.updateInput(it.category) }
                )
            }
        }
    }
}