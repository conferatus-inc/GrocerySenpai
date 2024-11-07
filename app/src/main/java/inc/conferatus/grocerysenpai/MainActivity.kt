package inc.conferatus.grocerysenpai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import inc.conferatus.grocerysenpai.presentation.mainlist.HistoryScreen
import inc.conferatus.grocerysenpai.presentation.mainlist.HistoryViewModel
import inc.conferatus.grocerysenpai.presentation.mainlist.MainListScreen
import inc.conferatus.grocerysenpai.presentation.mainlist.MainListViewModel
import inc.conferatus.grocerysenpai.presentation.qrscanner.GetQRCodeExample
import inc.conferatus.grocerysenpai.presentation.qrscanner.QrScannerViewModel
import inc.conferatus.grocerysenpai.ui.theme.GrocerySenpaiTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Composable
    fun ScreenRouter() {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "main",
        ) {
            composable(route = "main") {
                val mainListViewModel: MainListViewModel by viewModels()
                MainListScreen(
                    viewModel = mainListViewModel,
                    onGoToHistoryClick = { navController.navigate("history")}
                )
            }

            composable(route = "history") {
                val historyViewModel: HistoryViewModel by viewModels()
                HistoryScreen(
                    viewModel = historyViewModel,
                    onGoBackClick = { navController.navigate("main")},
                    onGoToQrScannerClick = { navController.navigate("qrScanner") }
                )
            }

            composable(route = "qrScanner") {
                /*todo*/
                val qrScannerViewModel: QrScannerViewModel by viewModels()
                val historyViewModel: HistoryViewModel by viewModels()
//                val mainListViewModel: MainListViewModel by viewModels()
                GetQRCodeExample(
                    viewModel = qrScannerViewModel,
                    onScanned = qrScannerViewModel::sendRequest,
//                    onScanned = {  },
                    onSuccess = {},
                    onErrorScanning = {},
                    save2 = historyViewModel::addMyItem,
                    save = historyViewModel::addMyItem,
                    check = historyViewModel::addMyItem,
                    onDone = { navController.navigate("history") }
                )
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GrocerySenpaiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScreenRouter()
                }
            }
        }
    }
}
