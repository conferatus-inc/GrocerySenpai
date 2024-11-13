package inc.conferatus.grocerysenpai.model.items

import inc.conferatus.grocerysenpai.api.QrProductDto
import java.time.ZonedDateTime

data class GroceryItem(
    val id: Int = 0,
    val category: CategoryItem,
    val description: String,
    val amount: Int,
    val amountPostfix: String,
    val bought: ZonedDateTime? = null
) {
    constructor(qrProductDto: QrProductDto) :
            this(
                (53245..624574252).random(),
                CategoryItem((53245..624574252).random(), qrProductDto.category)
//                xdd ?: CategoryItem(
//                    (53245..624574252).random(),
//                    "Чай"
//                ), ""
                , "",
                qrProductDto.amount, "", ZonedDateTime.now()
            )

}
