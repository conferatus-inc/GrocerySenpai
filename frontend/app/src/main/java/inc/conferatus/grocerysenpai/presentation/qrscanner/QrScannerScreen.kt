package inc.conferatus.grocerysenpai.presentation.qrscanner

import androidx.compose.runtime.Composable
import inc.conferatus.grocerysenpai.model.items.GroceryItem
import inc.conferatus.grocerysenpai.presentation.mainlist.ProductsDto
import kotlin.reflect.KFunction1

@Composable
fun QrScannerScreen(
    viewModel: QrScannerViewModel,
    onScanned: (String) -> ProductsDto,
    onSuccess: @Composable () -> Unit,
    onErrorScanning: @Composable () -> Unit,
    save2: (GroceryItem) -> Unit,
    save: (GroceryItem) -> Unit,
    onDone: () -> Unit,
    check: KFunction1<GroceryItem, Unit>
) {
    GetQRCodeExample(
        viewModel,
        onScanned,
        onSuccess,
        onErrorScanning,
        save2,
        save,
        onDone,
        check
    )
}
