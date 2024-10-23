package inc.conferatus.grocerysenpai.model.repository

import androidx.room.Delete
import inc.conferatus.grocerysenpai.data.entity.CategoryEntity
import inc.conferatus.grocerysenpai.model.items.CategoryItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface CategoryRepository {
    fun getAllCategories(): List<CategoryItem> // not stream, todo
}
