package inc.conferatus.grocerysenpai.data.repository

import inc.conferatus.grocerysenpai.data.dao.CategoryDao
import inc.conferatus.grocerysenpai.data.entity.CategoryEntity
import inc.conferatus.grocerysenpai.model.items.CategoryItem
import inc.conferatus.grocerysenpai.model.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val dao : CategoryDao
) : CategoryRepository {
    override fun getAllCategoriesStream(): Flow<List<CategoryItem>> {
        return dao.getCategories().map { list -> list.map { categoryEntityToItem(it) } }
    }

    override fun getCategoryStream(id: Int): Flow<CategoryItem?> {
        return dao.getCategoryById(id).map { item -> item?.let { categoryEntityToItem(it) } }
    }

    override fun getCategoryStreamByName(name: String): Flow<CategoryItem?> {
        return dao.getCaterogyByName(name).map { item -> item?.let { categoryEntityToItem(it) } }
    }

    override suspend fun insertCategory(category: CategoryItem) {
        dao.insertCategory(categoryItemToEntity(category))
    }

    override suspend fun deleteCategory(category: CategoryItem) {
        dao.deleteCategory(categoryItemToEntity(category))
    }

    companion object {
        fun categoryEntityToItem(entity: CategoryEntity): CategoryItem {
            return CategoryItem(
                id = entity.id,
                name = entity.name
            )
        }

        fun categoryItemToEntity(item: CategoryItem): CategoryEntity {
            return CategoryEntity(
                id = item.id,
                name = item.name
            )
        }
    }
}