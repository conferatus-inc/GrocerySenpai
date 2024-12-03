package inc.conferatus.grocerysenpai.presentation.mainlist

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import inc.conferatus.grocerysenpai.R
import inc.conferatus.grocerysenpai.model.util.HistoryGroceriesUtils.Companion.groupByDateDescending
import inc.conferatus.grocerysenpai.presentation.common.component.OnEmptyMessageComponent
import inc.conferatus.grocerysenpai.presentation.mainlist.component.HistoryEntryComponent
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    viewModel2: MainListViewModel,
    onGoBackClick: () -> Unit,
    onGoToQrScannerClick: () -> Unit
) {
    val historyGroceries by viewModel.historyGroceries.collectAsState(initial = emptyList())

    LaunchedEffect(viewModel) {
        viewModel2.startDataFetching()
    }

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
                },
                actions = {
                    IconButton(
                        onClick = onGoToQrScannerClick
                    ) {
                        Icon(Icons.Default.QrCode, stringResource(R.string.go_to_history_btn))
                    }
                }
            )
        }
    ) { innerPadding ->
        if (historyGroceries.isEmpty()) {
            OnEmptyMessageComponent(
                text = stringResource(R.string.history_no_items_text),
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .animateContentSize()
            ) {
                historyGroceries.groupByDateDescending()
                    .sortedByDescending { it.first }
                    .forEach { pair ->
                        item(pair.first) {
                            Text(
                                text = pair.first.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                        }

                        items(pair.second) {
                            val coroutineScope = rememberCoroutineScope()
                            HistoryEntryComponent(
                                mainText = it.category.name,
                                secondaryText = it.description,
                                amountText = "%d %s".format(it.amount, it.amountPostfix),
                                onAddClick = { coroutineScope.launch { viewModel.addItem(it) } },
                                onRemoveClick = { coroutineScope.launch { viewModel.removeItem(it) } },
                                modifier = Modifier.animateItemPlacement()
                            )
                        }
                }
            }
        }

    }
}

