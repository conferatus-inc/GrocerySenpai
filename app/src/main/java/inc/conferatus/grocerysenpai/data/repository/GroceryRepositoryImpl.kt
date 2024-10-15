package inc.conferatus.grocerysenpai.data.repository

import inc.conferatus.grocerysenpai.data.dao.GroceryDao
import inc.conferatus.grocerysenpai.data.entity.GroceryEntity
import inc.conferatus.grocerysenpai.data.repository.CategoryRepositoryImpl.Companion.categoryEntityToItem
import inc.conferatus.grocerysenpai.data.repository.CategoryRepositoryImpl.Companion.categoryItemToEntity
import inc.conferatus.grocerysenpai.model.items.GroceryItem
import inc.conferatus.grocerysenpai.model.repository.GroceryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class GroceryRepositoryImpl(
    private val dao: GroceryDao
) : GroceryRepository {
    override fun getAllGroceriesStream(): Flow<List<GroceryItem>> {
        return dao.getGroceries().map { list -> list.map { groceryEntityToItem(it) } }
    }

    override fun getHistoryGroceriesStream(): Flow<List<GroceryItem>> {
        return dao.getHistoryGroceries().map { list -> list.map { groceryEntityToItem(it) } }
    }

    override fun getCurrentGroceriesStream(): Flow<List<GroceryItem>> {
        return dao.getCurrentGroceries().map { list -> list.map { groceryEntityToItem(it) } }
    }

    override fun getGroceryStream(id: Int): Flow<GroceryItem?> {
        return dao.getGroceryById(id).map { item -> item?.let { groceryEntityToItem(it) } }
    }

    override suspend fun deleteGrocery(item: GroceryItem) {
        dao.deleteGrocery(groceryItemToEntity(item))
    }

    override suspend fun insertGrocery(item: GroceryItem) {
        dao.insertGrocery(groceryItemToEntity(item))
    }

    override suspend fun updateGrocery(item: GroceryItem) {
        dao.updateGrocery(groceryItemToEntity(item))
    }

    override suspend fun updateGroceryBoughtDate(item: GroceryItem, bought: Date?) {
        dao.updateGrocery(groceryItemToEntity(item.copy(bought = bought)))
    }

    companion object {
        fun groceryEntityToItem(entity: GroceryEntity): GroceryItem {
            return GroceryItem(
                id = entity.id,
                category = categoryEntityToItem(entity.category),
                description = entity.description,
                amount = entity.amount,
                amountPostfix = entity.amountPostfix,
                bought = entity.bought
            )
        }

        fun groceryItemToEntity(item: GroceryItem): GroceryEntity {
            return GroceryEntity(
                id = item.id,
                category = categoryItemToEntity(item.category),
                description = item.description,
                amount = item.amount,
                amountPostfix = item.amountPostfix,
                bought = item.bought
            )
        }
    }
}