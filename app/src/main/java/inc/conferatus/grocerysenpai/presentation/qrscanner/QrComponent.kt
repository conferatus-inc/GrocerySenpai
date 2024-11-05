package inc.conferatus.grocerysenpai.presentation.qrscanner

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode

@Composable
fun GetQRCodeExample() {
    var input = remember { mutableStateOf("") }
    val scanQrCodeLauncher = rememberLauncherForActivityResult(ScanQRCode()) { result ->
        when (result) {
            is QRResult.QRSuccess -> {
                input.value = result.content.rawValue.toString()

                // TODO AAAAAAAAAAAAAAAAAAAAAAAa
            }

            else -> {}
        }
    }
    SideEffect {
        scanQrCodeLauncher.launch(null)
    }


}
