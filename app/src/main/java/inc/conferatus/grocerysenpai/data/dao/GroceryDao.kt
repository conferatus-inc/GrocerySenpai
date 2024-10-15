package inc.conferatus.grocerysenpai.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import inc.conferatus.grocerysenpai.data.entity.CategoryEntity
import inc.conferatus.grocerysenpai.data.entity.GroceryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryDao {
    @Query("select * from groceries")
    fun getGroceries(): Flow<List<GroceryEntity>>

    @Query("select * from groceries where id = :id")
    fun getGroceryById(id: Int): Flow<GroceryEntity?> // todo not needed?

    @Query("select * from groceries where bought is null")
    fun getCurrentGroceries(): Flow<List<GroceryEntity>> // todo rename?

    @Query("select * from groceries where bought is not null")
    fun getHistoryGroceries(): Flow<List<GroceryEntity>> // todo rename?

    @Update
    suspend fun updateGrocery(grocery: GroceryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrocery(grocery: GroceryEntity)

    @Delete
    suspend fun deleteGrocery(grocery: GroceryEntity)
}