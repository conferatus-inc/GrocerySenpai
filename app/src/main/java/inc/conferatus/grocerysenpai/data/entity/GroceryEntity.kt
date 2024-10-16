package inc.conferatus.grocerysenpai.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.Date

@Entity(tableName = "groceries")
data class GroceryEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val categoryId: Int,
    val description: String,
    val amount: Int,
    val amountPostfix: String,
    val bought: Date?
)

data class GroceryAndCategory(
    @Embedded val categoryEntity: CategoryEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "categoryId"
    )
    val groceryEntity: GroceryEntity
)
