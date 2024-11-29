package inc.conferatus.grocerysenpai

import android.content.ContentProvider
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.CloudMediaProvider
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken
import dagger.hilt.android.AndroidEntryPoint
import inc.conferatus.grocerysenpai.GrocerySenpaiApp.Companion.sharedPreferences
import inc.conferatus.grocerysenpai.GrocerySenpaiApp.Companion.sharedPreferencesEditor
import inc.conferatus.grocerysenpai.api.BackendApi
import inc.conferatus.grocerysenpai.api.Role
import inc.conferatus.grocerysenpai.model.items.GroceryItem
import inc.conferatus.grocerysenpai.presentation.mainlist.HistoryScreen
import inc.conferatus.grocerysenpai.presentation.mainlist.HistoryViewModel
import inc.conferatus.grocerysenpai.presentation.mainlist.MainListScreen
import inc.conferatus.grocerysenpai.presentation.mainlist.MainListViewModel
import inc.conferatus.grocerysenpai.ui.theme.GrocerySenpaiTheme
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private fun String.makeBearer(): String {
        return "Bearer $this"
    }

    private fun handleResult(result: YandexAuthResult) {
        when (result) {
            is YandexAuthResult.Success -> onSuccessAuth(result.token)
            is YandexAuthResult.Failure -> onProccessError(result.exception)
            YandexAuthResult.Cancelled -> onCancelled()
        }
    }

    private fun onSuccessAuth(token: YandexAuthToken) {
        println(token)
        runBlocking {
            val res = BackendApi.loginApi.login(token.value.makeBearer(), Role.ROLE_USER)
            println(res)

            accessToken = res["access_token"].toString().makeBearer()
            refreshToken = res["refresh_token"].toString().makeBearer()

            sharedPreferencesEditor!!.putString("refresh_token", refreshToken).apply()

            println(sharedPreferences!!.getString("refresh_token", null))
        }
    }

    private fun onProccessError(exception: YandexAuthException) {
        println(exception)
    }

    private fun onCancelled() {
        println("Cancelled")
    }

    private fun startAuth() {
        val launcher = registerForActivityResult(GrocerySenpaiApp.sdk!!.contract) { result ->
            handleResult(result)
        }
        val loginOptions = YandexAuthLoginOptions()
        launcher.launch(loginOptions)
    }

    private fun startRelogin() {
        runBlocking {
            println("$refreshToken")
            val res = BackendApi.loginApi.refresh(refreshToken!!)

            accessToken = res["access_token"].toString().makeBearer()
            refreshToken = res["refresh_token"].toString().makeBearer()
        }
    }

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

                var input = remember { mutableStateOf("") }

                val coroutineScope = rememberCoroutineScope()

                val scanQrCodeLauncher = rememberLauncherForActivityResult(ScanQRCode()) { result ->
                    when (result) {
                        is QRResult.QRSuccess -> {
                            println("FNOASBRFDA")
                            input.value = result.content.rawValue.toString()
                            println(input.value)

                            coroutineScope.launch {
                                // TODO
//                                val res = BackendApi.qrApi.getQrData(accessToken!!, input.value);
//                                res.products.forEach { historyViewModel.addItem(GroceryItem(it)) }
                            }

                        }

                        else -> {}
                    }
                }

                HistoryScreen(
                    viewModel = historyViewModel,
                    onGoBackClick = { navController.navigate("main") },
                    onGoToQrScannerClick = {
                        scanQrCodeLauncher.launch(null)
//                        navController.navigate("qrScanner")
                    }
                )
            }

            composable(route = "qrScanner") {
                /*todo*/
//                val qrScannerViewModel: QrScannerViewModel by viewModels()
//                val historyViewModel: HistoryViewModel by viewModels()

//                QrTest()

//                val coroutineScope = rememberCoroutineScope()


//                val mainListViewModel: MainListViewModel by viewModels()
//                GetQRCodeExample(
//                    viewModel = qrScannerViewModel,
//                    onScanned = qrScannerViewModel::sendRequest,
////                    onScanned = {  },
//                    onSuccess = {},
//                    onErrorScanning = {},
//                    save2 = historyViewModel::addMyItem,
//                    save = historyViewModel::addMyItem,
//                    check = historyViewModel::addMyItem,
//                    onDone = { navController.navigate("history") }
//                )
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (GrocerySenpaiApp.sdk != null) {
            startAuth()
        }
        else {
            startRelogin()
        }

//        binding.button.setOnClickListener { scanQrCodeLauncher.launch(null) }

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

    companion object {
        var accessToken: String? = null
        var refreshToken: String? = null
    }
}
