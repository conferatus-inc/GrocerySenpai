package inc.conferatus.grocerysenpai.data.dao

import androidx.room.Dao
import androidx.room.Query
import inc.conferatus.grocerysenpai.data.entity.CategoryEntity

@Dao
interface CategoryDao {
    @Query("select * from categories")
    fun getCategories(): List<CategoryEntity>
}