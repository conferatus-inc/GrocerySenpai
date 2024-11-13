package inc.conferatus.grocerysenpai.presentation.qrscanner

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import inc.conferatus.grocerysenpai.model.items.GroceryItem
import inc.conferatus.grocerysenpai.api.QrProductsDto
import inc.conferatus.grocerysenpai.model.items.CategoryItem
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import java.time.ZonedDateTime
import kotlin.reflect.KFunction1

@Composable
@Deprecated(message = "Больше не используем")
fun GetQRCodeExample(
    viewModel: QrScannerViewModel,
    onScanned: (String) -> QrProductsDto,
    onSuccess: @Composable () -> Unit,
    onErrorScanning: @Composable () -> Unit,
    save2: (GroceryItem) -> Unit,
    save: (GroceryItem) -> Unit,
    onDone: () -> Unit,
    check: KFunction1<GroceryItem, Unit>
) {
    var flag = true
    var input = remember { mutableStateOf("") }

    val scanQrCodeLauncher = rememberLauncherForActivityResult(ScanQRCode()) { result ->
        when (result) {
            is QRResult.QRSuccess -> {
                input.value = result.content.rawValue.toString()
                println(input.value)

//                save2(GroceryItem((53245..624574252).random(), CategoryItem(13, "Газировка"), "", 1, "", ZonedDateTime.now()))
//                save2(GroceryItem((53245..624574252).random(), CategoryItem(31, "Лимонад"), "", 1, "", ZonedDateTime.now()))
//                save2(GroceryItem((53245..624574252).random(), CategoryItem(30, "Лимонад"), "", 1, "", ZonedDateTime.now()))
//                save2(GroceryItem((53245..624574252).random(), CategoryItem(74, "Лосось"), "", 1, "", ZonedDateTime.now()))

//                var products = Stack<ProductDto>()
//                products.push(ProductDto("Вода", 1))
//                products.push(ProductDto("Вода", 2))

//                products.forEach {
//                    val groceryItem = GroceryItem(it);
//                    save2(groceryItem);
//                }

//                save2(GroceryItem((53245..624574252).random(), CategoryItem(76, "Вода"), "", 3, "", ZonedDateTime.now()))
//                save2(GroceryItem((53245..624574252).random(), CategoryItem(56, "Хлеб"), "", 1, "", ZonedDateTime.now()))
//                save2(GroceryItem((53245..624574252).random(), CategoryItem(13, "Газировка"), "", 1, "", ZonedDateTime.now()))
//                save2(GroceryItem((53245..624574252).random(), CategoryItem(31, "Лимонад"), "", 1, "", ZonedDateTime.now()))
//                save2(GroceryItem((53245..624574252).random(), CategoryItem(30, "Лимонад"), "", 1, "", ZonedDateTime.now()))
//                save2(GroceryItem((53245..624574252).random(), CategoryItem(74, "Лосось"), "", 1, "", ZonedDateTime.now()))
//                save2(GroceryItem((53245..624574252).random(), CategoryItem(), "", 1, "", ZonedDateTime.now()))
//                var groceryItem = GroceryItem(product)

//                var groceryItem = GroceryItem(product, viewModel.getCategory(product.category))
//                println(groceryItem)
//                println(groceryItem.toString())
//                println(xdd)
//                save2(groceryItem)
//                save(groceryItem)
//                val tmp = onScanned(result.content.rawValue.toString())
//                tmp.products.forEach { save(GroceryItem(it)) }

//
//                onSuccess
//                result.content.rawValue
//                // decoding with default UTF-8 charset when rawValue is null will not result in meaningful output, demo purpose
//                    ?: result.content.rawBytes?.let { String(it) }.orEmpty()
            }

            else -> {}
        }
    }
    Button(onClick = { scanQrCodeLauncher.launch(null) }) {

    }
//    if (flag) {
//        SideEffect {
//            scanQrCodeLauncher.launch(null)
//            flag = false
//        }
//    }
}
