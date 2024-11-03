package inc.conferatus.grocerysenpai.presentation.qrscanner

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
//                result.content.rawValue
//                // decoding with default UTF-8 charset when rawValue is null will not result in meaningful output, demo purpose
//                    ?: result.content.rawBytes?.let { String(it) }.orEmpty()
            }

            else -> {}
        }
    }

    Text(text = input.value)
    if (input.value == "") {
        Button(onClick = { scanQrCodeLauncher.launch(null) }) {
        }
    }
//    Text(text = result2)
}