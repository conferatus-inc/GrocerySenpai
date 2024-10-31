package inc.conferatus.grocerysenpai.model.util

import inc.conferatus.grocerysenpai.model.items.CategoryItem

class CategoriesUtils {
    companion object {
        // TODO: возможно фильтровать с учётом грамматических ошибок
        fun List<CategoryItem>.filterCategories(
            inputBeginning: String,
            limit: Int = this.size
        ): List<CategoryItem> {
            return this.filter {
                it.name.startsWith(inputBeginning, ignoreCase = true)
                        || it.name.contains(inputBeginning, ignoreCase = true)
            }
                .take(limit)
        }

        // todo сортировка по частоте использования + нахождению в списке
        fun List<CategoryItem>.sortCategories(
            limit: Int = this.size,
            input: String
            // comparator??
            // something else?
        ): List<CategoryItem> {
            return this.sortedWith { string1, string2 ->
                compareStringWithInput(string1.name, string2.name, input)
            }.take(limit)
        }

        fun List<CategoryItem>.byName(
            name: String
        ): CategoryItem? {
            return this.firstOrNull { it.name.lowercase() == name.lowercase() }
        }

        private fun compareStringWithInput(string1: String, string2: String, input: String): Int {
            if (string1 == string2) {
                return 0
            }
            if (string1.startsWith(input, ignoreCase = true)) {
                if (string2.startsWith(input, ignoreCase = true)) {
                    return compareValues(string1.length, string2.length)
                }
                return -1
            }
            if (string2.startsWith(input, ignoreCase = true)) {
                return 1
            }
            return compareValues(string1.length, string2.length)
        }
    }
}
