package inc.conferatus.grocerysenpai.model.repository

import androidx.room.Delete
import inc.conferatus.grocerysenpai.data.entity.CategoryEntity
import inc.conferatus.grocerysenpai.data.entity.GroceryEntity
import inc.conferatus.grocerysenpai.model.items.CategoryItem
import inc.conferatus.grocerysenpai.model.items.CurrentGroceryItem
import inc.conferatus.grocerysenpai.model.items.GroceryItem
import inc.conferatus.grocerysenpai.model.items.HistoryGroceryItem
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface GroceryRepository {
    fun getAllGroceriesStream(): Flow<List<GroceryItem>>

    fun getHistoryGroceriesStream(): Flow<List<HistoryGroceryItem>>
    fun getCurrentGroceriesStream(): Flow<List<CurrentGroceryItem>>

    fun getGroceryStream(id: Int): Flow<GroceryItem?>

    suspend fun deleteGrocery(grocery: GroceryItem)

    suspend fun insertGrocery(grocery: GroceryItem)

    suspend fun updateGroceryTimestamp(grocery: GroceryItem, date: Date)

    suspend fun currentGroceryToHistory(grocery: CurrentGroceryItem)
}
