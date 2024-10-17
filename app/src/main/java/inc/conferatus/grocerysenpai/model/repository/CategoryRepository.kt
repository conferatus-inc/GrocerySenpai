package inc.conferatus.grocerysenpai.model.repository

import androidx.room.Delete
import inc.conferatus.grocerysenpai.data.entity.CategoryEntity
import inc.conferatus.grocerysenpai.model.items.CategoryItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface CategoryRepository {
    fun getAllCategoriesStream(): List<CategoryItem> // not stream, todo

    fun getCategoryStream(id: Int): Flow<CategoryItem?>

    fun getCategoryStreamByName(name: String): Flow<CategoryItem?>

    suspend fun insertCategory(category: CategoryItem)

    @Delete
    suspend fun deleteCategory(category: CategoryItem)
}
