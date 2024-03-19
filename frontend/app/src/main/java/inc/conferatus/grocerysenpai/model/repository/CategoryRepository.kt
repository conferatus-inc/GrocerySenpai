package inc.conferatus.grocerysenpai.model.repository

import inc.conferatus.grocerysenpai.model.items.CategoryItem
import javax.inject.Singleton

@Singleton
interface CategoryRepository {
    fun getAllCategories(): List<CategoryItem>
}
