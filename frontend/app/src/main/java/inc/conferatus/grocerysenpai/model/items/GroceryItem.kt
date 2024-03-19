package inc.conferatus.grocerysenpai.model.items

import inc.conferatus.grocerysenpai.presentation.mainlist.ProductDto
import java.time.ZonedDateTime

data class GroceryItem(
    val id: Int = 0,
    val category: CategoryItem,
    val description: String,
    val amount: Int,
    val amountPostfix: String,
    val bought: ZonedDateTime? = null
) {
    constructor(productDto: ProductDto) :
            this(
                (53245..624574252).random(),
                CategoryItem((53245..624574252).random(), productDto.category)
//                xdd ?: CategoryItem(
//                    (53245..624574252).random(),
//                    "Чай"
//                ), ""
                , "",
                productDto.amount, "", ZonedDateTime.now()
            )

}
