package inc.conferatus.grocerysenpai.data.repository

import inc.conferatus.grocerysenpai.data.dao.GroceryDao
import inc.conferatus.grocerysenpai.data.entity.GroceryEntity
import inc.conferatus.grocerysenpai.data.repository.CategoryRepositoryImpl.Companion.categoryEntityToItem
import inc.conferatus.grocerysenpai.data.repository.CategoryRepositoryImpl.Companion.categoryItemToEntity
import inc.conferatus.grocerysenpai.model.items.CurrentGroceryItem
import inc.conferatus.grocerysenpai.model.items.GroceryItem
import inc.conferatus.grocerysenpai.model.items.HistoryGroceryItem
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

    override fun getHistoryGroceriesStream(): Flow<List<HistoryGroceryItem>> {
        return dao.getHistoryGroceries().map { list -> list.map { groceryEntityToHistoryItem(it) } }
    }

    override fun getCurrentGroceriesStream(): Flow<List<CurrentGroceryItem>> {
        return dao.getCurrentGroceries().map { list -> list.map { groceryEntityToCurrentItem(it) } }
    }

    override fun getGroceryStream(id: Int): Flow<GroceryItem?> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGrocery(grocery: GroceryItem) {
        TODO("Not yet implemented")
    }

    override suspend fun insertGrocery(grocery: GroceryItem) {
        TODO("Not yet implemented")
    }

    override suspend fun updateGroceryTimestamp(grocery: GroceryItem, date: Date) {
        TODO("Not yet implemented")
    }

    // TODO TODOOOOOOOOOOOOOOOOOOOOOOOOOO НИЖЕ НАСРАНО!!!!!!!!!!1
    companion object {
        fun groceryEntityToItem(entity: GroceryEntity): GroceryItem {
            if (entity.bought != null) {
                return groceryEntityToHistoryItem(entity)
            } else {
                return groceryEntityToCurrentItem(entity)
            }
        }

        fun groceryItemToEntity(item: GroceryItem): GroceryEntity {
            return when (item) {
                is HistoryGroceryItem -> historyItemToGroceryEntity(item)
                is CurrentGroceryItem -> currentItemToGroceryEntity(item)
            }
        }
        private fun groceryEntityToHistoryItem(entity: GroceryEntity): HistoryGroceryItem {
            assert(entity.bought != null)

            return HistoryGroceryItem(
                id = entity.id,
                category = categoryEntityToItem(entity.category),
                description = entity.description,
                amount = entity.amount,
                amountPostfix = entity.amountPostfix,
                bought = entity.bought!!
            )
        }

        private fun groceryEntityToCurrentItem(entity: GroceryEntity): CurrentGroceryItem {
            assert(entity.bought == null)

            return CurrentGroceryItem(
                id = entity.id,
                category = categoryEntityToItem(entity.category),
                description = entity.description,
                amount = entity.amount,
                amountPostfix = entity.amountPostfix
            )
        }

        private fun historyItemToGroceryEntity(item: HistoryGroceryItem): GroceryEntity {
            return GroceryEntity(
                id = item.id,
                category = categoryItemToEntity(item.category),
                amount = item.amount,
                amountPostfix = item.amountPostfix,
                description = item.description,
                bought = item.bought
            )
        }

        private fun currentItemToGroceryEntity(item: CurrentGroceryItem): GroceryEntity {
            return GroceryEntity(
                id = item.id,
                category = categoryItemToEntity(item.category),
                amount = item.amount,
                amountPostfix = item.amountPostfix,
                description = item.description,
                bought = null
            )
        }
    }
}