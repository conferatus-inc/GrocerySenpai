package inc.conferatus.grocerysenpai.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import inc.conferatus.grocerysenpai.data.entity.GroceryAndCategory
import inc.conferatus.grocerysenpai.data.entity.GroceryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryDao {
    @Transaction
    @Query("select * from groceries")
    fun getGroceries(): Flow<List<GroceryAndCategory>>

    @Transaction
    @Query("select * from groceries where id = :id")
    fun getGroceryById(id: Int): Flow<GroceryAndCategory?> // todo not needed?

    @Transaction
    @Query("select * from groceries where bought is null")
    fun getCurrentGroceries(): Flow<List<GroceryAndCategory>> // todo rename?

    @Transaction
    @Query("select * from groceries where bought is not null")
    fun getHistoryGroceries(): Flow<List<GroceryAndCategory>> // todo rename?

    @Update
    suspend fun updateGrocery(grocery: GroceryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrocery(grocery: GroceryEntity)

    @Delete
    suspend fun deleteGrocery(grocery: GroceryEntity)
}