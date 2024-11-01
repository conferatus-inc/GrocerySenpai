package inc.conferatus.grocerysenpai.presentation.qr

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import io.github.g00fy2.quickie.ScanQRCode

@Composable
fun GetQRCodeExample() {
    val scanQrCodeLauncher = rememberLauncherForActivityResult(ScanQRCode()) { result ->
        println(result)
    }

    Button(onClick = { scanQrCodeLauncher.launch(null) }) {

    }
}