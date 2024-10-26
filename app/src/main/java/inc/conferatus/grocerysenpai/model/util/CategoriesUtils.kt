package inc.conferatus.grocerysenpai.model.util

import inc.conferatus.grocerysenpai.model.items.CategoryItem

class CategoriesUtils {
    companion object {
        // TODO: возможно фильтровать с учётом грамматических ошибок
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

        fun List<CategoryItem>.byName(
            name: String
        ): CategoryItem? {
            return this.firstOrNull { it.name.lowercase() == name.lowercase() }
        }
    }
}
