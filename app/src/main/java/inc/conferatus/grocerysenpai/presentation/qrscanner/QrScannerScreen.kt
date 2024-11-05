package inc.conferatus.grocerysenpai.presentation.qrscanner

import androidx.compose.runtime.Composable

@Composable
fun QrScannerScreen(
    viewModel: QrScannerViewModel,
) {
    GetQRCodeExample(
        onScanned = { _ -> true },
        onErrorChecking = {},
        onErrorScanning = {},
        onSuccess = {}
    )
}
