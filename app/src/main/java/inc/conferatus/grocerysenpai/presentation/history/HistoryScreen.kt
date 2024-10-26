package inc.conferatus.grocerysenpai.presentation.mainlist

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import inc.conferatus.grocerysenpai.R
import inc.conferatus.grocerysenpai.model.util.HistoryGroceriesUtils.Companion.groupByDateDescending
import inc.conferatus.grocerysenpai.presentation.history.component.HistoryNoItemsTextComponent
import inc.conferatus.grocerysenpai.presentation.mainlist.component.HistoryEntryComponent
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
        LazyColumn (
            modifier = Modifier
                .padding(innerPadding)
                .animateContentSize()
        ){
            historyGroceries.groupByDateDescending().forEach {
                pair ->
                    item(pair.first) {
                        Text(text = pair.first.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")))
                    }

                    items(pair.second) {
                        HistoryEntryComponent(
                            mainText = it.category.name,
                            secondaryText = it.description,
                            amountText = "%d %s".format(it.amount, it.amountPostfix),
                            onAddClick = { viewModel.addItem(it) },
                            onRemoveClick = { viewModel.removeItem(it) },
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
            }
        }

    }
}

