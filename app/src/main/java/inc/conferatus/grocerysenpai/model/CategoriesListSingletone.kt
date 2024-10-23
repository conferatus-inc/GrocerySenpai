package inc.conferatus.grocerysenpai.model

import inc.conferatus.grocerysenpai.model.items.CategoryItem
import inc.conferatus.grocerysenpai.model.repository.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriesListSingletone @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    var categories : List<CategoryItem> = categoryRepository.getAllCategories()
        private set
}
