package inc.conferatus.grocerysenpai.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import inc.conferatus.grocerysenpai.data.dao.CategoryDao
import inc.conferatus.grocerysenpai.data.dao.GroceryDao
import inc.conferatus.grocerysenpai.data.entity.CategoryEntity
import inc.conferatus.grocerysenpai.data.entity.GroceryEntity

@Database(
    entities = [
        CategoryEntity::class,
        GroceryEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class GroceryDatabase : RoomDatabase() {
    abstract val categoryDao: CategoryDao
    abstract val groceryDao: GroceryDao

    companion object {
        const val DATABASE_NAME = "groceries_db"
    }
}
