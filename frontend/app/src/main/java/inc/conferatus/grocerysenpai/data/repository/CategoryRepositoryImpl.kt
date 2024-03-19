package inc.conferatus.grocerysenpai.data.repository

import inc.conferatus.grocerysenpai.data.dao.CategoryDao
import inc.conferatus.grocerysenpai.data.entity.CategoryEntity
import inc.conferatus.grocerysenpai.model.items.CategoryItem
import inc.conferatus.grocerysenpai.model.repository.CategoryRepository

class CategoryRepositoryImpl(
    private val dao : CategoryDao
) : CategoryRepository {
    override fun getAllCategories(): List<CategoryItem> {
        return dao.getCategories().map {  categoryEntityToItem(it)  }
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