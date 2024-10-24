package inc.conferatus.grocerysenpai.model.util

import inc.conferatus.grocerysenpai.model.items.CategoryItem

// TODO: возможно фильтровать с учётом грамматических ошибок
class CategoriesUtils {
    companion object {
        fun List<CategoryItem>.filterCategories(
            inputBeginning: String,
            limit: Int = this.size
        ): List<CategoryItem> {
            return this.filter { it.name.lowercase().startsWith(inputBeginning.lowercase()) }
                .take(limit)
        }

        // todo сортировка по частоте использования + нахождению в списке
        fun List<CategoryItem>.sortCategories(
            limit: Int = this.size
            // comparator??
            // something else?
        ): List<CategoryItem> {
            return this.shuffled()
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
