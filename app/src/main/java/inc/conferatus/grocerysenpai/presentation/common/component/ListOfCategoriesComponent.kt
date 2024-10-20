import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inc.conferatus.grocerysenpai.model.items.CategoryItem

@OptIn(ExperimentalLayoutApi::class)
//@Preview
@Composable
fun ListOfCategoriesComponent(userInput: String, onClick: () -> Unit) {
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

    fun filterCategories(): List<CategoryItem> {
        return categories.filter { category ->
            category.name.lowercase().startsWith(userInput.lowercase())
        }
    }

    // var because list changes because of userInput changes
//    var filteredCategories = filterCategories()

    val boxPadding = 10.dp
    FlowRow(
        modifier = Modifier.padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(boxPadding)
    ) {
        for (category in filterCategories()) {
            TextButton(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}