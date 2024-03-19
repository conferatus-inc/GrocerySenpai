package inc.conferatus.grocerysenpai.model.util

import inc.conferatus.grocerysenpai.model.items.GroceryItem
import java.time.LocalDate

class HistoryGroceriesUtils {
    companion object {
        fun List<GroceryItem>.groupByDateDescending(
        ): List<Pair<LocalDate, List<GroceryItem>>> {
            return this.groupBy { it.bought!!.toLocalDate() }
                .toList() // todo адекватно сделать
        }
    }
}
