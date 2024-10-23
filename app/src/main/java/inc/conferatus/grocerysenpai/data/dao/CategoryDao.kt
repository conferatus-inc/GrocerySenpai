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
    fun getCategories(): List<CategoryEntity>
}