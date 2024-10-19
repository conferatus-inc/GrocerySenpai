import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inc.conferatus.grocerysenpai.model.items.CategoryItem

@OptIn(ExperimentalLayoutApi::class)
//@Preview
@Composable
fun ListOfCategories(userInput: String) {
    val categories = mutableListOf(
        // TODO: take from DB
        CategoryItem(1, "Хлеб"),
        CategoryItem(3, "Помидор"),
        CategoryItem(0, "Огурец"),
        CategoryItem(1, "Сыр"),
        CategoryItem(2, "Пивас"),
        CategoryItem(2, "Пивасик"),
        CategoryItem(2, "Пивчанский"),
        CategoryItem(2, "Пиво"),
        CategoryItem(2, "Соль"),
        CategoryItem(3, "Молоко"),
        CategoryItem(0, "Куриное филе"),
        CategoryItem(0, "Шавуха"),
        CategoryItem(1, "Перец"),
        CategoryItem(2, "Творог"),
        CategoryItem(3, "Сахар"),
    )

    // var because list changes because of userInput changes
    var filteredCategories = categories.filter { category ->
        category.name.lowercase().startsWith(userInput.lowercase())
    }

    val boxPadding = 8.dp
    FlowRow(
        modifier = Modifier.padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(boxPadding)
    ) {
        for (category in filteredCategories) {
            Box(
                modifier = Modifier.padding(top = boxPadding)
            ) {
                Box(
                    Modifier
                        .clip(shape = RoundedCornerShape(12.dp, 12.dp, 12.dp, 12.dp))
                        .background(Color(0xFF4D4D4D))
                        .wrapContentWidth()
                        .animateContentSize()
                ) {
                    Text(
                        text = category.name,
                        modifier = Modifier.padding(all = 7.dp),
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}