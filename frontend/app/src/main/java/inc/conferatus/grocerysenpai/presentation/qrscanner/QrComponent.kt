package inc.conferatus.grocerysenpai.presentation.qrscanner

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import inc.conferatus.grocerysenpai.model.items.GroceryItem
import inc.conferatus.grocerysenpai.presentation.mainlist.QrProductsDto
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import kotlin.reflect.KFunction1

@Composable
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
                val tmp = onScanned(result.content.rawValue.toString())
                tmp.products.forEach { save(GroceryItem(it)) }

//
//                onSuccess
//                result.content.rawValue
//                // decoding with default UTF-8 charset when rawValue is null will not result in meaningful output, demo purpose
//                    ?: result.content.rawBytes?.let { String(it) }.orEmpty()
            }

            else -> {}
        }
    }

//    Text(text = input.value)
//    if (input.value == "") {
//        println(xdd)
    if (flag) {
        SideEffect {
            scanQrCodeLauncher.launch(null)
            flag = false
        }
    }
//        Button(onClick = onDone) {
//        }
//    }
//    Text(text = result2)
//    var func: @Composable () -> Unit = {}

//    val scanQrCodeLauncher = rememberLauncherForActivityResult(ScanQRCode()) { result ->
//        when (result) {
//            is QRResult.QRSuccess -> {
//                println(result.content.rawValue.toString())
//                println("AAAAAAAAAAAAAAAAAAAAAAAAAA")
//                val tmp = onScanned(result.content.rawValue.toString())
//                tmp.products.forEach { save(GroceryItem(it, xdd)) }
////                save(tmp)
//
//                onSuccess
//            }
//
//            else -> {
//                println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
////                onErrorScanning
//            }
//        }
//        onDone()
//    }
//
//    SideEffect {
////        println("ABOBA")
//        scanQrCodeLauncher.launch(null)
//    }
//
////    func()
}
