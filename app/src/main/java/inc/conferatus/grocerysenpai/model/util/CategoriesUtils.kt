package inc.conferatus.grocerysenpai.model.util

import inc.conferatus.grocerysenpai.model.items.CategoryItem

// TODO: возможно фильтровать с учётом грамматических ошибок
class CategoriesUtils {
    companion object {
        fun filter(
            inputBeginning: String,
            categories: List<CategoryItem>,
            limit: Int = Int.MAX_VALUE
        ): List<CategoryItem> {
            return categories
                .filter { it.name.lowercase().startsWith(inputBeginning.lowercase()) }
                .take(limit)
        }

        // todo сортировка по частоте использования + нахождению в списке
        fun sort(
            categories: List<CategoryItem>,
            limit: Int = Int.MAX_VALUE
            // comparator??
            // something else?
        ): List<CategoryItem> {
            return categories
                .shuffled()
                .take(limit)
        }

        fun byName(
            name: String,
            allCategories: List<CategoryItem> // todo тут можно не статиками сделать а нормально
        ): CategoryItem? {
                return allCategories.firstOrNull { it.name.lowercase() == name.lowercase() }
        }
    }
}
