import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inc.conferatus.grocerysenpai.model.items.CategoryItem
import inc.conferatus.grocerysenpai.presentation.common.component.FlowRow

fun getRandomColor(): Color {
    return listOf(
        Color(0xFFB35707),
        Color(0xFF8F0450),
        Color(0xFF0B7510),
        Color(0xFFB12626),
        Color(0xFF11977C),
        Color(0xFF092B8D),
    ).random()
}

@Preview
@Composable
fun ListOfCategories() {
    val categories = listOf(
        // TODO: take categories from DB
        CategoryItem(0, "Шавуха"),
        CategoryItem(1, "Хлеб"),
        CategoryItem(2, "Пивас"),
        CategoryItem(3, "Помидор"),
        CategoryItem(0, "Огурец"),
        CategoryItem(1, "Сыр"),
        CategoryItem(2, "Соль"),
        CategoryItem(3, "Молоко"),
        CategoryItem(0, "Куриное филе"),
        CategoryItem(1, "Перец"),
        CategoryItem(2, "Творог"),
        CategoryItem(3, "Сахар"),
    )

    FlowRow(
        modifier = Modifier.padding(16.dp),
        horizontalSpacing = 20.dp,
        verticalSpacing = 20.dp
    ) {
        for (category in categories) {
            Box(
                Modifier
                    .clip(shape = RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp))
                    .background(getRandomColor())
                    .wrapContentWidth()
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