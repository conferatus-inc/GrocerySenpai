package inc.conferatus.grocerysenpai.model.repository

import inc.conferatus.grocerysenpai.model.items.GroceryItem
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface GroceryRepository {
    fun getAllGroceriesStream(): Flow<List<GroceryItem>>

    fun getHistoryGroceriesStream(): Flow<List<GroceryItem>>
    fun getCurrentGroceriesStream(): Flow<List<GroceryItem>>

    fun getGroceryStream(id: Int): Flow<GroceryItem?>

    suspend fun deleteGrocery(item: GroceryItem)

    suspend fun insertGrocery(item: GroceryItem)

    suspend fun updateGrocery(item: GroceryItem)

    suspend fun updateGroceryBoughtDate(item: GroceryItem, bought: Date?) // по моему это логичнее в бизнесе сделать
}
