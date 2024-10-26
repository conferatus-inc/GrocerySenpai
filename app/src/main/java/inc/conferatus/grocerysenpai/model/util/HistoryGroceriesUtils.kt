package inc.conferatus.grocerysenpai.model.util

import inc.conferatus.grocerysenpai.model.items.GroceryItem
import java.time.LocalDate
import java.util.Date
import java.util.SortedMap

class HistoryGroceriesUtils {
    companion object {
        fun List<GroceryItem>.groupByDateDescending(
        ): SortedMap<LocalDate, List<GroceryItem>> {
            return this.groupBy { it.bought!! }
                .toSortedMap {date1, date2 -> date2.compareTo(date1)}
        }
    }
}
