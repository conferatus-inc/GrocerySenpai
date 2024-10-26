package inc.conferatus.grocerysenpai.presentation.mainlist

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import inc.conferatus.grocerysenpai.R
import inc.conferatus.grocerysenpai.model.util.HistoryGroceriesUtils.Companion.groupByDateDescending
import inc.conferatus.grocerysenpai.presentation.history.component.HistoryNoItemsTextComponent
import inc.conferatus.grocerysenpai.presentation.mainlist.component.HistoryEntryComponent

// todo TODO!!!!!!!!!!!!
// todo мб сюда вообще не отдавать айтемы, а чисто стрингами все?? подумать как будет лучше архитектурно
// todo focus change after typing is finished
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onGoBackClick: () -> Unit
) {
    val historyGroceries by viewModel.historyGroceries.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.history_title),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onGoBackClick
                    ) {
                        Icon(Icons.Default.ArrowBack, stringResource(R.string.go_back_btn))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxHeight()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            if (historyGroceries.isEmpty()) {
                HistoryNoItemsTextComponent()
            } else {
                historyGroceries.groupByDateDescending().forEach {
                    Text(
                        text = it.key.form
                        style = MaterialTheme.typography.bodyLarge
                    ),

                    HistoryEntryComponent(
                        mainText
                        secondaryText = it.description,
                        amountText = "%d %s".format(it.amount, it.amountPostfix),
                        onRemoveButton = { viewModel.removeItem(it) }
                    )
                }
            }
        }
    }
}

