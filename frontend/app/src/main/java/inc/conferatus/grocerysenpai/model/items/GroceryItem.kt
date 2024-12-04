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
                CategoryItem(
                    id = (53245..624574252).random(),
                    name = qrProductDto.category
                ),
                "",
                1,
                "",
                ZonedDateTime.now()
            )

}
