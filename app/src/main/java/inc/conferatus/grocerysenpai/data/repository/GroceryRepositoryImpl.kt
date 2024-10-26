package inc.conferatus.grocerysenpai.data.repository

import inc.conferatus.grocerysenpai.data.dao.GroceryDao
import inc.conferatus.grocerysenpai.data.entity.GroceryAndCategory
import inc.conferatus.grocerysenpai.data.entity.GroceryEntity
import inc.conferatus.grocerysenpai.data.repository.CategoryRepositoryImpl.Companion.categoryEntityToItem
import inc.conferatus.grocerysenpai.model.items.GroceryItem
import inc.conferatus.grocerysenpai.model.repository.GroceryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.Date

class GroceryRepositoryImpl(
    private val dao: GroceryDao
) : GroceryRepository {
    override fun getAllGroceriesStream(): Flow<List<GroceryItem>> { //todo GroceryAndCategory
        return dao.getGroceries().map { list -> list.map { groceryAndCategoryToItem(it) } }
    }

    override fun getHistoryGroceriesStream(): Flow<List<GroceryItem>> {
        return dao.getHistoryGroceries().map { list -> list.map { groceryAndCategoryToItem(it) } }
    }

    override fun getCurrentGroceriesStream(): Flow<List<GroceryItem>> {
        return dao.getCurrentGroceries().map { list -> list.map { groceryAndCategoryToItem(it) } }
    }

    override fun getGroceryStream(id: Int): Flow<GroceryItem?> {
        return dao.getGroceryById(id).map { item -> item?.let { groceryAndCategoryToItem(it) } }
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

    override suspend fun updateGroceryBoughtDate(item: GroceryItem, bought: ZonedDateTime?) {
        dao.updateGrocery(groceryItemToEntity(item.copy(bought = bought)))
    }

    companion object {
        fun groceryAndCategoryToItem(relation: GroceryAndCategory): GroceryItem {
            return GroceryItem(
                id = relation.groceryEntity.id,
                category = categoryEntityToItem(relation.categoryEntity),
                description = relation.groceryEntity.description,
                amount = relation.groceryEntity.amount,
                amountPostfix = relation.groceryEntity.amountPostfix,
                bought = relation.groceryEntity.bought
            )
        }

        fun groceryItemToEntity(item: GroceryItem): GroceryEntity {
            return GroceryEntity(
                id = item.id,
                categoryId = item.category.id,
                description = item.description,
                amount = item.amount,
                amountPostfix = item.amountPostfix,
                bought = item.bought
            )
        }
    }
}