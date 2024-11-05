package inc.conferatus.grocerysenpai.presentation.qrscanner

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode

@Composable
fun GetQRCodeExample(
    onScanned: (String) -> Boolean,
    onSuccess: @Composable () -> Unit,
    onErrorScanning: @Composable () -> Unit,
    onErrorChecking: @Composable () -> Unit
) {
    var func: @Composable () -> Unit = {}

    val scanQrCodeLauncher = rememberLauncherForActivityResult(ScanQRCode()) { result ->
        func = when (result) {
            is QRResult.QRSuccess -> {
                val tmp = onScanned(result.content.rawValue.toString())
                if (tmp) onSuccess else onErrorChecking
            }

            else -> {
                onErrorScanning
            }
        }
    }

    func()

    SideEffect {
        scanQrCodeLauncher.launch(null)
    }
}
