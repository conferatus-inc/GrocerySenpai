package inc.conferatus.grocerysenpai.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import inc.conferatus.grocerysenpai.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("select * from categories")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Query("select * from categories where id = :id")
    suspend fun getCategoryById(id: Int): CategoryEntity // todo not needed?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)
}