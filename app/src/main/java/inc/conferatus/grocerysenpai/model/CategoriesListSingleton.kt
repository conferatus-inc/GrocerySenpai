package inc.conferatus.grocerysenpai.model

import inc.conferatus.grocerysenpai.model.items.CategoryItem
import inc.conferatus.grocerysenpai.model.repository.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriesListSingleton @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    val categories : List<CategoryItem> = categoryRepository.getAllCategories()
}
