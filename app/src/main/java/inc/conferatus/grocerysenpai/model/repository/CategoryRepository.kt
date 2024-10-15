package inc.conferatus.grocerysenpai.model.repository

import androidx.room.Delete
import inc.conferatus.grocerysenpai.data.entity.CategoryEntity
import inc.conferatus.grocerysenpai.model.items.CategoryItem
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategoriesStream(): Flow<List<CategoryItem>>

    fun getCategoryStream(id: Int): Flow<CategoryItem?>

    fun getCategoryStreamByName(name: String): Flow<CategoryItem?>

    suspend fun insertCategory(category: CategoryItem)

    @Delete
    suspend fun deleteCategory(category: CategoryItem)
}
