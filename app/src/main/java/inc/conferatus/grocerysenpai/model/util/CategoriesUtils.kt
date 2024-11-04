package inc.conferatus.grocerysenpai.model.util

import inc.conferatus.grocerysenpai.model.items.CategoryItem

class CategoriesUtils {
    companion object {
        // TODO: возможно фильтровать с учётом грамматических ошибок
        fun List<CategoryItem>.filterCategories(
            inputBeginning: String,
            limit: Int = this.size
        ): List<CategoryItem> {
//            return this.filter {
//                it.name.startsWith(inputBeginning, ignoreCase = true)
//                        || it.name.contains(inputBeginning, ignoreCase = true)
//            }
            return this.sortedWith { string1, string2 ->
                compareStringWithInput(string1.name, string2.name, inputBeginning)
            }.take(limit)
        }

        // todo сортировка по частоте использования + нахождению в списке
        fun List<CategoryItem>.sortCategories(
            limit: Int = this.size,
            input: String
            // comparator??
            // something else?
        ): List<CategoryItem> {
//            return this.sortedWith { string1, string2 ->
//                compareStringWithInput(string1.name, string2.name, input)
//            }.take(limit)
            return this.take(limit)
        }

        fun List<CategoryItem>.byName(
            name: String
        ): CategoryItem? {
            return this.firstOrNull { it.name.lowercase() == name.lowercase() }
        }

        private fun compareStringWithInput(string1: String, string2: String, input: String): Int {
            return compareValues(
                optimalStringAlignmentDistance(string1, input),
                optimalStringAlignmentDistance(string2, input)
            )
//            return 0
//            if (string1 == string2) {
//                return 0
//            }
//            if (string1.startsWith(input, ignoreCase = true)) {
//                if (string2.startsWith(input, ignoreCase = true)) {
//                    return compareValues(string1.length, string2.length)
//                }
//                return -1
//            }
//            if (string2.startsWith(input, ignoreCase = true)) {
//                return 1
//            }
//            return compareValues(string1.length, string2.length)
        }

        fun optimalStringAlignmentDistance(s1: String, s2: String): Int {
            // Create a table to store the results of subproblems
//            val dp = Array(s1.length + 1) IntArray (s2.length + 1)
            val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }

            // Initialize the table
            for (i in 0..s1.length) {
                dp[i][0] = i
            }
            for (j in 0..s2.length) {
                dp[0][j] = j
            }

            // Populate the table using dynamic programming
            for (i in 1..s1.length) {
                for (j in 1..s2.length) {
                    if (s1[i - 1] == s2[j - 1]) {
                        dp[i][j] = dp[i - 1][j - 1]
                    } else {
                        dp[i][j] =
                            1 + minOf(dp[i - 1][j], minOf(dp[i][j - 1], dp[i - 1][j - 1]))
                    }
                }
            }

            // Return the edit distance
            return dp[s1.length][s2.length]
        }
    }
}
